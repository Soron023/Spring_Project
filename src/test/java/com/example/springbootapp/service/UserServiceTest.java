package com.example.springbootapp.service;

import com.example.springbootapp.dto.UserRegistrationDto;
import com.example.springbootapp.entity.Role;
import com.example.springbootapp.entity.User;
import com.example.springbootapp.exception.ResourceNotFoundException;
import com.example.springbootapp.exception.UserAlreadyExistsException;
import com.example.springbootapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private UserRegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setRole("USER");
        testUser.setEnabled(true);

        registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("newuser");
        registrationDto.setEmail("new@example.com");
        registrationDto.setPassword("password123");
        registrationDto.setFirstName("New");
        registrationDto.setLastName("User");
    }

    @Test
    void testRegisterUser_Success() {
        // Given
        when(userRepository.existsByUsername(registrationDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        // When
        User result = userService.registerUser(registrationDto);

        // Then
        assertNotNull(result);
        assertEquals(registrationDto.getUsername(), result.getUsername());
        assertEquals(registrationDto.getEmail(), result.getEmail());
        assertEquals("USER", result.getRole());
        assertTrue(result.isEnabled());
        
        verify(userRepository).existsByUsername(registrationDto.getUsername());
        verify(userRepository).existsByEmail(registrationDto.getEmail());
        verify(passwordEncoder).encode(registrationDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() {
        // Given
        when(userRepository.existsByUsername(registrationDto.getUsername())).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(registrationDto);
        });
        
        verify(userRepository).existsByUsername(registrationDto.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Given
        when(userRepository.existsByUsername(registrationDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(registrationDto);
        });
        
        verify(userRepository).existsByUsername(registrationDto.getUsername());
        verify(userRepository).existsByEmail(registrationDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserById_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        User result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        
        verify(userRepository).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
        
        verify(userRepository).findById(999L);
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        var result = userService.loadUserByUsername("testuser");

        // Then
        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        // Given
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistent");
        });
        
        verify(userRepository).findByUsername("nonexistent");
    }
} 