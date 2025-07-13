package com.example.springbootapp.controller;

import com.example.springbootapp.dto.AuthResponseDto;
import com.example.springbootapp.dto.LoginDto;
import com.example.springbootapp.dto.TokenResponseDto;
import com.example.springbootapp.dto.UserRegistrationDto;
import com.example.springbootapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        return ResponseEntity.ok(new TokenResponseDto(token));
    }
    
    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        String token = authService.register(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TokenResponseDto(token));
    }
    
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        boolean isValid = authService.validateToken(jwt);
        
        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);
        
        if (isValid) {
            String username = authService.getUsernameFromToken(jwt);
            response.put("username", username);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Authentication service is running");
        return ResponseEntity.ok(response);
    }
} 