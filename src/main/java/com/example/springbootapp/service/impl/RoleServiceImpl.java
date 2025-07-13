package com.example.springbootapp.service.impl;

import com.example.springbootapp.dto.RoleDto;
import com.example.springbootapp.entity.Role;
import com.example.springbootapp.entity.Permission;
import com.example.springbootapp.entity.RolePermission;
import com.example.springbootapp.exception.ResourceNotFoundException;
import com.example.springbootapp.repository.RoleRepository;
import com.example.springbootapp.repository.PermissionRepository;
import com.example.springbootapp.repository.RolePermissionRepository;
import com.example.springbootapp.service.RoleService;
import com.example.springbootapp.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }

    @Override
    public Role createRole(RoleDto roleDto) {
        if (roleRepository.existsByName(roleDto.getName())) {
            throw new RuntimeException("Role with name '" + roleDto.getName() + "' already exists");
        }

        Role role = new Role();
        role.setName(roleDto.getName());
        role = roleRepository.save(role);

        if (roleDto.getPermissionIds() != null && !roleDto.getPermissionIds().isEmpty()) {
            assignPermissionsToRole(role.getId(), roleDto.getPermissionIds());
        }

        return role;
    }

    @Override
    public Role updateRole(Long id, RoleDto roleDto) {
        Role role = getRoleById(id);

        if (!role.getName().equals(roleDto.getName()) && roleRepository.existsByName(roleDto.getName())) {
            throw new RuntimeException("Role with name '" + roleDto.getName() + "' already exists");
        }

        role.setName(roleDto.getName());
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
    }

    @Override
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        Role role = getRoleById(roleId);
        
        for (Long permissionId : permissionIds) {
            Permission permission = permissionService.getPermissionById(permissionId);
            
            if (!rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)) {
                RolePermission rolePermission = new RolePermission(role, permission);
                rolePermissionRepository.save(rolePermission);
            }
        }
    }

    @Override
    public void removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
        for (Long permissionId : permissionIds) {
            rolePermissionRepository.deleteByRoleIdAndPermissionId(roleId, permissionId);
        }
    }

    @Override
    public List<Long> getRolePermissions(Long roleId) {
        return rolePermissionRepository.findByRoleId(roleId)
                .stream()
                .map(rp -> rp.getPermission().getId())
                .collect(Collectors.toList());
    }
} 