package com.tpe.PetStore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tpe.PetStore.entity.Pet;
import com.tpe.PetStore.entity.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterResponseDTO {

        private Long id;
        private String username;
        private String email;

        private String firstName;

        private String lastName;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private UserRole userRole;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private List<Pet> storePets;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private List<Pet> customerPets;
    }

