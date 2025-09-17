package com.slsolution.taxiruta.service;

import com.slsolution.taxiruta.dto.response.UserResponseDTO;
import com.slsolution.taxiruta.exception.ResourceNotFoundException;
import com.slsolution.taxiruta.model.User;
import com.slsolution.taxiruta.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService
 * Tests business logic and service layer functionality
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User testDriver;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setFullName("Test User");
        testUser.setPhoneNumber("1234567890");
        testUser.setRole(User.Role.PASSENGER);
        testUser.setActive(true);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());

        testDriver = new User();
        testDriver.setId(2L);
        testDriver.setUsername("testdriver");
        testDriver.setEmail("driver@example.com");
        testDriver.setPassword("password");
        testDriver.setFullName("Test Driver");
        testDriver.setPhoneNumber("0987654321");
        testDriver.setRole(User.Role.DRIVER);
        testDriver.setActive(true);
        testDriver.setCreatedAt(LocalDateTime.now());
        testDriver.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should load user by username successfully")
    void shouldLoadUserByUsernameSuccessfully() {
        // Given
        when(userRepository.findByUsernameOrEmail(anyString(), anyString()))
                .thenReturn(Optional.of(testUser));

        // When
        UserDetails result = userService.loadUserByUsername("testuser");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.isEnabled()).isTrue();
        verify(userRepository).findByUsernameOrEmail("testuser", "testuser");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findByUsernameOrEmail(anyString(), anyString()))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.loadUserByUsername("nonexistent"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findByUsernameOrEmail("nonexistent", "nonexistent");
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserByIdSuccessfully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        User result = userService.getUserById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user ID not found")
    void shouldThrowResourceNotFoundExceptionWhenUserIdNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findById(999L);
    }

    @Test
    @DisplayName("Should get user response DTO by ID successfully")
    void shouldGetUserResponseDtoByIdSuccessfully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserResponseDTO result = userService.getUserResponseById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getFullName()).isEqualTo("Test User");
        assertThat(result.getRole()).isEqualTo("PASSENGER");
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should get all users successfully")
    void shouldGetAllUsersSuccessfully() {
        // Given
        List<User> users = Arrays.asList(testUser, testDriver);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserResponseDTO> result = userService.getAllUsers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("testuser");
        assertThat(result.get(1).getUsername()).isEqualTo("testdriver");
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should check if username exists")
    void shouldCheckIfUsernameExists() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        when(userRepository.existsByUsername("newuser")).thenReturn(false);

        // When & Then
        assertThat(userService.existsByUsername("testuser")).isTrue();
        assertThat(userService.existsByUsername("newuser")).isFalse();

        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByUsername("newuser");
    }

    @Test
    @DisplayName("Should check if email exists")
    void shouldCheckIfEmailExists() {
        // Given
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        // When & Then
        assertThat(userService.existsByEmail("test@example.com")).isTrue();
        assertThat(userService.existsByEmail("new@example.com")).isFalse();

        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository).existsByEmail("new@example.com");
    }

    @Test
    @DisplayName("Should convert User to UserResponseDTO correctly")
    void shouldConvertUserToUserResponseDtoCorrectly() {
        // When
        UserResponseDTO result = userService.convertToResponseDTO(testUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUser.getId());
        assertThat(result.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(result.getFullName()).isEqualTo(testUser.getFullName());
        assertThat(result.getPhoneNumber()).isEqualTo(testUser.getPhoneNumber());
        assertThat(result.getRole()).isEqualTo(testUser.getRole().name());
        assertThat(result.getActive()).isEqualTo(testUser.getActive());
        assertThat(result.getCreatedAt()).isEqualTo(testUser.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle user authorities correctly")
    void shouldHandleUserAuthoritiesCorrectly() {
        // When
        var authorities = testUser.getAuthorities();

        // Then
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_PASSENGER");

        // Test driver authorities
        var driverAuthorities = testDriver.getAuthorities();
        assertThat(driverAuthorities).hasSize(1);
        assertThat(driverAuthorities.iterator().next().getAuthority()).isEqualTo("ROLE_DRIVER");
    }
}