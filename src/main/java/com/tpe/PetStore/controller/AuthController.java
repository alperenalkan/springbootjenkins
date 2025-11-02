package com.tpe.PetStore.controller;

import com.tpe.PetStore.dto.UserLoginRequestDTO;
import com.tpe.PetStore.dto.UserRegisterRequestDTO;
import com.tpe.PetStore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginRequestDTO dto){
        return ResponseEntity.ok(userService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody UserRegisterRequestDTO dto){
        return new ResponseEntity<>(userService.saveUser(dto), HttpStatus.CREATED);
    }
}
