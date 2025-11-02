package com.tpe.PetStore.dto;

import com.tpe.PetStore.entity.user.User;
import com.tpe.PetStore.enums.Category;
import com.tpe.PetStore.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetDTO {
    @NotBlank(message = "Name is required.")
    @Size(min = 8, max = 32, message = "Name must be between {min}-{max} characters.")
    private String name;

    @NotNull(message = "Age is required.")
    private Integer age;

    @NotNull(message = "Price is required.")
    private Integer price;

    @NotNull(message = "Category is required.")
    private Category category;

    @NotNull(message = "Gender is required.")
    private Gender gender;

    @NotNull(message = "StoreOwner is required.")
    private User storeOwner;

    private User customer;
}
