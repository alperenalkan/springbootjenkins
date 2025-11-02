package com.tpe.PetStore.controller;

import com.tpe.PetStore.dto.PetDTO;
import com.tpe.PetStore.dto.UserRegisterResponseDTO;
import com.tpe.PetStore.entity.Pet;
import com.tpe.PetStore.entity.user.User;
import com.tpe.PetStore.service.PetService;
import com.tpe.PetStore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.UnresolvedAddressException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PetService petService;

    @GetMapping("/all")
    public ResponseEntity<List<UserRegisterResponseDTO>> getAllUsers(){
        List<UserRegisterResponseDTO> userList = userService.findAll();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserRegisterResponseDTO> getUser(@PathVariable("id") Long id){
        return new ResponseEntity<>(userService.findUserById(id),HttpStatus.OK);
    }

    @PostMapping("/{userId}/buyPet/{petId}")
    public ResponseEntity<Map<String,Object>> buyPet(@PathVariable Long userId, @PathVariable Long petId) {
        return ResponseEntity.ok(userService.buyPet(userId, petId));
    }


}
