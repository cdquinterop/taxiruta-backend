package com.slsolution.taxiruta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.slsolution.taxiruta.model.Booking;
import com.slsolution.taxiruta.model.Trip;
import com.slsolution.taxiruta.model.User;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByTripId(Long tripId);
    
    List<Booking> findByPassengerId(Long passengerId);
    
    List<Booking> findByStatus(Booking.Status status);
    
    @Query("SELECT b FROM Booking b WHERE b.trip.id = :tripId AND b.passenger.id = :passengerId")
    Optional<Booking> findByTripIdAndPassengerId(@Param("tripId") Long tripId, 
                                               @Param("passengerId") Long passengerId);
    
    @Query("SELECT b FROM Booking b WHERE b.trip = :trip AND b.status = :status")
    List<Booking> findByTripAndStatus(@Param("trip") Trip trip, @Param("status") Booking.Status status);
    
    @Query("SELECT b FROM Booking b WHERE b.passenger = :passenger AND b.status IN :statuses")
    List<Booking> findByPassengerAndStatusIn(@Param("passenger") User passenger, 
                                           @Param("statuses") List<Booking.Status> statuses);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.trip = :trip AND b.status = 'CONFIRMED'")
    Integer countConfirmedBookingsByTrip(@Param("trip") Trip trip);
    
    @Query("SELECT b FROM Booking b WHERE b.trip.driver = :driver AND b.status = 'PENDING'")
    List<Booking> findPendingBookingsForDriver(@Param("driver") User driver);
}