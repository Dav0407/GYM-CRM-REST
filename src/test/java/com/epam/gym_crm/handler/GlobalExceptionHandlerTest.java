package com.epam.gym_crm.handler;

import com.epam.gym_crm.dto.response.ExceptionResponse;
import com.epam.gym_crm.exception.InvalidPasswordException;
import com.epam.gym_crm.exception.InvalidUserCredentialException;
import com.epam.gym_crm.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.epam.gym_crm.handler.BusinessErrorCodes.INTERNAL_ERROR;
import static com.epam.gym_crm.handler.BusinessErrorCodes.USER_UNAUTHORIZED;
import static com.epam.gym_crm.handler.BusinessErrorCodes.VALIDATION_FAILED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleValidationExceptions_ShouldReturnValidationErrorResponse() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "error message");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        // When
        ResponseEntity<ExceptionResponse> response = globalExceptionHandler.handleValidationExceptions(exception);

        // Then
        assertEquals(VALIDATION_FAILED.getHttpStatus(), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(VALIDATION_FAILED.getCode(), response.getBody().getBusinessErrorCode());
        assertEquals(VALIDATION_FAILED.getDescription(), response.getBody().getBusinessErrorDescription());
        assertEquals("One or more fields are invalid.", response.getBody().getErrorMessage());

        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("fieldName", "error message");
        assertEquals(expectedErrors, response.getBody().getValidationErrors());
    }

    @Test
    void handleInvalidUserCredentialException_ShouldReturnUnauthorizedResponse() {
        // Given
        InvalidUserCredentialException exception = new InvalidUserCredentialException("Invalid credentials");

        // When
        ResponseEntity<ExceptionResponse> response = globalExceptionHandler.handleInvalidUserCredentialException(exception);

        // Then
        assertEquals(HttpStatus.valueOf(USER_UNAUTHORIZED.getCode()), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(USER_UNAUTHORIZED.getCode(), response.getBody().getBusinessErrorCode());
        assertEquals(USER_UNAUTHORIZED.getDescription(), response.getBody().getBusinessErrorDescription());
        assertEquals("Invalid credentials", response.getBody().getErrorMessage());
    }

    @Test
    void handleInvalidPasswordException_ShouldReturnValidationFailedResponse() {
        // Given
        InvalidPasswordException exception = new InvalidPasswordException("Password is invalid");

        // When
        ResponseEntity<ExceptionResponse> response = globalExceptionHandler.handleInvalidPasswordException(exception);

        // Then
        assertEquals(VALIDATION_FAILED.getHttpStatus(), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(VALIDATION_FAILED.getCode(), response.getBody().getBusinessErrorCode());
        assertEquals(VALIDATION_FAILED.getDescription(), response.getBody().getBusinessErrorDescription());
        assertEquals("Password is invalid", response.getBody().getErrorMessage());
    }

    @Test
    void handleUserNotFoundException_ShouldReturnUnauthorizedResponse() {
        // Given
        UserNotFoundException exception = new UserNotFoundException("User not found");

        // When
        ResponseEntity<ExceptionResponse> response = globalExceptionHandler.handleUserNotFoundException(exception);

        // Then
        assertEquals(USER_UNAUTHORIZED.getHttpStatus(), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(USER_UNAUTHORIZED.getCode(), response.getBody().getBusinessErrorCode());
        assertEquals(USER_UNAUTHORIZED.getDescription(), response.getBody().getBusinessErrorDescription());
        assertEquals("User not found", response.getBody().getErrorMessage());
    }

    @Test
    void handleMissingRequestHeaderException_ShouldReturnUnauthorizedResponse() {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.getNestedParameterType()).thenReturn((Class)Object.class);
        MissingRequestHeaderException exception = new MissingRequestHeaderException("headerName", parameter);

        // When
        ResponseEntity<ExceptionResponse> response = globalExceptionHandler.handleMissingRequestHeaderException(exception);

        // Then
        assertEquals(USER_UNAUTHORIZED.getHttpStatus(), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(USER_UNAUTHORIZED.getCode(), response.getBody().getBusinessErrorCode());
        assertEquals(USER_UNAUTHORIZED.getDescription(), response.getBody().getBusinessErrorDescription());
        assertTrue(response.getBody().getErrorMessage().contains("headerName"));
    }

    @Test
    void handleException_ShouldReturnInternalServerErrorResponse() {
        // Given
        Exception exception = new Exception("Something went wrong");

        // When
        ResponseEntity<ExceptionResponse> response = globalExceptionHandler.handleException(exception);

        // Then
        assertEquals(INTERNAL_ERROR.getHttpStatus(), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(INTERNAL_ERROR.getCode(), response.getBody().getBusinessErrorCode());
        assertEquals(INTERNAL_ERROR.getDescription(), response.getBody().getBusinessErrorDescription());
        assertEquals("Something went wrong", response.getBody().getErrorMessage());
    }
}