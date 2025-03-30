package com.epam.gym_crm.controller;

import com.epam.gym_crm.dto.request.CreateTraineeProfileRequestDTO;
import com.epam.gym_crm.dto.request.UpdateTraineeProfileRequestDTO;
import com.epam.gym_crm.dto.response.TraineeProfileResponseDTO;
import com.epam.gym_crm.dto.response.TraineeResponseDTO;
import com.epam.gym_crm.service.TraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TraineeResponseDTO> registerTrainee(@RequestBody CreateTraineeProfileRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(traineeService.createTraineeProfile(request));
    }

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TraineeProfileResponseDTO> getTraineeProfile(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.FOUND).body(traineeService.getTraineeByUsername(username));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TraineeProfileResponseDTO> updateTraineeProfile(@RequestBody UpdateTraineeProfileRequestDTO request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(traineeService.updateTraineeProfile(request));
    }

    @DeleteMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TraineeProfileResponseDTO> deleteTraineeProfile(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(traineeService.deleteTraineeProfileByUsername(username));
    }

}


