package com.example.springbootapp.controller;

import com.example.springbootapp.dto.RoleDto;
import com.example.springbootapp.entity.Role;
import com.example.springbootapp.service.RoleService;
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
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {
    
    @Autowired
    private RoleService roleService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> createRole(@Valid @RequestBody RoleDto roleDto) {
        Role role = roleService.createRole(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDto roleDto) {
        Role role = roleService.updateRole(id, roleDto);
        return ResponseEntity.ok(role);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Role deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> assignPermissionsToRole(
            @PathVariable Long id,
            @RequestBody List<Long> permissionIds) {
        roleService.assignPermissionsToRole(id, permissionIds);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Permissions assigned successfully");
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> removePermissionsFromRole(
            @PathVariable Long id,
            @RequestBody List<Long> permissionIds) {
        roleService.removePermissionsFromRole(id, permissionIds);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Permissions removed successfully");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Long>> getRolePermissions(@PathVariable Long id) {
        List<Long> permissionIds = roleService.getRolePermissions(id);
        return ResponseEntity.ok(permissionIds);
    }
} 