package com.slsolution.taxiruta.service;

import com.slsolution.taxiruta.dto.request.TripRequestDTO;
import com.slsolution.taxiruta.dto.response.TripResponseDTO;
import com.slsolution.taxiruta.dto.response.UserResponseDTO;
import com.slsolution.taxiruta.exception.BadRequestException;
import com.slsolution.taxiruta.exception.ResourceNotFoundException;
import com.slsolution.taxiruta.exception.UnauthorizedException;
import com.slsolution.taxiruta.model.Trip;
import com.slsolution.taxiruta.model.User;
import com.slsolution.taxiruta.repository.TripRepository;
import com.slsolution.taxiruta.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserService userService;

    public TripResponseDTO createTrip(TripRequestDTO request, User driver) {
        // Validate that user is a driver
        if (driver.getRole() != User.Role.DRIVER) {
            throw new UnauthorizedException("Only drivers can create trips");
        }

        // Validate departure time is in the future
        if (request.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Departure time must be in the future");
        }

        Trip trip = new Trip();
        trip.setDriver(driver);
        trip.setOrigin(request.getOrigin());
        trip.setDestination(request.getDestination());
        trip.setDepartureTime(request.getDepartureTime());
        trip.setAvailableSeats(request.getAvailableSeats());
        trip.setPricePerSeat(request.getPricePerSeat());
        trip.setDescription(request.getDescription());
        trip.setStatus(Trip.Status.ACTIVE);

        Trip savedTrip = tripRepository.save(trip);
        return convertToResponseDTO(savedTrip);
    }

    public TripResponseDTO updateTrip(Long tripId, TripRequestDTO request, User user) {
        Trip trip = getTripById(tripId);

        // Only the driver can update the trip
        if (!trip.getDriver().getId().equals(user.getId())) {
            throw new UnauthorizedException("Only the trip driver can update this trip");
        }

        // Cannot update if trip is not active
        if (trip.getStatus() != Trip.Status.ACTIVE) {
            throw new BadRequestException(Constants.ERROR_TRIP_NOT_ACTIVE);
        }

        // Validate departure time is in the future
        if (request.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Departure time must be in the future");
        }

        trip.setOrigin(request.getOrigin());
        trip.setDestination(request.getDestination());
        trip.setDepartureTime(request.getDepartureTime());
        trip.setAvailableSeats(request.getAvailableSeats());
        trip.setPricePerSeat(request.getPricePerSeat());
        trip.setDescription(request.getDescription());

        Trip updatedTrip = tripRepository.save(trip);
        return convertToResponseDTO(updatedTrip);
    }

    public void cancelTrip(Long tripId, User user) {
        Trip trip = getTripById(tripId);

        // Only the driver can cancel the trip
        if (!trip.getDriver().getId().equals(user.getId())) {
            throw new UnauthorizedException("Only the trip driver can cancel this trip");
        }

        // Cannot cancel if trip is not active
        if (trip.getStatus() != Trip.Status.ACTIVE) {
            throw new BadRequestException("Trip is already cancelled or completed");
        }

        trip.setStatus(Trip.Status.CANCELLED);
        tripRepository.save(trip);
    }

    public Trip getTripById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.ERROR_TRIP_NOT_FOUND));
    }

    public TripResponseDTO getTripResponseById(Long id) {
        Trip trip = getTripById(id);
        return convertToResponseDTO(trip);
    }

    public List<TripResponseDTO> getAllTrips() {
        List<Trip> trips = tripRepository.findAll();
        return trips.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TripResponseDTO> getActiveTrips() {
        List<Trip> trips = tripRepository.findActiveTrips(LocalDateTime.now());
        return trips.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TripResponseDTO> searchTrips(String origin, String destination, LocalDateTime departureDate) {
        List<Trip> trips = tripRepository.findTripsWithFilters(
                LocalDateTime.now(), origin, destination, departureDate);
        return trips.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TripResponseDTO> getDriverTrips(Long driverId) {
        List<Trip> trips = tripRepository.findByDriverId(driverId);
        return trips.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TripResponseDTO> getActiveTripsForDriver(User driver) {
        List<Trip> trips = tripRepository.findActiveTripsForDriver(driver, LocalDateTime.now());
        return trips.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public TripResponseDTO convertToResponseDTO(Trip trip) {
        UserResponseDTO driverResponse = userService.convertToResponseDTO(trip.getDriver());
        
        return new TripResponseDTO(
                trip.getId(),
                driverResponse,
                trip.getOrigin(),
                trip.getDestination(),
                trip.getDepartureTime(),
                trip.getAvailableSeats(),
                trip.getRemainingSeats(),
                trip.getPricePerSeat(),
                trip.getDescription(),
                trip.getStatus().name(),
                trip.getCreatedAt()
        );
    }
}