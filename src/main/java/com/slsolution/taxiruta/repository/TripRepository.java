package com.slsolution.taxiruta.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.slsolution.taxiruta.model.Trip;
import com.slsolution.taxiruta.model.User;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    
    List<Trip> findByDriverId(Long driverId);
    
    List<Trip> findByStatus(Trip.Status status);
    
    @Query("SELECT t FROM Trip t WHERE t.status = 'ACTIVE' AND t.departureTime > :now")
    List<Trip> findActiveTrips(@Param("now") LocalDateTime now);
    
    @Query("SELECT t FROM Trip t WHERE t.status = 'ACTIVE' " +
           "AND t.departureTime > :now " +
           "AND (:origin IS NULL OR LOWER(t.origin) LIKE LOWER(CONCAT('%', :origin, '%'))) " +
           "AND (:destination IS NULL OR LOWER(t.destination) LIKE LOWER(CONCAT('%', :destination, '%'))) " +
           "AND (:departureDate IS NULL OR DATE(t.departureTime) = DATE(:departureDate)) " +
           "ORDER BY t.departureTime ASC")
    List<Trip> findTripsWithFilters(@Param("now") LocalDateTime now,
                                   @Param("origin") String origin,
                                   @Param("destination") String destination,
                                   @Param("departureDate") LocalDateTime departureDate);
    
    @Query("SELECT t FROM Trip t WHERE t.driver = :driver AND t.status = 'ACTIVE' AND t.departureTime > :now")
    List<Trip> findActiveTripsForDriver(@Param("driver") User driver, @Param("now") LocalDateTime now);
    
    @Query("SELECT t FROM Trip t WHERE t.departureTime BETWEEN :startDate AND :endDate")
    List<Trip> findTripsBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                    @Param("endDate") LocalDateTime endDate);
}