package com.epam.gym_crm.controller;

import com.epam.gym_crm.dto.request.ChangePasswordRequestDTO;
import com.epam.gym_crm.dto.request.LogInRequestDTO;
import com.epam.gym_crm.dto.response.UserResponseDTO;
import com.epam.gym_crm.exception.InvalidPasswordException;
import com.epam.gym_crm.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.epam.gym_crm.handler.BusinessErrorCodes.VALIDATION_FAILED;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void logIn_WithValidCredentials_ShouldReturnUser() throws Exception {
        // Given
        LogInRequestDTO request = LogInRequestDTO.builder()
                .username("test.user")
                .password("password123")
                .build();

        UserResponseDTO response = UserResponseDTO.builder()
                .username("test.user")
                .isActive(true)
                .build();

        // When
        when(userService.login(anyString(), anyString())).thenReturn(response);

        // Then - Changed from POST to GET
        mockMvc.perform(get("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test.user"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void changePassword_WithValidRequest_ShouldReturnUpdatedUser() throws Exception {
        // Given
        ChangePasswordRequestDTO request = ChangePasswordRequestDTO.builder()
                .username("test.user")
                .oldPassword("oldPassword123")
                .newPassword("newPassword456")
                .build();

        UserResponseDTO response = UserResponseDTO.builder()
                .username("test.user")
                .isActive(true)
                .build();

        // When
        when(userService.changePassword(anyString(), anyString(), anyString())).thenReturn(response);

        // Then
        mockMvc.perform(put("/api/v1/users/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.username").value("test.user"))
                .andExpect(jsonPath("$.isActive").value(true));
    }
}