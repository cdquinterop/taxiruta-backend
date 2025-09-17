package com.slsolution.taxiruta.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequestDTO {

    @NotNull(message = "Seats requested is required")
    @Positive(message = "Seats requested must be positive")
    private Integer seatsRequested;

    // Constructors
    public BookingRequestDTO() {}

    public BookingRequestDTO(Integer seatsRequested) {
        this.seatsRequested = seatsRequested;
    }

    // Getters and Setters
    public Integer getSeatsRequested() {
        return seatsRequested;
    }

    public void setSeatsRequested(Integer seatsRequested) {
        this.seatsRequested = seatsRequested;
    }
}