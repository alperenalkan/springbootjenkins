package com.tpe.PetStore.service;

import com.tpe.PetStore.entity.user.UserRole;
import com.tpe.PetStore.enums.Role;
import com.tpe.PetStore.exception.ResourceNotFoundException;
import com.tpe.PetStore.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRole findByRoleName(String role){
        return userRoleRepository.findByRoleName(Role.valueOf(role)).orElseThrow(
                () -> new ResourceNotFoundException("No matching role found: " + role)
        );
    }

    public List<UserRole> findAll() {
        return userRoleRepository.findAll();
    }

    public void saveRole(UserRole role) {
        userRoleRepository.save(role);
    }

    public void saveAllRoles(Collection<UserRole> roles){
        userRoleRepository.saveAllAndFlush(roles);
    }
}
