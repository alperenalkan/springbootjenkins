package com.tpe.PetStore.service;

import com.tpe.PetStore.dto.PetDTO;
import com.tpe.PetStore.dto.UserLoginRequestDTO;
import com.tpe.PetStore.dto.UserRegisterRequestDTO;
import com.tpe.PetStore.dto.UserRegisterResponseDTO;
import com.tpe.PetStore.entity.Pet;
import com.tpe.PetStore.entity.user.User;
import com.tpe.PetStore.entity.user.UserRole;
import com.tpe.PetStore.enums.Role;
import com.tpe.PetStore.exception.ConflictException;
import com.tpe.PetStore.exception.ResourceNotFoundException;
import com.tpe.PetStore.mapper.UserMapper;
import com.tpe.PetStore.repository.UserRepository;
import com.tpe.PetStore.security.jwt.JwtUtils;
import com.tpe.PetStore.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserRoleService userRoleService;
    private final PetService petService;
    private final UserMapper userMapper;

    public Map<String, Object> login(UserLoginRequestDTO dto) {
        String username = dto.getUsername();
        String password = dto.getPassword(); //duz sifre. 12345678 gibi.

        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        //Sikinti olursa SecurityContext'e ekleme yapilaiblir.

        String jwt = jwtUtils.generateJWT(auth);

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Map<String, Object> map = new HashMap<>();
        map.put("message","Login successful!");
        map.put("jwt", jwt);
        map.put("roles", roles);

        return map;
    }

    public Map<String, Object> saveUser(UserRegisterRequestDTO dto) {
        if (userRepository.existsByUsernameOrEmail(dto.getUsername(), dto.getEmail())) {
            throw new ConflictException("Username or email is already in use!");
        }

        UserRole userRole = userRoleService.findByRoleName(dto.getRole());

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserRole(userRole);

        userRepository.save(user);

        Map<String, Object> map = new HashMap<>();
        map.put("message","Register successful!");
        return map;
    }

    public int getAdminCount() {
        return userRepository.getUserCountByRole(Role.ADMIN);
    }
    public int getStoreOwnerCount() {
        return userRepository.getUserCountByRole(Role.STORE_OWNER);
    }

//    public List<UserRegisterResponseDTO> findAll() {
//        if (userRepository.findAll().isEmpty()) {
//            throw new ResourceNotFoundException("User not found!");
//        }
//       List<User> uses = userRepository.findAll();
//        List<UserRegisterResponseDTO> dtos = new ArrayList<>();
//        for (User user : uses) {
//            UserRegisterResponseDTO dto = new UserRegisterResponseDTO();
//            dto.setId(user.getId());
//            dto.setFirstName(user.getFirstName());
//            dto.setLastName(user.getLastName());
//            dto.setEmail(user.getEmail());
//            dto.setUsername(user.getUsername());
//            dtos.add(dto);
//        }
//        return dtos;
//
//
//    }
    public List<UserRegisterResponseDTO> findAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("User not found!");
            // Tercihen boş liste döndürüp 200/204 de yapabilirsin: return List.of();
        }

        return userMapper.toDTOList(users);
    }



    public UserRegisterResponseDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        return userMapper.toDTO(user);
    }


    @Transactional
    public Map<String, Object> buyPet(Long userId, Long petId) {
        User customer = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Pet pet = petService.findById(petId);

        if (pet.getCustomer() != null) {
            throw new ConflictException("Pet already sold");
        }

        pet.setCustomer(customer);
        petService.updatePet(pet);

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("message", "Buy pet successful!");
        res.put("petId", pet.getId());
        res.put("customer", customer.getUsername());
        return res;
    }

}
