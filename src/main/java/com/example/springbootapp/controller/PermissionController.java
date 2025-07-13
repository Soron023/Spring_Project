package com.example.springbootapp.controller;

import com.example.springbootapp.dto.PermissionDto;
import com.example.springbootapp.entity.Permission;
import com.example.springbootapp.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/permissions")
@CrossOrigin(origins = "*")
public class PermissionController {
    
    @Autowired
    private PermissionService permissionService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        Permission permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(permission);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody PermissionDto permissionDto) {
        Permission permission = permissionService.createPermission(permissionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(permission);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long id, @Valid @RequestBody PermissionDto permissionDto) {
        Permission permission = permissionService.updatePermission(id, permissionDto);
        return ResponseEntity.ok(permission);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Permission deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/resource/{resource}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Permission>> getPermissionsByResource(@PathVariable String resource) {
        List<Permission> permissions = permissionService.getPermissionsByResource(resource);
        return ResponseEntity.ok(permissions);
    }
} 