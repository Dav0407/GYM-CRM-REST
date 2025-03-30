package com.epam.gym_crm.mapper;

import com.epam.gym_crm.dto.response.UserResponseDTO;
import com.epam.gym_crm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toUserResponseDTO(User user);
}
