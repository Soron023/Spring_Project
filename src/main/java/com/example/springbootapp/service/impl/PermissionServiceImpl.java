package com.example.springbootapp.service.impl;

import com.example.springbootapp.dto.PermissionDto;
import com.example.springbootapp.entity.Permission;
import com.example.springbootapp.exception.ResourceNotFoundException;
import com.example.springbootapp.repository.PermissionRepository;
import com.example.springbootapp.service.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findByActiveTrue();
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + id));
    }

    @Override
    public Permission createPermission(PermissionDto permissionDto) {
        if (permissionRepository.existsByName(permissionDto.getName())) {
            throw new RuntimeException("Permission with name '" + permissionDto.getName() + "' already exists");
        }

        Permission permission = new Permission();
        permission.setName(permissionDto.getName());
        permission.setDescription(permissionDto.getDescription());
        permission.setResource(permissionDto.getResource());
        permission.setAction(permissionDto.getAction());
        permission.setActive(permissionDto.isActive());

        return permissionRepository.save(permission);
    }

    @Override
    public Permission updatePermission(Long id, PermissionDto permissionDto) {
        Permission permission = getPermissionById(id);

        if (!permission.getName().equals(permissionDto.getName()) && permissionRepository.existsByName(permissionDto.getName())) {
            throw new RuntimeException("Permission with name '" + permissionDto.getName() + "' already exists");
        }

        permission.setName(permissionDto.getName());
        permission.setDescription(permissionDto.getDescription());
        permission.setResource(permissionDto.getResource());
        permission.setAction(permissionDto.getAction());
        permission.setActive(permissionDto.isActive());

        return permissionRepository.save(permission);
    }

    // Remove @Override since this method is not in the interface
    public Permission updatePermissionWithBeanUtils(Long id, Permission updatedData) {
        Permission permission = getPermissionById(id);
        BeanUtils.copyProperties(updatedData, permission, getNullPropertyNames(updatedData));
        // Do not update id
        return permissionRepository.save(permission);
    }

    private String[] getNullPropertyNames(Object source) {
        try {
            java.beans.BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(source.getClass(), Object.class);
            return java.util.Arrays.stream(beanInfo.getPropertyDescriptors())
                    .filter(pd -> {
                        try {
                            return pd.getReadMethod().invoke(source) == null;
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .map(java.beans.PropertyDescriptor::getName)
                    .toArray(String[]::new);
        } catch (java.beans.IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePermission(Long id) {
        Permission permission = getPermissionById(id);
        permissionRepository.delete(permission);
    }

    @Override
    public List<Permission> getPermissionsByResource(String resource) {
        return permissionRepository.findByResource(resource);
    }
}