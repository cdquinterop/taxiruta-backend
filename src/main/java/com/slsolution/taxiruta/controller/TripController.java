package com.slsolution.taxiruta.controller;

import com.slsolution.taxiruta.dto.request.TripRequestDTO;
import com.slsolution.taxiruta.dto.response.ApiResponseDTO;
import com.slsolution.taxiruta.dto.response.TripResponseDTO;
import com.slsolution.taxiruta.model.User;
import com.slsolution.taxiruta.service.TripService;
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
 * Controlador REST para gestión de viajes
 * Maneja todas las operaciones relacionadas con viajes
 */
@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    /**
     * Crea un nuevo viaje (solo conductores)
     */
    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponseDTO<TripResponseDTO>> createTrip(
            @Valid @RequestBody TripRequestDTO request,
            @AuthenticationPrincipal User currentUser) {
        
        TripResponseDTO trip = tripService.createTrip(request, currentUser);
        ApiResponseDTO<TripResponseDTO> response = ApiResponseDTO.success(
                Constants.SUCCESS_TRIP_CREATED, trip);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Obtiene todos los viajes activos disponibles
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<TripResponseDTO>>> getAllTrips() {
        
        List<TripResponseDTO> trips = tripService.getActiveTrips();
        ApiResponseDTO<List<TripResponseDTO>> response = ApiResponseDTO.success(
                "Trips retrieved successfully", trips);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un viaje por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TripResponseDTO>> getTripById(
            @PathVariable Long id) {
        
        TripResponseDTO trip = tripService.getTripResponseById(id);
        ApiResponseDTO<TripResponseDTO> response = ApiResponseDTO.success(
                "Trip retrieved successfully", trip);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza un viaje existente (solo el conductor propietario)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponseDTO<TripResponseDTO>> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripRequestDTO request,
            @AuthenticationPrincipal User currentUser) {
        
        TripResponseDTO trip = tripService.updateTrip(id, request, currentUser);
        ApiResponseDTO<TripResponseDTO> response = ApiResponseDTO.success(
                Constants.SUCCESS_TRIP_UPDATED, trip);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Cancela un viaje (solo el conductor propietario)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponseDTO<String>> deleteTrip(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        
        tripService.cancelTrip(id, currentUser);
        ApiResponseDTO<String> response = ApiResponseDTO.success(
                Constants.SUCCESS_TRIP_CANCELLED, null);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene los viajes del conductor autenticado
     */
    @GetMapping("/my-trips")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponseDTO<List<TripResponseDTO>>> getMyTrips(
            @AuthenticationPrincipal User currentUser) {
        
        List<TripResponseDTO> trips = tripService.getDriverTrips(currentUser.getId());
        ApiResponseDTO<List<TripResponseDTO>> response = ApiResponseDTO.success(
                "Driver trips retrieved successfully", trips);
        
        return ResponseEntity.ok(response);
    }
}