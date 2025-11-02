package com.tpe.PetStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpe.PetStore.dto.PetDTO;
import com.tpe.PetStore.entity.Pet;
import com.tpe.PetStore.entity.user.User;
import com.tpe.PetStore.enums.Category;
import com.tpe.PetStore.enums.Gender;
import com.tpe.PetStore.enums.Role;
import com.tpe.PetStore.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createPet_ShouldReturnCreatedResponse_WhenValidPetDTO() throws Exception {
        // Given
        PetDTO petDTO = createValidPetDTO();
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "Pet saved successfully");
        
        when(petService.savePet(any(PetDTO.class))).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/pet/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Pet saved successfully"));

        verify(petService, times(1)).savePet(any(PetDTO.class));
    }

    @Test
    void createPet_ShouldReturnBadRequest_WhenInvalidPetDTO() throws Exception {
        // Given
        PetDTO invalidPetDTO = new PetDTO(); // Empty DTO with null values

        // When & Then
        mockMvc.perform(post("/pet/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPetDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPet_ShouldHandleServiceException() throws Exception {
        // Given
        PetDTO petDTO = createValidPetDTO();
        when(petService.savePet(any(PetDTO.class)))
                .thenThrow(new RuntimeException("Service error"));

        // When & Then
        mockMvc.perform(post("/pet/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createPet_UnitTest_ShouldReturnCorrectResponse() {
        // Given
        PetDTO petDTO = createValidPetDTO();
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "Pet saved successfully");
        
        when(petService.savePet(petDTO)).thenReturn(expectedResponse);

        // When
        ResponseEntity<Map<String, Object>> response = petController.createPet(petDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Pet saved successfully", response.getBody().get("message"));
        verify(petService, times(1)).savePet(petDTO);
    }

    @Test
    void createPet_UnitTest_ShouldHandleNullResponse() {
        // Given
        PetDTO petDTO = createValidPetDTO();
        when(petService.savePet(petDTO)).thenReturn(null);

        // When
        ResponseEntity<Map<String, Object>> response = petController.createPet(petDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Helper method to create valid PetDTO
    private PetDTO createValidPetDTO() {
        PetDTO petDTO = new PetDTO();
        petDTO.setName("Buddy");
        petDTO.setAge(3);
        petDTO.setPrice(500);
        petDTO.setCategory(Category.DOG);
        petDTO.setGender(Gender.MALE);
        
        User storeOwner = new User();
        storeOwner.setFirstName("Ali");
        storeOwner.setLastName("Gürbüz");
        petDTO.setStoreOwner(storeOwner);
        
        return petDTO;
    }

    // Helper method to create valid Pet entity
    private Pet createValidPet() {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Buddy");
        pet.setAge(3);
        pet.setPrice(500);
        pet.setCategory(Category.DOG);
        pet.setGender(Gender.MALE);
        
        User storeOwner = new User();
        storeOwner.setId(1L);
        storeOwner.setFirstName("Ali");
        storeOwner.setLastName("Gürbüz");
        storeOwner.setUsername("StoreOwner");
        pet.setStoreOwner(storeOwner);
        
        return pet;
    }
}