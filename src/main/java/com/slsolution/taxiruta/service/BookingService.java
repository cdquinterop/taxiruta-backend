package com.slsolution.taxiruta.service;

import com.slsolution.taxiruta.dto.request.BookingRequestDTO;
import com.slsolution.taxiruta.dto.response.BookingResponseDTO;
import com.slsolution.taxiruta.dto.response.UserResponseDTO;
import com.slsolution.taxiruta.exception.BadRequestException;
import com.slsolution.taxiruta.exception.ResourceNotFoundException;
import com.slsolution.taxiruta.exception.UnauthorizedException;
import com.slsolution.taxiruta.model.Booking;
import com.slsolution.taxiruta.model.Trip;
import com.slsolution.taxiruta.model.User;
import com.slsolution.taxiruta.repository.BookingRepository;
import com.slsolution.taxiruta.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TripService tripService;

    @Autowired
    private UserService userService;

    public BookingResponseDTO createBooking(Long tripId, BookingRequestDTO request, User passenger) {
        Trip trip = tripService.getTripById(tripId);

        // Validate that trip is active
        if (trip.getStatus() != Trip.Status.ACTIVE) {
            throw new BadRequestException(Constants.ERROR_TRIP_NOT_ACTIVE);
        }

        // Validate that passenger is not the driver
        if (trip.getDriver().getId().equals(passenger.getId())) {
            throw new BadRequestException("Driver cannot book their own trip");
        }

        // Validate that passenger doesn't already have a booking for this trip
        Optional<Booking> existingBooking = bookingRepository.findByTripIdAndPassengerId(tripId, passenger.getId());
        if (existingBooking.isPresent()) {
            throw new BadRequestException(Constants.ERROR_BOOKING_ALREADY_EXISTS);
        }

        // Validate available seats
        if (request.getSeatsRequested() > trip.getRemainingSeats()) {
            throw new BadRequestException(Constants.ERROR_INSUFFICIENT_SEATS);
        }

        // Calculate total price
        BigDecimal totalPrice = trip.getPricePerSeat().multiply(BigDecimal.valueOf(request.getSeatsRequested()));

        Booking booking = new Booking();
        booking.setTrip(trip);
        booking.setPassenger(passenger);
        booking.setSeatsRequested(request.getSeatsRequested());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(Booking.Status.PENDING);

        Booking savedBooking = bookingRepository.save(booking);
        return convertToResponseDTO(savedBooking);
    }

    public BookingResponseDTO confirmBooking(Long bookingId, User driver) {
        Booking booking = getBookingById(bookingId);

        // Only the trip driver can confirm bookings
        if (!booking.getTrip().getDriver().getId().equals(driver.getId())) {
            throw new UnauthorizedException("Only the trip driver can confirm bookings");
        }

        // Validate booking is pending
        if (booking.getStatus() != Booking.Status.PENDING) {
            throw new BadRequestException("Booking is not pending");
        }

        // Check if there are still available seats
        if (booking.getSeatsRequested() > booking.getTrip().getRemainingSeats()) {
            throw new BadRequestException(Constants.ERROR_INSUFFICIENT_SEATS);
        }

        booking.confirm();
        Booking savedBooking = bookingRepository.save(booking);
        return convertToResponseDTO(savedBooking);
    }

    public BookingResponseDTO rejectBooking(Long bookingId, User driver) {
        Booking booking = getBookingById(bookingId);

        // Only the trip driver can reject bookings
        if (!booking.getTrip().getDriver().getId().equals(driver.getId())) {
            throw new UnauthorizedException("Only the trip driver can reject bookings");
        }

        // Validate booking is pending
        if (booking.getStatus() != Booking.Status.PENDING) {
            throw new BadRequestException("Booking is not pending");
        }

        booking.reject();
        Booking savedBooking = bookingRepository.save(booking);
        return convertToResponseDTO(savedBooking);
    }

    public void cancelBooking(Long bookingId, User user) {
        Booking booking = getBookingById(bookingId);

        // Only the passenger or driver can cancel bookings
        boolean isPassenger = booking.getPassenger().getId().equals(user.getId());
        boolean isDriver = booking.getTrip().getDriver().getId().equals(user.getId());

        if (!isPassenger && !isDriver) {
            throw new UnauthorizedException("Unauthorized to cancel this booking");
        }

        // Can only cancel pending or confirmed bookings
        if (booking.getStatus() != Booking.Status.PENDING && booking.getStatus() != Booking.Status.CONFIRMED) {
            throw new BadRequestException("Cannot cancel this booking");
        }

        booking.cancel();
        bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.ERROR_BOOKING_NOT_FOUND));
    }

    public BookingResponseDTO getBookingResponseById(Long id) {
        Booking booking = getBookingById(id);
        return convertToResponseDTO(booking);
    }

    public List<BookingResponseDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDTO> getBookingsByTrip(Long tripId) {
        List<Booking> bookings = bookingRepository.findByTripId(tripId);
        return bookings.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDTO> getBookingsByPassenger(Long passengerId) {
        List<Booking> bookings = bookingRepository.findByPassengerId(passengerId);
        return bookings.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDTO> getPendingBookingsForDriver(User driver) {
        List<Booking> bookings = bookingRepository.findPendingBookingsForDriver(driver);
        return bookings.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public BookingResponseDTO convertToResponseDTO(Booking booking) {
        UserResponseDTO passengerResponse = userService.convertToResponseDTO(booking.getPassenger());
        
        return new BookingResponseDTO(
                booking.getId(),
                booking.getTrip().getId(),
                passengerResponse,
                booking.getSeatsRequested(),
                booking.getTotalPrice(),
                booking.getStatus().name(),
                booking.getBookingDate(),
                booking.getConfirmedDate()
        );
    }
}