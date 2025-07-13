package com.example.springbootapp.service;

import com.example.springbootapp.dto.AuthResponseDto;
import com.example.springbootapp.dto.LoginDto;
import com.example.springbootapp.dto.UserRegistrationDto;
import com.example.springbootapp.entity.User;
import com.example.springbootapp.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private UserService userService;
    
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        return tokenProvider.generateToken(authentication);
    }
    
    public String register(UserRegistrationDto registrationDto) {
        User user = userService.registerUser(registrationDto);
        
        // Auto-login after registration
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(registrationDto.getUsername(), registrationDto.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        return tokenProvider.generateToken(authentication);
    }
    
    public boolean validateToken(String token) {
        return tokenProvider.validateToken(token);
    }
    
    public String getUsernameFromToken(String token) {
        return tokenProvider.getUsernameFromToken(token);
    }
} 