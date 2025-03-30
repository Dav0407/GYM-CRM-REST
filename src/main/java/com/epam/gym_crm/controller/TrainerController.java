package com.epam.gym_crm.controller;

import com.epam.gym_crm.dto.request.CreateTrainerProfileRequestDTO;
import com.epam.gym_crm.dto.request.UpdateTrainerListRequestDTO;
import com.epam.gym_crm.dto.request.UpdateTrainerProfileRequestDTO;
import com.epam.gym_crm.dto.response.TrainerProfileResponseDTO;
import com.epam.gym_crm.dto.response.TrainerResponseDTO;
import com.epam.gym_crm.dto.response.TrainerSecureResponseDTO;
import com.epam.gym_crm.service.TraineeTrainerService;
import com.epam.gym_crm.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final TraineeTrainerService traineeTrainerService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrainerResponseDTO> registerTrainer(@RequestBody CreateTrainerProfileRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(trainerService.createTrainerProfile(request));
    }

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrainerProfileResponseDTO> getTrainerProfile(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.FOUND).body(trainerService.getTrainerByUsername(username));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrainerProfileResponseDTO> updateTrainerProfile(@RequestBody UpdateTrainerProfileRequestDTO request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(trainerService.updateTrainerProfile(request));
    }

    @GetMapping(value = "/not-assigned/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TrainerSecureResponseDTO>> getNotAssignedTrainers(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.FOUND).body(trainerService.getNotAssignedTrainersByTraineeUsername(username));
    }

    @PutMapping(value = "/assign", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TrainerSecureResponseDTO>> updateTraineesTrainersList(@RequestBody UpdateTrainerListRequestDTO request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(traineeTrainerService.updateTraineeTrainers(request.getTraineeUsername(), request.getTrainerUsernames()));
    }


}
