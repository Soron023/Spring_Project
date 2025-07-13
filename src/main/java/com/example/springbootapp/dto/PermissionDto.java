package com.example.springbootapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PermissionDto {
    private Long id;

    @NotBlank(message = "Permission name is required")
    @Size(min = 2, max = 100, message = "Permission name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Resource is required")
    private String resource;

    @NotBlank(message = "Action is required")
    private String action;

    private boolean active = true;

    public PermissionDto() {}

    public PermissionDto(String name, String description, String resource, String action) {
        this.name = name;
        this.description = description;
        this.resource = resource;
        this.action = action;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
} 