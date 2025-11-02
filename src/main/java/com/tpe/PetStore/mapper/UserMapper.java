package com.tpe.PetStore.mapper;



import com.tpe.PetStore.dto.UserRegisterRequestDTO;
import com.tpe.PetStore.dto.UserRegisterResponseDTO;
import com.tpe.PetStore.entity.user.User;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {

    // 1️⃣ Entity -> ResponseDTO
    UserRegisterResponseDTO toDTO(User user);

    // 2️⃣ RequestDTO -> Entity (kayıt/güncelleme için)
   User toEntity(UserRegisterRequestDTO dto);

    // 3️⃣ (opsiyonel) List dönüşümü (MapStruct kendisi otomatik destekler)
    List<UserRegisterResponseDTO> toDTOList(List<User> users);
}




