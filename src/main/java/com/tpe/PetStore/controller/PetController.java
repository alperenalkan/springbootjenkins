package com.tpe.PetStore.controller;

import com.tpe.PetStore.dto.PetDTO;
import com.tpe.PetStore.entity.Pet;
import com.tpe.PetStore.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @PostMapping("/create")
    public ResponseEntity<Map<String,Object>> createPet(@RequestBody PetDTO dto) {
        return new ResponseEntity<>(petService.savePet(dto), HttpStatus.CREATED);
    }

    @GetMapping("/allPets")
    public ResponseEntity<List<PetDTO>> getAllPets() {
        return ResponseEntity.ok(petService.findAll());
    }



}
