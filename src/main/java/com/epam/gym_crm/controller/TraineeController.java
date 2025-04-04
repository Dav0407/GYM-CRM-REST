package com.epam.gym_crm.controller;

import com.epam.gym_crm.dto.request.CreateTraineeProfileRequestDTO;
import com.epam.gym_crm.dto.request.UpdateTraineeProfileRequestDTO;
import com.epam.gym_crm.dto.response.TraineeProfileResponseDTO;
import com.epam.gym_crm.dto.response.TraineeResponseDTO;
import com.epam.gym_crm.service.TraineeService;
import com.epam.gym_crm.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/trainees")
@RequiredArgsConstructor
@Tag(name = "Your Controller Name", description = "Description of your controller")
public class TraineeController {

    private final TraineeService traineeService;
    private final UserService userService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TraineeResponseDTO> registerTrainee(@Valid @RequestBody CreateTraineeProfileRequestDTO request) {
        TraineeResponseDTO response = traineeService.createTraineeProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TraineeProfileResponseDTO> getTraineeProfile(@PathVariable("username") @NotBlank(message = "Username is required") String username,
                                                                       @RequestHeader(value = "Username") String headerUsername,
                                                                       @RequestHeader(value = "Password") String headerPassword) {

        userService.validateCredentials(headerUsername, headerPassword);
        TraineeProfileResponseDTO response = traineeService.getTraineeByUsername(username);
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TraineeProfileResponseDTO> updateTraineeProfile(@Valid @RequestBody UpdateTraineeProfileRequestDTO request,
                                                                          @RequestHeader(value = "Username") String headerUsername,
                                                                          @RequestHeader(value = "Password") String headerPassword) {
        userService.validateCredentials(headerUsername, headerPassword);
        TraineeProfileResponseDTO response = traineeService.updateTraineeProfile(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TraineeProfileResponseDTO> deleteTraineeProfile(@PathVariable("username") @NotBlank(message = "Username is required") String username,
                                                                          @RequestHeader(value = "Username") String headerUsername,
                                                                          @RequestHeader(value = "Password") String headerPassword) {
        userService.validateCredentials(headerUsername, headerPassword);
        TraineeProfileResponseDTO response = traineeService.deleteTraineeProfileByUsername(username);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PatchMapping(value = "/{trainee-username}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TraineeProfileResponseDTO> switchTraineeStatus(@PathVariable("trainee-username") @NotBlank(message = "Username is required") String traineeUsername,
                                                                         @RequestHeader(value = "Username") String headerUsername,
                                                                         @RequestHeader(value = "Password") String headerPassword) {
        userService.validateCredentials(headerUsername, headerPassword);
        traineeService.updateStatus(traineeUsername);
        TraineeProfileResponseDTO response = traineeService.getTraineeByUsername(traineeUsername);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}


