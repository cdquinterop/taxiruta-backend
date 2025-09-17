package com.slsolution.taxiruta.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    
    @Builder.Default
    private String type = "Bearer";
    
    private UserResponseDTO user;

    // Lombok generates constructors, getters and setters automatically
}