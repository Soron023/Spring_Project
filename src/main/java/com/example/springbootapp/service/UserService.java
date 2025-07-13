package com.example.springbootapp.service;

import com.example.springbootapp.dto.UserRegistrationDto;
import com.example.springbootapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User registerUser(UserRegistrationDto registrationDto);
    List<User> getAllUsers();
    Page<User> getAllUsers(Pageable pageable);
    User getUserById(Long id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    User updateUser(Long id, User userDetails);
    User updateUserPassword(Long id, String newPassword);
    void deleteUser(Long id);
    void deactivateUser(Long id);
    void activateUser(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
} 