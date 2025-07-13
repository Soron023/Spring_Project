package com.example.springbootapp.service;

import com.example.springbootapp.dto.PermissionDto;
import com.example.springbootapp.entity.Permission;
import java.util.List;

public interface PermissionService {
    List<Permission> getAllPermissions();
    Permission getPermissionById(Long id);
    Permission createPermission(PermissionDto permissionDto);
    Permission updatePermission(Long id, PermissionDto permissionDto);
    void deletePermission(Long id);
    List<Permission> getPermissionsByResource(String resource);
} 