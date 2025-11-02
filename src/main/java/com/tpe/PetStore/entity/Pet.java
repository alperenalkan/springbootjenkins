package com.tpe.PetStore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tpe.PetStore.entity.user.User;
import com.tpe.PetStore.enums.Category;
import com.tpe.PetStore.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "t_pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    private Integer price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "store_owner_id", nullable = false)
    private User storeOwner;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "customer_id")
    private User customer;

}
