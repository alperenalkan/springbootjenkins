package com.tpe.PetStore.repository;

import com.tpe.PetStore.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public interface PetRepository extends JpaRepository<Pet,Long> {
    boolean existsByName(String name);

    Pet findByName(String name);
}
