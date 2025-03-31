package com.epam.gym_crm.handler;

import com.epam.gym_crm.dto.response.ExceptionResponse;
import com.epam.gym_crm.exception.InvalidUserCredentialException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static com.epam.gym_crm.handler.BusinessErrorCodes.INTERNAL_ERROR;
import static com.epam.gym_crm.handler.BusinessErrorCodes.USER_UNAUTHORIZED;
import static com.epam.gym_crm.handler.BusinessErrorCodes.VALIDATION_FAILED;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Log LOG = LogFactory.getLog(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {

        LOG.error("MethodArgumentNotValidException: ", exception);

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(VALIDATION_FAILED.getHttpStatus()).body(
                ExceptionResponse.builder()
                        .businessErrorCode(VALIDATION_FAILED.getCode())
                        .businessErrorDescription(VALIDATION_FAILED.getDescription())
                        .errorMessage("One or more fields are invalid.")
                        .validationErrors(errors)
                        .build()
        );
    }

    @ExceptionHandler(InvalidUserCredentialException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidUserCredentialException(InvalidUserCredentialException exception) {

        LOG.error("InvalidUserCredentialException: ", exception);

        return ResponseEntity.status(USER_UNAUTHORIZED.getCode()).body(
                ExceptionResponse.builder()
                        .businessErrorCode(USER_UNAUTHORIZED.getCode())
                        .businessErrorDescription(USER_UNAUTHORIZED.getDescription())
                        .errorMessage(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ExceptionResponse> handleMissingRequestHeaderException(MissingRequestHeaderException exception) {

        LOG.error("MissingRequestHeaderException: ", exception);

        return ResponseEntity
                .status(USER_UNAUTHORIZED.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(USER_UNAUTHORIZED.getCode())
                                .businessErrorDescription(USER_UNAUTHORIZED.getDescription())
                                .errorMessage(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {

        LOG.error("An exception occurred: ", exception);

        return ResponseEntity
                .status(INTERNAL_ERROR.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(INTERNAL_ERROR.getCode())
                                .businessErrorDescription(INTERNAL_ERROR.getDescription())
                                .errorMessage(exception.getMessage())
                                .build()
                );
    }
}
