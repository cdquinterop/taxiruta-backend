package com.slsolution.taxiruta.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TripRequestDTO {

    @NotBlank(message = "Origin is required")
    @Size(max = 200, message = "Origin must not exceed 200 characters")
    private String origin;

    @NotBlank(message = "Destination is required")
    @Size(max = 200, message = "Destination must not exceed 200 characters")
    private String destination;

    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @NotNull(message = "Available seats is required")
    @Positive(message = "Available seats must be positive")
    private Integer availableSeats;

    @NotNull(message = "Price per seat is required")
    @Positive(message = "Price per seat must be positive")
    private BigDecimal pricePerSeat;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    // Constructors
    public TripRequestDTO() {}

    public TripRequestDTO(String origin, String destination, LocalDateTime departureTime,
                         Integer availableSeats, BigDecimal pricePerSeat, String description) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
        this.pricePerSeat = pricePerSeat;
        this.description = description;
    }

    // Getters and Setters
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public BigDecimal getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(BigDecimal pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}