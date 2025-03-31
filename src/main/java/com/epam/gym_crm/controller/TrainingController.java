package com.epam.gym_crm.controller;

import com.epam.gym_crm.dto.request.AddTrainingRequestDTO;
import com.epam.gym_crm.dto.request.GetTraineeTrainingsRequestDTO;
import com.epam.gym_crm.dto.request.GetTrainerTrainingsRequestDTO;
import com.epam.gym_crm.dto.response.TraineeTrainingResponseDTO;
import com.epam.gym_crm.dto.response.TrainerTrainingResponseDTO;
import com.epam.gym_crm.dto.response.TrainingResponseDTO;
import com.epam.gym_crm.dto.response.TrainingTypeResponseDTO;
import com.epam.gym_crm.service.TrainingService;
import com.epam.gym_crm.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrainingResponseDTO> addTraining(@RequestBody AddTrainingRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingService.addTraining(request));
    }

    @GetMapping(value = "/trainees", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TraineeTrainingResponseDTO>> getTraineeTrainings(@RequestBody GetTraineeTrainingsRequestDTO request) {
        return ResponseEntity.status(HttpStatus.FOUND).body(trainingService.getTraineeTrainings(request));
    }

    @GetMapping(value = "/trainers", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TrainerTrainingResponseDTO>> getTraineeTrainings(@RequestBody GetTrainerTrainingsRequestDTO request) {
        return ResponseEntity.status(HttpStatus.FOUND).body(trainingService.getTrainerTrainings(request));
    }

    @GetMapping(value = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TrainingTypeResponseDTO>> getTrainingTypes() {
        return ResponseEntity.status(HttpStatus.FOUND).body(trainingTypeService.getAllTrainingTypes());
    }
}
