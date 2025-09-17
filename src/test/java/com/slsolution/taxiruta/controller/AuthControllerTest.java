package com.slsolution.taxiruta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slsolution.taxiruta.dto.request.LoginRequestDTO;
import com.slsolution.taxiruta.dto.request.UserRegisterRequestDTO;
import com.slsolution.taxiruta.dto.response.AuthResponseDTO;
import com.slsolution.taxiruta.dto.response.UserResponseDTO;
import com.slsolution.taxiruta.exception.BadRequestException;
import com.slsolution.taxiruta.exception.UnauthorizedException;
import com.slsolution.taxiruta.model.User;
import com.slsolution.taxiruta.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AuthController.
 * Tests all authentication endpoints: register and login.
 * Following senior-level testing practices with comprehensive coverage.
 * 
 * @author Senior Developer
 * @version 1.0
 */
@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRegisterRequestDTO validRegisterRequest;
    private LoginRequestDTO validLoginRequest;
    private AuthResponseDTO authResponse;
    private UserResponseDTO userResponse;

    @BeforeEach
    void setUp() {
        // Setup test data using Lombok builders
        validRegisterRequest = UserRegisterRequestDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .password("SecurePassword123!")
                .build();

        validLoginRequest = LoginRequestDTO.builder()
                .usernameOrEmail("john.doe@example.com")
                .password("SecurePassword123!")
                .build();

        userResponse = UserResponseDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .role(User.Role.PASSENGER)
                .isActive(true)
                .build();

        authResponse = AuthResponseDTO.builder()
                .token("jwt.token.here")
                .type("Bearer")
                .user(userResponse)
                .build();
    }

    // ============ REGISTER ENDPOINT TESTS ============

    @Test
    @DisplayName("POST /api/auth/register - Should register user successfully")
    void shouldRegisterUserSuccessfully() throws Exception {
        // Given
        when(authService.register(any(UserRegisterRequestDTO.class))).thenReturn(authResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("jwt.token.here"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.user.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.user.firstName").value("John"))
                .andExpect(jsonPath("$.user.lastName").value("Doe"));

        verify(authService, times(1)).register(any(UserRegisterRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/auth/register - Should return 400 when email already exists")
    void shouldReturn400WhenEmailAlreadyExists() throws Exception {
        // Given
        when(authService.register(any(UserRegisterRequestDTO.class)))
                .thenThrow(new BadRequestException("Email already exists"));

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, times(1)).register(any(UserRegisterRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/auth/register - Should return 400 for invalid request body")
    void shouldReturn400ForInvalidRegisterRequest() throws Exception {
        // Given - Invalid request with missing required fields
        UserRegisterRequestDTO invalidRequest = UserRegisterRequestDTO.builder()
                .email("invalid-email")
                .password("123") // Too short
                .build();

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/register - Should return 400 for empty request body")
    void shouldReturn400ForEmptyRegisterRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ============ LOGIN ENDPOINT TESTS ============

    @Test
    @DisplayName("POST /api/auth/login - Should login user successfully")
    void shouldLoginUserSuccessfully() throws Exception {
        // Given
        when(authService.login(any(LoginRequestDTO.class))).thenReturn(authResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpected(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("jwt.token.here"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.user.email").value("john.doe@example.com"));

        verify(authService, times(1)).login(any(LoginRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/auth/login - Should return 401 for invalid credentials")
    void shouldReturn401ForInvalidCredentials() throws Exception {
        // Given
        when(authService.login(any(LoginRequestDTO.class)))
                .thenThrow(new UnauthorizedException("Invalid credentials"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isUnauthorized());

        verify(authService, times(1)).login(any(LoginRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/auth/login - Should return 400 for invalid request format")
    void shouldReturn400ForInvalidLoginRequest() throws Exception {
        // Given - Invalid request with missing fields
        LoginRequestDTO invalidRequest = LoginRequestDTO.builder()
                .usernameOrEmail("")
                .password("")
                .build();

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login - Should return 400 for empty request body")
    void shouldReturn400ForEmptyLoginRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login - Should return 415 for unsupported media type")
    void shouldReturn415ForUnsupportedMediaType() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("plain text content"))
                .andExpect(status().isUnsupportedMediaType());
    }
}