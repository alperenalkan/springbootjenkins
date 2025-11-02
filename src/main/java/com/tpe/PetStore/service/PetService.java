package com.tpe.PetStore.service;

import com.tpe.PetStore.dto.PetDTO;
import com.tpe.PetStore.entity.Pet;
import com.tpe.PetStore.entity.user.User;
import com.tpe.PetStore.exception.ConflictException;
import com.tpe.PetStore.exception.ResourceNotFoundException;
import com.tpe.PetStore.repository.PetRepository;
import com.tpe.PetStore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;



    public Map<String, Object> savePet(PetDTO dto) {
        if (petRepository.existsByName(dto.getName())) {
            throw new ConflictException("Pet already exists");
        }
        
        // StoreOwner'ı veritabanından çek (firstName + lastName ile)
        User storeOwner = userRepository.findByFirstNameAndLastName(
                dto.getStoreOwner().getFirstName(),
                dto.getStoreOwner().getLastName()
        );
        if (storeOwner == null) {
            throw new ConflictException("Store owner not found");
        }
        
        // Customer kontrolü - sadece customer varsa kontrol et
        User customer = null;
        if (dto.getCustomer() != null) {
            customer = userRepository.findByUsername(dto.getCustomer().getUsername());
            if (customer == null) {
                throw new ConflictException("Customer not found");
            }
        }
        
        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setAge(dto.getAge());
        pet.setCustomer(customer); // null olabilir
        pet.setCategory(dto.getCategory());
        pet.setPrice(dto.getPrice());
        pet.setStoreOwner(storeOwner); // Veritabanından çekilen User
        pet.setGender(dto.getGender());
        petRepository.save(pet);

        Map<String, Object> map = new HashMap<>();
        map.put("message", "Pet saved successfully");
        //map.put("pet", pet);
        return map;

    }

//    public List<PetDTO> findAll() {
//        if (petRepository.findAll().isEmpty()) {
//            throw new ResourceNotFoundException("Pet not found");
//        }
//        return petRepository.findAll();
//    }
    public Pet findById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
    }

    public Pet findByName(String name) {
        if (petRepository.existsByName(name)) {
            return petRepository.findByName(name);

        }
        throw new ResourceNotFoundException("Pet not found");
   }

    public void updatePet(Pet pet) {
        petRepository.save(pet);
    }

    public List<PetDTO> findAll() {
        if (petRepository.findAll().isEmpty()) {
            throw new ResourceNotFoundException("Pet not found");
        }
        List<PetDTO> petDTOList = new ArrayList<>();
        for (Pet pet : petRepository.findAll()) {
            PetDTO petDTO = new PetDTO();
            petDTO.setName(pet.getName());
            petDTO.setAge(pet.getAge());
            petDTO.setCategory(pet.getCategory());
            petDTO.setGender(pet.getGender());
            petDTO.setPrice(pet.getPrice());
            petDTO.setStoreOwner(pet.getStoreOwner());
            petDTO.setCustomer(pet.getCustomer());
            petDTOList.add(petDTO);

        }
        return petDTOList;

    }
}
