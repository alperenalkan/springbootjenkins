package com.tpe.PetStore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestDTO {
    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 32, message = "Username must be between {min}-{max} characters.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 32, message = "Username must be between {min}-{max} characters.")
    private String password;
}
