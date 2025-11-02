package com.tpe.PetStore.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tpe.PetStore.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "t_user")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 32, unique = true, nullable = false)
    private String username;

    @Column(length = 96, unique = true, nullable = false)
    private String email;

    @Column(length = 128, nullable = false)
    private String  password;

    @Column(length = 32, nullable = false)
    private String firstName;

    @Column(length = 32, nullable = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private UserRole userRole;

    @OneToMany(mappedBy = "storeOwner", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pet> storePets;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pet> customerPets;
}

