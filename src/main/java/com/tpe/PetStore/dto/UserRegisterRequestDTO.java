package com.tpe.PetStore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequestDTO {

    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 32, message = "Username must be between {min}-{max} characters.")
    private String username;

    @NotBlank(message = "Email is required.")
    @Size(min = 3, max = 64, message = "Email must be between {min}-{max} characters.")
    @Email
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 32, message = "Password must be between {min}-{max} characters.")
    private String  password;

    @NotBlank(message = "First name is required.")
    @Size(min = 3, max = 32, message = "First name must be between {min}-{max} characters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(min = 3, max = 32, message = "Last name must be between {min}-{max} characters.")
    private String lastName;

    @NotBlank(message = "Role is required.")
    @Pattern(regexp = "STORE_OWNER|CUSTOMER")
    private String role;
}
