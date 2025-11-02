package com.tpe.PetStore.service;

import com.tpe.PetStore.dto.PetDTO;
import com.tpe.PetStore.entity.Pet;
import com.tpe.PetStore.entity.user.User;
import com.tpe.PetStore.enums.Category;
import com.tpe.PetStore.enums.Gender;
import com.tpe.PetStore.exception.ConflictException;
import com.tpe.PetStore.exception.ResourceNotFoundException;
import com.tpe.PetStore.repository.PetRepository;
import com.tpe.PetStore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PetService petService;

    private PetDTO validPetDTO;
    private Pet validPet;
    private User storeOwner;
    private User customer;

    @BeforeEach
    void setUp() {
        // Setup valid PetDTO
        validPetDTO = new PetDTO();
        validPetDTO.setName("Buddy");
        validPetDTO.setAge(3);
        validPetDTO.setPrice(500);
        validPetDTO.setCategory(Category.DOG);
        validPetDTO.setGender(Gender.MALE);
        
        User storeOwnerDTO = new User();
        storeOwnerDTO.setFirstName("Ali");
        storeOwnerDTO.setLastName("Gürbüz");
        validPetDTO.setStoreOwner(storeOwnerDTO);

        // Setup valid Pet entity
        validPet = new Pet();
        validPet.setId(1L);
        validPet.setName("Buddy");
        validPet.setAge(3);
        validPet.setPrice(500);
        validPet.setCategory(Category.DOG);
        validPet.setGender(Gender.MALE);

        // Setup User entities
        storeOwner = new User();
        storeOwner.setId(1L);
        storeOwner.setFirstName("Ali");
        storeOwner.setLastName("Gürbüz");
        storeOwner.setUsername("StoreOwner");

        customer = new User();
        customer.setId(2L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setUsername("john_doe");
    }

    @Test
    void savePet_ShouldReturnSuccessMessage_WhenValidPetDTO() {
        // Given
        when(petRepository.existsByName("Buddy")).thenReturn(false);
        when(userRepository.findByFirstNameAndLastName("Ali", "Gürbüz")).thenReturn(storeOwner);
        when(petRepository.save(any(Pet.class))).thenReturn(validPet);

        // When
        Map<String, Object> result = petService.savePet(validPetDTO);

        // Then
        assertNotNull(result);
        assertEquals("Pet saved successfully", result.get("message"));
        verify(petRepository, times(1)).existsByName("Buddy");
        verify(userRepository, times(1)).findByFirstNameAndLastName("Ali", "Gürbüz");
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void savePet_ShouldThrowConflictException_WhenPetNameExists() {
        // Given
        when(petRepository.existsByName("Buddy")).thenReturn(true);

        // When & Then
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            petService.savePet(validPetDTO);
        });

        assertEquals("Pet already exists", exception.getMessage());
        verify(petRepository, times(1)).existsByName("Buddy");
        verify(userRepository, never()).findByFirstNameAndLastName(anyString(), anyString());
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void savePet_ShouldThrowConflictException_WhenStoreOwnerNotFound() {
        // Given
        when(petRepository.existsByName("Buddy")).thenReturn(false);
        when(userRepository.findByFirstNameAndLastName("Ali", "Gürbüz")).thenReturn(null);

        // When & Then
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            petService.savePet(validPetDTO);
        });

        assertEquals("Store owner not found", exception.getMessage());
        verify(petRepository, times(1)).existsByName("Buddy");
        verify(userRepository, times(1)).findByFirstNameAndLastName("Ali", "Gürbüz");
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void savePet_ShouldHandleCustomer_WhenCustomerProvided() {
        // Given
        User customerDTO = new User();
        customerDTO.setUsername("john_doe");
        validPetDTO.setCustomer(customerDTO);

        when(petRepository.existsByName("Buddy")).thenReturn(false);
        when(userRepository.findByFirstNameAndLastName("Ali", "Gürbüz")).thenReturn(storeOwner);
        when(userRepository.findByUsername("john_doe")).thenReturn(customer);
        when(petRepository.save(any(Pet.class))).thenReturn(validPet);

        // When
        Map<String, Object> result = petService.savePet(validPetDTO);

        // Then
        assertNotNull(result);
        assertEquals("Pet saved successfully", result.get("message"));
        verify(userRepository, times(1)).findByUsername("john_doe");
    }

    @Test
    void savePet_ShouldThrowConflictException_WhenCustomerNotFound() {
        // Given
        User customerDTO = new User();
        customerDTO.setUsername("nonexistent_user");
        validPetDTO.setCustomer(customerDTO);

        when(petRepository.existsByName("Buddy")).thenReturn(false);
        when(userRepository.findByFirstNameAndLastName("Ali", "Gürbüz")).thenReturn(storeOwner);
        when(userRepository.findByUsername("nonexistent_user")).thenReturn(null);

        // When & Then
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            petService.savePet(validPetDTO);
        });

        assertEquals("Customer not found", exception.getMessage());
    }

//    @Test
//    void findAll_ShouldReturnListOfPets_WhenPetsExist() {
//        // Given
//        List<PetDTO> pets = List.of(validPetDTO);
//        when(petService.findAll()).thenReturn(pets);
//
//        // When
//        List<PetDTO> result = petService.findAll();
//
//        // Then
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(validPetDTO, result.get(0));
//        verify(petRepository, times(1)).findAll();
//    }

    @Test
    void findAll_ShouldThrowResourceNotFoundException_WhenNoPetsExist() {
        // Given
        when(petRepository.findAll()).thenReturn(new ArrayList<>());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.findAll();
        });

        assertEquals("Pet not found", exception.getMessage());
        verify(petRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnPet_WhenPetExists() {
        // Given
        Long petId = 1L;
        when(petRepository.findById(petId)).thenReturn(Optional.of(validPet));

        // When
        Pet result = petService.findById(petId);

        // Then
        assertNotNull(result);
        assertEquals(validPet, result);
        verify(petRepository, times(1)).findById(petId);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenPetNotFound() {
        // Given
        Long petId = 999L;
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.findById(petId);
        });

        assertEquals("Pet not found", exception.getMessage());
        verify(petRepository, times(1)).findById(petId);
    }

    @Test
    void findByName_ShouldReturnPet_WhenPetExists() {
        // Given
        String petName = "Buddy";
        when(petRepository.existsByName(petName)).thenReturn(true);
        when(petRepository.findByName(petName)).thenReturn(validPet);

        // When
        Pet result = petService.findByName(petName);

        // Then
        assertNotNull(result);
        assertEquals(validPet, result);
        verify(petRepository, times(1)).existsByName(petName);
        verify(petRepository, times(1)).findByName(petName);
    }

    @Test
    void findByName_ShouldThrowResourceNotFoundException_WhenPetNotFound() {
        // Given
        String petName = "NonexistentPet";
        when(petRepository.existsByName(petName)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            petService.findByName(petName);
        });

        assertEquals("Pet not found", exception.getMessage());
        verify(petRepository, times(1)).existsByName(petName);
        verify(petRepository, never()).findByName(anyString());
    }

    @Test
    void updatePet_ShouldSavePet_WhenPetExists() {
        // Given
        when(petRepository.save(validPet)).thenReturn(validPet);

        // When
        petService.updatePet(validPet);

        // Then
        verify(petRepository, times(1)).save(validPet);
    }
}
