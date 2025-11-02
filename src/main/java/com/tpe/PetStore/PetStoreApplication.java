package com.tpe.PetStore;

import com.tpe.PetStore.dto.UserRegisterRequestDTO;
import com.tpe.PetStore.entity.user.UserRole;
import com.tpe.PetStore.enums.Role;
import com.tpe.PetStore.service.UserRoleService;
import com.tpe.PetStore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class
PetStoreApplication implements CommandLineRunner {

    private final UserRoleService userRoleService;
    private final UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(PetStoreApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        //burada yapilacak olan seyler, uygulama baslamadan once yapilir! yani rol yoksa olustur, admin yoksa olustur
        //gibi durumlar bu mettoda yapilir!

        //ROLLERIN OLUSTURULMASI
        if (userRoleService.findAll().isEmpty()){

            //List<UserRole> userRoles = new ArrayList<>();

            UserRole admin = new UserRole();

            admin.setRole(Role.ADMIN);
            userRoleService.saveRole(admin);
            //userRoles.add(admin);

            UserRole storeOwner = new UserRole();

            storeOwner.setRole(Role.STORE_OWNER);
            userRoleService.saveRole(storeOwner);
            //userRoles.add(storeOwner);

            UserRole customer = new UserRole();

            customer.setRole(Role.CUSTOMER);
            userRoleService.saveRole(customer);
            //userRoles.add(customer);

            //userRoleService.saveAllRoles(userRoles);
        }

        //ADMIN ve StoreOwner VAR MI?
        if (userService.getAdminCount() == 0 && userService.getStoreOwnerCount()==0) {

            UserRegisterRequestDTO adminDTO = new UserRegisterRequestDTO();
            adminDTO.setFirstName("Mehmet");
            adminDTO.setLastName("Karadayuzen");
            adminDTO.setUsername("Admin");
            adminDTO.setPassword("12345678");
            adminDTO.setEmail("admin@petstore.app");
            adminDTO.setRole("ADMIN");

            userService.saveUser(adminDTO);

            UserRegisterRequestDTO storeOwnerDTO = new UserRegisterRequestDTO();
            storeOwnerDTO.setFirstName("Ali");
            storeOwnerDTO.setLastName("Gürbüz");
            storeOwnerDTO.setUsername("StoreOwner");
            storeOwnerDTO.setPassword("12345678");
            storeOwnerDTO.setEmail("storeowner@petstore.app");
            storeOwnerDTO.setRole("STORE_OWNER");

            userService.saveUser(storeOwnerDTO);


        }
    }
}
