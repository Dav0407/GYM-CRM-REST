package com.epam.gym_crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.epam.gym_crm.entity.Trainer;
import com.epam.gym_crm.dto.response.TrainerResponseDTO;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.password", target = "password")
    @Mapping(source = "user.isActive", target = "isActive")
    @Mapping(source = "specialization.trainingTypeName", target = "specialization")
    TrainerResponseDTO toTrainerResponseDTO(Trainer trainer);
}
