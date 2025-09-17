package com.slsolution.taxiruta.exception;

import com.slsolution.taxiruta.dto.response.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.error(ex.getMessage(), "RESOURCE_NOT_FOUND");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.error(ex.getMessage(), "BAD_REQUEST");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleUnauthorizedException(
            UnauthorizedException ex, WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.error(ex.getMessage(), "UNAUTHORIZED");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.error("Invalid credentials", "INVALID_CREDENTIALS");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ApiResponseDTO<Object> response = new ApiResponseDTO<>(false, "Validation failed", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleGlobalException(
            Exception ex, WebRequest request) {
        
        ApiResponseDTO<Object> response = ApiResponseDTO.error(
            "An unexpected error occurred", "INTERNAL_SERVER_ERROR");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}