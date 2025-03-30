package com.epam.gym_crm.service.impl;

import com.epam.gym_crm.dto.response.TrainerSecureResponseDTO;
import com.epam.gym_crm.entity.Trainee;
import com.epam.gym_crm.entity.TraineeTrainer;
import com.epam.gym_crm.entity.Trainer;
import com.epam.gym_crm.mapper.TrainerMapper;
import com.epam.gym_crm.repository.TraineeTrainerRepository;
import com.epam.gym_crm.service.TraineeService;
import com.epam.gym_crm.service.TraineeTrainerService;
import com.epam.gym_crm.service.TrainerService;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TraineeTrainerServiceImpl implements TraineeTrainerService {

    private static final Log LOG = LogFactory.getLog(TraineeTrainerServiceImpl.class);

    private final TraineeTrainerRepository traineeTrainerRepository;
    private final TrainerMapper trainerMapper;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Override
    @Transactional
    public TraineeTrainer createTraineeTrainer(String traineeUsername, String trainerUsername) {
        LOG.info("Attempting to create a trainee-trainer relationship: Trainee = " + traineeUsername + ", Trainer = " + trainerUsername);

        // Validate input
        if (traineeUsername == null || traineeUsername.trim().isEmpty()) {
            LOG.error("Trainee username is null or empty.");
            throw new IllegalArgumentException("Trainee username cannot be empty.");
        }
        if (trainerUsername == null || trainerUsername.trim().isEmpty()) {
            LOG.error("Trainer username is null or empty.");
            throw new IllegalArgumentException("Trainer username cannot be empty.");
        }

        // Retrieve trainee and trainer
        Trainee trainee = traineeService.getTraineeEntityByUsername(traineeUsername);
        Trainer trainer = trainerService.getTrainerEntityByUsername(trainerUsername);

        // Check if relationship already exists
        Optional<TraineeTrainer> existingRelation = traineeTrainerRepository.findByTraineeAndTrainer(trainee, trainer);
        if (existingRelation.isPresent()) {
            LOG.warn("Trainee-Trainer relationship already exists between " + traineeUsername + " and " + trainerUsername);
            return existingRelation.get();
        }

        // Create and save new relationship
        TraineeTrainer traineeTrainer = TraineeTrainer.builder()
                .trainee(trainee)
                .trainer(trainer)
                .build();

        TraineeTrainer savedTraineeTrainer = traineeTrainerRepository.save(traineeTrainer);
        LOG.info("Successfully created Trainee-Trainer relationship with ID: " + savedTraineeTrainer.getId());

        return savedTraineeTrainer;
    }

    @Override
    public List<TraineeTrainer> findByTraineeUsername(String traineeUsername) {
        LOG.info("Fetching trainee-trainer relationships for trainee: " + traineeUsername);
        return traineeTrainerRepository.findAllByTraineeUsername(traineeUsername);
    }

    @Override
    @Transactional
    public List<TrainerSecureResponseDTO> updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames) {
        // Validate inputs
        if (traineeUsername == null || traineeUsername.isBlank()) {
            throw new IllegalArgumentException("Trainee username cannot be null or empty.");
        }

        if (trainerUsernames == null) {
            throw new IllegalArgumentException("Trainer usernames list cannot be null.");
        }

        LOG.info("Updating trainers list for trainee: {}" + traineeUsername);

        // Fetch the trainee
        Trainee trainee = traineeService.getTraineeEntityByUsername(traineeUsername);
        if (trainee == null) {
            throw new IllegalArgumentException("Trainee not found: " + traineeUsername);
        }

        // Remove existing trainer relationships
        List<TraineeTrainer> existingRelations = traineeTrainerRepository.findAllByTraineeUsername(traineeUsername);
        traineeTrainerRepository.deleteAll(existingRelations);
        LOG.info("Removed " + existingRelations.size() + " existing trainer relations for trainee " + traineeUsername);
        // Add new trainer relationships
        List<TraineeTrainer> newRelations = trainerUsernames.stream()
                .map(trainerUsername -> {
                    Trainer trainer = trainerService.getTrainerEntityByUsername(trainerUsername);
                    if (trainer == null) {
                        LOG.warn("Trainer not found: {}" + trainerUsername);
                        return null;
                    }
                    return TraineeTrainer.builder()
                            .trainer(trainer)
                            .trainee(trainee)
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();

        traineeTrainerRepository.saveAll(newRelations);
        LOG.info("Added " + newRelations.size() + " new trainers for trainee " + traineeUsername);

        return newRelations.stream()
                .filter(traineeTrainer -> traineeTrainer.getTrainee().equals(trainee))
                .map(TraineeTrainer::getTrainer)
                .map(trainerMapper::toTrainerSecureResponseDTO)
                .toList();
    }

}
