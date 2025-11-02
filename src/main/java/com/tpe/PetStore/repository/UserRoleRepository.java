package com.tpe.PetStore.repository;

import com.tpe.PetStore.entity.user.UserRole;
import com.tpe.PetStore.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query("SELECT u FROM UserRole u WHERE u.role = ?1")
    Optional<UserRole> findByRoleName(Role role);
}
