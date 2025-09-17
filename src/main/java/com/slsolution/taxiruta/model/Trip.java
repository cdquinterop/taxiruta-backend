package com.slsolution.taxiruta.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * Entidad Trip que representa los viajes en el sistema
 */
@Entity
@Table(name = "trips")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @NotBlank
    @Size(max = 200)
    private String origin;

    @NotBlank
    @Size(max = 200)
    private String destination;

    @NotNull
    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @NotNull
    @Positive
    private Integer availableSeats;

    @NotNull
    @Positive
    private BigDecimal pricePerSeat;

    @Size(max = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Status {
        ACTIVE, CANCELLED, COMPLETED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Lombok generates constructors automatically

    // Business methods
    public int getConfirmedBookingsCount() {
        return (int) bookings.stream()
                .filter(booking -> booking.getStatus() == Booking.Status.CONFIRMED)
                .count();
    }

    public int getRemainingSeats() {
        return availableSeats - getConfirmedBookingsCount();
    }

    public boolean hasAvailableSeats() {
        return getRemainingSeats() > 0;
    }

    // Lombok @Data annotation generates all basic getters and setters automatically
}