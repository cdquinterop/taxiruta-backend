package com.slsolution.taxiruta.service;

import com.slsolution.taxiruta.dto.response.UserResponseDTO;
import com.slsolution.taxiruta.exception.ResourceNotFoundException;
import com.slsolution.taxiruta.model.User;
import com.slsolution.taxiruta.repository.UserRepository;
import com.slsolution.taxiruta.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Constants.ERROR_USER_NOT_FOUND));
        return user;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.ERROR_USER_NOT_FOUND));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.ERROR_USER_NOT_FOUND));
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserResponseById(Long id) {
        User user = getUserById(id);
        return convertToResponseDTO(user);
    }

    public UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                user.getPhone(),
                user.getRole().name(),
                user.getIsActive(),
                user.getCreatedAt()
        );
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}