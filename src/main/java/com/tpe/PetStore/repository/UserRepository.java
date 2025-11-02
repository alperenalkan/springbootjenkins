package com.tpe.PetStore.repository;

import com.tpe.PetStore.entity.user.User;
import com.tpe.PetStore.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); //derived
    User findByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByUsernameOrEmail(String username, String email);

    @Query("SELECT count(u) FROM User u WHERE u.userRole.role = ?1")
    int getUserCountByRole(Role role);

    boolean existsByFirstName(String firstName);
}
