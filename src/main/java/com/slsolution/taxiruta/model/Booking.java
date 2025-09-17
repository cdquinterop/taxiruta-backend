package com.slsolution.taxiruta.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * Entidad Booking que representa las reservas de asientos en viajes
 */
@Entity
@Table(name = "bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id", nullable = false)
    private User passenger;

    @NotNull
    @Positive
    private Integer seatsRequested;

    @NotNull
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.PENDING;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate;

    @Column(name = "confirmed_date")
    private LocalDateTime confirmedDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Status {
        PENDING, CONFIRMED, REJECTED, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (bookingDate == null) {
            bookingDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Lombok generates constructors automatically

    // Business methods
    public void confirm() {
        this.status = Status.CONFIRMED;
        this.confirmedDate = LocalDateTime.now();
    }

    public void reject() {
        this.status = Status.REJECTED;
    }

    public void cancel() {
        this.status = Status.CANCELLED;
    }

    public boolean isPending() {
        return status == Status.PENDING;
    }

    public boolean isConfirmed() {
        return status == Status.CONFIRMED;
    }

    // Lombok @Data annotation generates all basic getters and setters automatically
}