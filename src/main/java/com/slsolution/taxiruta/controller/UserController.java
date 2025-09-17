package com.slsolution.taxiruta.controller;

import com.slsolution.taxiruta.dto.response.ApiResponseDTO;
import com.slsolution.taxiruta.dto.response.UserResponseDTO;
import com.slsolution.taxiruta.model.User;
import com.slsolution.taxiruta.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de usuarios
 * Proporciona endpoints para obtener información de usuarios
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Obtiene un usuario por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(
            @PathVariable Long id) {
        
        UserResponseDTO user = userService.getUserResponseById(id);
        ApiResponseDTO<UserResponseDTO> response = ApiResponseDTO.success(
                "User retrieved successfully", user);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene el perfil del usuario autenticado
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getProfile(
            @AuthenticationPrincipal User currentUser) {
        
        UserResponseDTO userProfile = userService.getUserResponseById(currentUser.getId());
        ApiResponseDTO<UserResponseDTO> response = ApiResponseDTO.success(
                "Profile retrieved successfully", userProfile);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todos los usuarios (solo administradores)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getAllUsers() {
        
        List<UserResponseDTO> users = userService.getAllUsers();
        ApiResponseDTO<List<UserResponseDTO>> response = ApiResponseDTO.success(
                "Users retrieved successfully", users);
        
        return ResponseEntity.ok(response);
    }
}