package com.slsolution.taxiruta.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TripResponseDTO {

    private Long id;
    private UserResponseDTO driver;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private Integer availableSeats;
    private Integer remainingSeats;
    private BigDecimal pricePerSeat;
    private String description;
    private String status;
    private LocalDateTime createdAt;

    // Constructors
    public TripResponseDTO() {}

    public TripResponseDTO(Long id, UserResponseDTO driver, String origin, String destination,
                          LocalDateTime departureTime, Integer availableSeats, Integer remainingSeats,
                          BigDecimal pricePerSeat, String description, String status, LocalDateTime createdAt) {
        this.id = id;
        this.driver = driver;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
        this.remainingSeats = remainingSeats;
        this.pricePerSeat = pricePerSeat;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponseDTO getDriver() {
        return driver;
    }

    public void setDriver(UserResponseDTO driver) {
        this.driver = driver;
    }

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

    public Integer getRemainingSeats() {
        return remainingSeats;
    }

    public void setRemainingSeats(Integer remainingSeats) {
        this.remainingSeats = remainingSeats;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}