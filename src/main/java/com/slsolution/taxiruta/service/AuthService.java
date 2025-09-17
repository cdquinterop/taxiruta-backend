package com.slsolution.taxiruta.service;

import com.slsolution.taxiruta.dto.request.LoginRequestDTO;
import com.slsolution.taxiruta.dto.request.UserRegisterRequestDTO;
import com.slsolution.taxiruta.dto.response.AuthResponseDTO;
import com.slsolution.taxiruta.dto.response.UserResponseDTO;
import com.slsolution.taxiruta.exception.BadRequestException;
import com.slsolution.taxiruta.model.User;
import com.slsolution.taxiruta.repository.UserRepository;
import com.slsolution.taxiruta.util.Constants;
import com.slsolution.taxiruta.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    public AuthResponseDTO register(UserRegisterRequestDTO request) {
        // Validate email doesn't already exist
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(Constants.ERROR_EMAIL_ALREADY_EXISTS);
        }

        // Validate role
        User.Role role;
        try {
            role = User.Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(Constants.ERROR_INVALID_ROLE);
        }

        // Create new user using Lombok builder pattern
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(savedUser);

        // Convert to response DTO
        UserResponseDTO userResponse = userService.convertToResponseDTO(savedUser);

        return AuthResponseDTO.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsernameOrEmail(),
                    request.getPassword()
                )
            );

            User user = (User) authentication.getPrincipal();

            // Generate JWT token
            String token = jwtUtil.generateToken(user);

            // Convert to response DTO
            UserResponseDTO userResponse = userService.convertToResponseDTO(user);

            return AuthResponseDTO.builder()
                    .token(token)
                    .user(userResponse)
                    .build();

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(Constants.ERROR_INVALID_CREDENTIALS);
        }
    }
}