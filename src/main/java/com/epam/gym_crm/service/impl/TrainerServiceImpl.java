package com.epam.gym_crm.service.impl;

import com.epam.gym_crm.dto.request.CreateTrainerProfileRequestDTO;
import com.epam.gym_crm.dto.request.UpdateTrainerProfileRequestDTO;
import com.epam.gym_crm.dto.response.TrainerProfileResponseDTO;
import com.epam.gym_crm.dto.response.TrainerResponseDTO;
import com.epam.gym_crm.dto.response.TrainerSecureResponseDTO;
import com.epam.gym_crm.entity.Trainer;
import com.epam.gym_crm.entity.User;
import com.epam.gym_crm.mapper.TrainerMapper;
import com.epam.gym_crm.repository.TrainerRepository;
import com.epam.gym_crm.service.TrainerService;
import com.epam.gym_crm.service.TrainingTypeService;
import com.epam.gym_crm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private static final Logger LOG = LogManager.getLogger(TrainerServiceImpl.class);

    private final TrainingTypeService trainingTypeService;
    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final UserService userService;

    @Transactional
    @Override
    public TrainerResponseDTO createTrainerProfile(CreateTrainerProfileRequestDTO request) {
        LOG.info("Creating new trainer profile for: {} {}", request.getFirstName(), request.getLastName());

        validateRequest(request);

        User user = createUser(request.getFirstName(), request.getLastName());

        Trainer trainer = Trainer.builder()
                .specialization(trainingTypeService.findByValue(request.getTrainingType())
                        .orElseThrow(() -> new RuntimeException("Training type not found: " + request.getTrainingType())))
                .user(user)
                .build();

        Trainer savedTrainer = trainerRepository.save(trainer);

        LOG.info("Trainer profile created successfully: {}", savedTrainer.toString());
        return getTrainerResponseDTO(trainer);
    }

    @Override
    public TrainerResponseDTO getTrainerById(Long id) {

        LOG.info("Fetching trainer by ID: {}", id);

        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found with ID: " + id));

        return getTrainerResponseDTO(trainer);
    }

    @Transactional
    @Override
    public TrainerProfileResponseDTO getTrainerByUsername(String username) {

        User userByUsername = userService.getUserByUsername(username);

        Trainer trainer = trainerRepository.findByUserId(userByUsername.getId())
                .orElseThrow(() -> new RuntimeException("Trainer not found with username: " + userByUsername.getUsername()));

        return trainerMapper.toTrainerProfileResponseDTO(trainer);
    }

    @Override
    public Trainer getTrainerEntityByUsername(String username) {
        User userByUsername = userService.getUserByUsername(username);

        return trainerRepository.findByUserId(userByUsername.getId())
                .orElseThrow(() -> new RuntimeException("Trainer not found with username: " + userByUsername.getUsername()));
    }

    @Transactional
    @Override
    public TrainerProfileResponseDTO updateTrainerProfile(UpdateTrainerProfileRequestDTO request) {

        Trainer trainer = trainerRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Trainer not found with username: " + request.getUsername()));

        trainer.getUser().setFirstName(request.getFirstName().trim());
        trainer.getUser().setLastName(request.getLastName().trim());

        trainer.setSpecialization(
                trainingTypeService.findByValue(
                                request.getTrainingTypeName())
                        .orElseThrow(() -> new RuntimeException("Training type not found: " + request.getTrainingTypeName())
                        ));

        if (request.getIsActive() != trainer.getUser().getIsActive()) {
            updateStatus(trainer.getUser().getUsername());
        }

        return trainerMapper.toTrainerProfileResponseDTO(trainer);
    }

    @Override
    public void updateStatus(String username) {
        userService.updateStatus(username);
    }

    @Override
    public List<TrainerSecureResponseDTO> getNotAssignedTrainersByTraineeUsername(String traineeUsername) {
        LOG.info("Fetching unassigned trainers for trainee: {}", traineeUsername);

        // Validate input
        if (!StringUtils.hasText(traineeUsername)) {
            LOG.warn("Trainee username is null or empty.");
            throw new IllegalArgumentException("Trainee username must not be empty");
        }

        try {

            List<TrainerSecureResponseDTO> unassignedTrainers = trainerRepository.findUnassignedTrainersByTraineeUsername(traineeUsername)
                    .stream()
                    .map(trainerMapper::toTrainerSecureResponseDTO)
                    .toList();

            LOG.info("Found {} unassigned trainers for trainee: {}", unassignedTrainers.size(), traineeUsername);

            return unassignedTrainers;
        } catch (Exception e) {
            LOG.error("Error while fetching unassigned trainers for trainee: {}", traineeUsername, e);
            throw new RuntimeException("Failed to retrieve unassigned trainers", e);
        }
    }

    private void validateRequest(CreateTrainerProfileRequestDTO request) {
        if (!StringUtils.hasText(request.getFirstName()) || !StringUtils.hasText(request.getLastName())) {
            throw new IllegalArgumentException("First name and last name cannot be empty");
        }
        if (!StringUtils.hasText(request.getTrainingType())) {
            throw new IllegalArgumentException("Training type cannot be empty");
        }
    }

    @Override
    public TrainerResponseDTO getTrainerResponseDTO(Trainer trainer) {
        return trainerMapper.toTrainerResponseDTO(trainer);
    }

    @Override
    public UserService getUserService() {
        return userService;
    }
}
