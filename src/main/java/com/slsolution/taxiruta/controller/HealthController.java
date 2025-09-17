package com.slsolution.taxiruta.controller;

import com.slsolution.taxiruta.dto.response.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "TaxiRuta Backend");
        status.put("version", "1.0.0");
        
        ApiResponseDTO<Map<String, String>> response = ApiResponseDTO.success(
                "Service is running", status);
        
        return ResponseEntity.ok(response);
    }
}