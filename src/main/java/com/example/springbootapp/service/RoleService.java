package com.example.springbootapp.service;

import com.example.springbootapp.dto.RoleDto;
import com.example.springbootapp.entity.Role;
import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role createRole(RoleDto roleDto);
    Role updateRole(Long id, RoleDto roleDto);
    void deleteRole(Long id);
    void assignPermissionsToRole(Long roleId, List<Long> permissionIds);
    void removePermissionsFromRole(Long roleId, List<Long> permissionIds);
    List<Long> getRolePermissions(Long roleId);
} 