package com.tpe.PetStore.controller;

import com.tpe.PetStore.dto.UserRegisterResponseDTO;
import com.tpe.PetStore.entity.Pet;
import com.tpe.PetStore.entity.user.User;
import com.tpe.PetStore.enums.Category;
import com.tpe.PetStore.enums.Gender;
import com.tpe.PetStore.enums.Role;
import com.tpe.PetStore.service.PetService;
import com.tpe.PetStore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PetService petService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        // Setup can be done here if needed
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers_WhenUsersExist() {
        // Given
        List<UserRegisterResponseDTO> expectedUsers = createUserList();
        when(userService.findAll()).thenReturn(expectedUsers);

        // When
        ResponseEntity<List<UserRegisterResponseDTO>> response = userController.getAllUsers();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUsers, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).findAll();
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() {
        // Given
        List<UserRegisterResponseDTO> emptyList = new ArrayList<>();
        when(userService.findAll()).thenReturn(emptyList);

        // When
        ResponseEntity<List<UserRegisterResponseDTO>> response = userController.getAllUsers();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyList, response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(userService, times(1)).findAll();
    }

    @Test
    void getUser_ShouldReturnUser_WhenUserExists() {
        // Given
        Long userId = 1L;
        UserRegisterResponseDTO expectedUser = createValidUser();
        when(userService.findUserById(userId)).thenReturn(expectedUser);

        // When
        ResponseEntity<UserRegisterResponseDTO> response = userController.getUser(userId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
        verify(userService, times(1)).findUserById(userId);
    }

    @Test
    void getUser_ShouldThrowException_WhenUserNotFound() {
        // Given
        Long userId = 999L;
        when(userService.findUserById(userId))
                .thenThrow(new RuntimeException("User not found"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            userController.getUser(userId);
        });
        verify(userService, times(1)).findUserById(userId);
    }

    @Test
    void buyPet_ShouldReturnSuccessResponse_WhenValidPurchase() {
        // Given
        Long userId = 1L;
        Long petId = 2L;
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "Buy pet successful!");
        expectedResponse.put("petId", petId);
        expectedResponse.put("customer", "john_doe");

        when(userService.buyPet(userId, petId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<Map<String, Object>> response = userController.buyPet(userId, petId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Buy pet successful!", response.getBody().get("message"));
        assertEquals(petId, response.getBody().get("petId"));
        assertEquals("john_doe", response.getBody().get("customer"));
        verify(userService, times(1)).buyPet(userId, petId);
    }

    @Test
    void buyPet_ShouldHandleServiceException() {
        // Given
        Long userId = 1L;
        Long petId = 2L;
        when(userService.buyPet(userId, petId))
                .thenThrow(new RuntimeException("Pet not found"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            userController.buyPet(userId, petId);
        });
        verify(userService, times(1)).buyPet(userId, petId);
    }

    @Test
    void buyPet_ShouldReturnNullResponse_WhenServiceReturnsNull() {
        // Given
        Long userId = 1L;
        Long petId = 2L;
        when(userService.buyPet(userId, petId)).thenReturn(null);

        // When
        ResponseEntity<Map<String, Object>> response = userController.buyPet(userId, petId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).buyPet(userId, petId);
    }

    // Helper methods
    private List<UserRegisterResponseDTO> createUserList() {
        List<UserRegisterResponseDTO> users = new ArrayList<>();
        users.add(createValidUser());
        users.add(createAnotherUser());
        return users;
    }

    private UserRegisterResponseDTO createValidUser() {
        UserRegisterResponseDTO user = new UserRegisterResponseDTO();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        return user;
    }

    private UserRegisterResponseDTO createAnotherUser() {
        UserRegisterResponseDTO user = new UserRegisterResponseDTO();
        user.setId(2L);
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setUsername("jane_smith");
        user.setEmail("jane@example.com");
        return user;
    }

    private Pet createValidPet() {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Buddy");
        pet.setAge(3);
        pet.setPrice(500);
        pet.setCategory(Category.DOG);
        pet.setGender(Gender.MALE);
        return pet;
    }
}
