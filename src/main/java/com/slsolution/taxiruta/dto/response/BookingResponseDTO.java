package com.slsolution.taxiruta.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingResponseDTO {

    private Long id;
    private Long tripId;
    private UserResponseDTO passenger;
    private Integer seatsRequested;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime bookingDate;
    private LocalDateTime confirmedDate;

    // Constructors
    public BookingResponseDTO() {}

    public BookingResponseDTO(Long id, Long tripId, UserResponseDTO passenger, Integer seatsRequested,
                             BigDecimal totalPrice, String status, LocalDateTime bookingDate, LocalDateTime confirmedDate) {
        this.id = id;
        this.tripId = tripId;
        this.passenger = passenger;
        this.seatsRequested = seatsRequested;
        this.totalPrice = totalPrice;
        this.status = status;
        this.bookingDate = bookingDate;
        this.confirmedDate = confirmedDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public UserResponseDTO getPassenger() {
        return passenger;
    }

    public void setPassenger(UserResponseDTO passenger) {
        this.passenger = passenger;
    }

    public Integer getSeatsRequested() {
        return seatsRequested;
    }

    public void setSeatsRequested(Integer seatsRequested) {
        this.seatsRequested = seatsRequested;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDateTime getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(LocalDateTime confirmedDate) {
        this.confirmedDate = confirmedDate;
    }
}