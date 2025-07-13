package com.example.springbootapp.dto;

public class TokenResponseDto {
    
    private String token;
    
    // Constructors
    public TokenResponseDto() {}
    
    public TokenResponseDto(String token) {
        this.token = token;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
} 