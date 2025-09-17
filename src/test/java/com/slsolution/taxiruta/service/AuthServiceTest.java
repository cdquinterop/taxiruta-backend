package com.slsolution.taxiruta.service;

import com.slsolution.taxiruta.dto.request.LoginRequestDTO;
import com.slsolution.taxiruta.dto.request.UserRegisterRequestDTO;
import com.slsolution.taxiruta.dto.response.AuthResponseDTO;
import com.slsolution.taxiruta.exception.BadRequestException;
import com.slsolution.taxiruta.exception.UnauthorizedException;
import com.slsolution.taxiruta.model.User;
import com.slsolution.taxiruta.repository.UserRepository;
import com.slsolution.taxiruta.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuthService
 * Tests authentication and registration logic with comprehensive coverage
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    private UserRegisterRequestDTO validRegisterRequest;
    private LoginRequestDTO validLoginRequest;
    private User existingUser;

    @BeforeEach
    void setUp() {
        validRegisterRequest = new UserRegisterRequestDTO();
        validRegisterRequest.setUsername("testuser");
        validRegisterRequest.setEmail("test@example.com");
        validRegisterRequest.setPassword("password123");
        validRegisterRequest.setFullName("Test User");
        validRegisterRequest.setPhoneNumber("1234567890");
        validRegisterRequest.setRole("PASSENGER");

        validLoginRequest = new LoginRequestDTO();
        validLoginRequest.setUsernameOrEmail("testuser");
        validLoginRequest.setPassword("password123");

        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("testuser");
        existingUser.setEmail("test@example.com");
        existingUser.setPassword("encodedPassword");
        existingUser.setFullName("Test User");
        existingUser.setPhoneNumber("1234567890");
        existingUser.setRole(User.Role.PASSENGER);
        existingUser.setActive(true);
    }

    @Test
    @DisplayName("Should successfully register new user")
    void shouldSuccessfullyRegisterNewUser() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("jwt-token");
        when(userService.convertToResponseDTO(any(User.class))).thenReturn(null); // Simplified for now

        // When
        AuthResponseDTO result = authService.register(validRegisterRequest);

        // Then
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        assertEquals("Bearer", result.getType());

        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(jwtUtil).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // When & Then
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> authService.register(validRegisterRequest)
        );

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // When & Then
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> authService.register(validRegisterRequest)
        );

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should successfully login with valid credentials")
    void shouldSuccessfullyLoginWithValidCredentials() {
        // Given
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(existingUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("jwt-token");
        when(userService.convertToResponseDTO(any(User.class))).thenReturn(null); // Simplified for now

        // When
        AuthResponseDTO result = authService.login(validLoginRequest);

        // Then
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        assertEquals("Bearer", result.getType());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when login credentials are invalid")
    void shouldThrowExceptionWhenLoginCredentialsAreInvalid() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When & Then
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authService.login(validLoginRequest)
        );

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, never()).generateToken(any(User.class));
    }
}