package com.epam.gym_crm.service.impl;

import com.epam.gym_crm.dto.request.CreateTrainerProfileRequestDTO;
import com.epam.gym_crm.dto.request.UpdateTrainerProfileRequestDTO;
import com.epam.gym_crm.dto.response.TrainerResponseDTO;
import com.epam.gym_crm.entity.Trainer;
import com.epam.gym_crm.entity.User;
import com.epam.gym_crm.repository.TrainerRepository;
import com.epam.gym_crm.service.TrainerService;
import com.epam.gym_crm.service.TrainingTypeService;
import com.epam.gym_crm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private static final Log LOG = LogFactory.getLog(TrainerServiceImpl.class);

    private final TrainerRepository trainerRepository;
    private final TrainingTypeService trainingTypeService;
    private final UserService userService;

    @Transactional
    @Override
    public TrainerResponseDTO createTrainerProfile(CreateTrainerProfileRequestDTO request) {
        LOG.info("Creating new trainer profile for: " + request.getFirstName() + " " + request.getLastName());

        validateRequest(request);

        User user = createUser(request.getFirstName(), request.getLastName());

        Trainer trainer = Trainer.builder()
                .specialization(trainingTypeService.findByValue(request.getTrainingType())
                        .orElseThrow(() -> new RuntimeException("Training type not found: " + request.getTrainingType())))
                .user(user)
                .build();

        Trainer savedTrainer = trainerRepository.save(trainer);

        LOG.info("Trainer profile created successfully: " + savedTrainer.toString());
        return getTrainerResponseDTO(trainer);
    }

    @Override
    public TrainerResponseDTO getTrainerById(Long id) {

        LOG.info("Fetching trainer by ID: " + id);

        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found with ID: " + id));

        return getTrainerResponseDTO(trainer);
    }

    @Override
    public TrainerResponseDTO getTrainerByUsername(String username) {

        User userByUsername = userService.getUserByUsername(username);

        Trainer trainer = trainerRepository.findByUserId(userByUsername.getId())
                .orElseThrow(() -> new RuntimeException("Trainer not found with username: " + userByUsername.getUsername()));

        return getTrainerResponseDTO(trainer);
    }

    @Override
    public Trainer getTrainerEntityByUsername(String username) {
        User userByUsername = userService.getUserByUsername(username);

        return trainerRepository.findByUserId(userByUsername.getId())
                .orElseThrow(() -> new RuntimeException("Trainer not found with username: " + userByUsername.getUsername()));
    }

    @Transactional
    @Override
    public TrainerResponseDTO updateTrainerProfile(Long id, UpdateTrainerProfileRequestDTO request) {

        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found with ID: " + id));

        trainer.getUser().setFirstName(request.getFirstName().trim());
        trainer.getUser().setLastName(request.getLastName().trim());

        trainer.setSpecialization(
                trainingTypeService.findByValue(
                                request.getTrainingTypeName())
                        .orElseThrow(() -> new RuntimeException("Training type not found: " + request.getTrainingTypeName())
                        ));

        return getTrainerResponseDTO(trainer);
    }

    @Override
    public void updateStatus(String username) {
        userService.updateStatus(username);
    }

    @Override
    public List<TrainerResponseDTO> getNotAssignedTrainersByTraineeUsername(String traineeUsername) {
        LOG.info("Fetching unassigned trainers for trainee: " + traineeUsername);

        // Validate input
        if (!StringUtils.hasText(traineeUsername)) {
            LOG.warn("Trainee username is null or empty.");
            throw new IllegalArgumentException("Trainee username must not be empty");
        }

        try {

            List<TrainerResponseDTO> unassignedTrainers = trainerRepository.findUnassignedTrainersByTraineeUsername(traineeUsername)
                    .stream()
                    .map(this::getTrainerResponseDTO)
                    .toList();

            LOG.info("Found " + unassignedTrainers.size() + " unassigned trainers for trainee: " + traineeUsername);

            return unassignedTrainers;
        } catch (Exception e) {
            LOG.error("Error while fetching unassigned trainers for trainee: " + traineeUsername, e);
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

    public TrainerResponseDTO getTrainerResponseDTO(Trainer trainer) {
        return TrainerResponseDTO.builder()
                .id(trainer.getId())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .username(trainer.getUser().getUsername())
                .password(trainer.getUser().getPassword())
                .isActive(trainer.getUser().getIsActive())
                .specialization(trainer.getSpecialization().getTrainingTypeName())
                .build();
    }

    @Override
    public UserService getUserService() {
        return userService;
    }
}
