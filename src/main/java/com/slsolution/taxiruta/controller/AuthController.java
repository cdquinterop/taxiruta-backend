package com.slsolution.taxiruta.controller;

import com.slsolution.taxiruta.dto.request.LoginRequestDTO;
import com.slsolution.taxiruta.dto.request.UserRegisterRequestDTO;
import com.slsolution.taxiruta.dto.response.ApiResponseDTO;
import com.slsolution.taxiruta.dto.response.AuthResponseDTO;
import com.slsolution.taxiruta.service.AuthService;
import com.slsolution.taxiruta.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticación y registro de usuarios
 * Maneja login, registro y operaciones relacionadas con JWT
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro, login y gestión de autenticación JWT")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Registra un nuevo usuario en el sistema
     */
    @Operation(
        summary = "Registrar nuevo usuario", 
        description = "Registra un nuevo usuario en el sistema con rol de conductor o pasajero"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Usuario registrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "El usuario ya existe",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> register(
            @Valid @RequestBody UserRegisterRequestDTO request) {
        
        AuthResponseDTO response = authService.register(request);
        ApiResponseDTO<AuthResponseDTO> apiResponse = ApiResponseDTO.success(
                Constants.SUCCESS_USER_REGISTERED, response);
        
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    /**
     * Autentica un usuario y devuelve un token JWT
     */
    @Operation(
        summary = "Iniciar sesión", 
        description = "Autentica las credenciales del usuario y devuelve un token JWT válido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Autenticación exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Credenciales inválidas",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Credenciales incorrectas",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {
        
        AuthResponseDTO response = authService.login(request);
        ApiResponseDTO<AuthResponseDTO> apiResponse = ApiResponseDTO.success(
                Constants.SUCCESS_LOGIN, response);
        
        return ResponseEntity.ok(apiResponse);
    }
}