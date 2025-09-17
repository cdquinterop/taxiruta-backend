package com.slsolution.taxiruta.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO<T> {

    private boolean success;
    private String message;
    private T data;
    private String error;
    private LocalDateTime timestamp;

    // Constructors
    public ApiResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponseDTO(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponseDTO(boolean success, String message, String error) {
        this.success = success;
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }

    // Static factory methods
    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>(true, message, data);
    }

    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(true, "Operation completed successfully", data);
    }

    public static <T> ApiResponseDTO<T> error(String message, String error) {
        return new ApiResponseDTO<>(false, message, error);
    }

    public static <T> ApiResponseDTO<T> error(String message) {
        return new ApiResponseDTO<>(false, message, (String) null);
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}