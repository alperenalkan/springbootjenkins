package com.tpe.PetStore.security.service;

import com.tpe.PetStore.entity.user.User;
import com.tpe.PetStore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user != null){
            return new UserDetailsImpl(user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUserRole());
        }else{
            throw new UsernameNotFoundException("Username: " + username + " not found.");
        }
    }
}
