package com.slsolution.taxiruta.controller;

import com.slsolution.taxiruta.dto.request.BookingRequestDTO;
import com.slsolution.taxiruta.dto.response.ApiResponseDTO;
import com.slsolution.taxiruta.dto.response.BookingResponseDTO;
import com.slsolution.taxiruta.model.User;
import com.slsolution.taxiruta.service.BookingService;
import com.slsolution.taxiruta.util.Constants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de reservas de viajes
 * Maneja todas las operaciones relacionadas con bookings
 */
@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Crear una reserva para un viaje específico
     */
    @PostMapping("/trips/{tripId}/bookings")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<ApiResponseDTO<BookingResponseDTO>> createBooking(
            @PathVariable Long tripId,
            @Valid @RequestBody BookingRequestDTO request,
            @AuthenticationPrincipal User currentUser) {
        
        BookingResponseDTO booking = bookingService.createBooking(tripId, request, currentUser);
        ApiResponseDTO<BookingResponseDTO> response = ApiResponseDTO.success(
                Constants.SUCCESS_BOOKING_CREATED, booking);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Aceptar una reserva (solo conductores)
     */
    @PutMapping("/trips/{tripId}/bookings/{bookingId}/accept")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponseDTO<BookingResponseDTO>> acceptBooking(
            @PathVariable Long tripId,
            @PathVariable Long bookingId,
            @AuthenticationPrincipal User currentUser) {
        
        BookingResponseDTO booking = bookingService.confirmBooking(bookingId, currentUser);
        ApiResponseDTO<BookingResponseDTO> response = ApiResponseDTO.success(
                Constants.SUCCESS_BOOKING_CONFIRMED, booking);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Rechazar una reserva (solo conductores)
     */
    @PutMapping("/trips/{tripId}/bookings/{bookingId}/reject")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponseDTO<BookingResponseDTO>> rejectBooking(
            @PathVariable Long tripId,
            @PathVariable Long bookingId,
            @AuthenticationPrincipal User currentUser) {
        
        BookingResponseDTO booking = bookingService.rejectBooking(bookingId, currentUser);
        ApiResponseDTO<BookingResponseDTO> response = ApiResponseDTO.success(
                Constants.SUCCESS_BOOKING_REJECTED, booking);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Listar reservas de un viaje (solo conductores)
     */
    @GetMapping("/trips/{tripId}/bookings")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponseDTO<List<BookingResponseDTO>>> getTripBookings(
            @PathVariable Long tripId) {
        
        List<BookingResponseDTO> bookings = bookingService.getBookingsByTrip(tripId);
        ApiResponseDTO<List<BookingResponseDTO>> response = ApiResponseDTO.success(
                "Trip bookings retrieved successfully", bookings);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Listar reservas de un pasajero
     */
    @GetMapping("/users/{userId}/bookings")
    public ResponseEntity<ApiResponseDTO<List<BookingResponseDTO>>> getUserBookings(
            @PathVariable Long userId,
            @AuthenticationPrincipal User currentUser) {
        
        // Solo el mismo usuario puede ver sus reservas
        if (!currentUser.getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to user bookings");
        }
        
        List<BookingResponseDTO> bookings = bookingService.getBookingsByPassenger(userId);
        ApiResponseDTO<List<BookingResponseDTO>> response = ApiResponseDTO.success(
                "User bookings retrieved successfully", bookings);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Cancelar una reserva
     */
    @DeleteMapping("/trips/{tripId}/bookings/{bookingId}")
    public ResponseEntity<ApiResponseDTO<String>> cancelBooking(
            @PathVariable Long tripId,
            @PathVariable Long bookingId,
            @AuthenticationPrincipal User currentUser) {
        
        bookingService.cancelBooking(bookingId, currentUser);
        ApiResponseDTO<String> response = ApiResponseDTO.success(
                Constants.SUCCESS_BOOKING_CANCELLED, null);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener mis reservas
     */
    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponseDTO<List<BookingResponseDTO>>> getMyBookings(
            @AuthenticationPrincipal User currentUser) {
        
        List<BookingResponseDTO> bookings = bookingService.getBookingsByPassenger(currentUser.getId());
        ApiResponseDTO<List<BookingResponseDTO>> response = ApiResponseDTO.success(
                "User bookings retrieved successfully", bookings);
        
        return ResponseEntity.ok(response);
    }
}