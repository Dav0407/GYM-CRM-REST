package com.epam.gym_crm.mapper;

import com.epam.gym_crm.dto.response.TraineeResponseDTO;
import com.epam.gym_crm.entity.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TraineeMapper {

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.password", target = "password")
    @Mapping(source = "user.isActive", target = "isActive")
    @Mapping(source = "dateOfBirth", target = "birthDate")
    TraineeResponseDTO toTraineeResponseDTO(Trainee trainee);
}