package com.example.springbootapp.dto;

import com.example.springbootapp.entity.AuditLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AuditLogDto {

    private Long id;

    @NotBlank(message = "User ID is required")
    private String userId;

    private String username;

    @NotBlank(message = "Action is required")
    private String action;

    @NotBlank(message = "Resource type is required")
    private String resourceType;

    private String resourceId;

    private String resourceName;

    private String details;

    private String ipAddress;

    private String userAgent;

    private String requestUrl;

    private String httpMethod;

    private Integer statusCode;

    private Long executionTimeMs;

    @NotNull(message = "Audit level is required")
    private AuditLevel auditLevel;

    private String sessionId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // Constructors
    public AuditLogDto() {}

    public AuditLogDto(Long id, String userId, String username, String action, String resourceType,
                      String resourceId, String resourceName, String details, String ipAddress,
                      String userAgent, String requestUrl, String httpMethod, Integer statusCode,
                      Long executionTimeMs, AuditLevel auditLevel, String sessionId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.action = action;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.details = details;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.requestUrl = requestUrl;
        this.httpMethod = httpMethod;
        this.statusCode = statusCode;
        this.executionTimeMs = executionTimeMs;
        this.auditLevel = auditLevel;
        this.sessionId = sessionId;
        this.createdAt = createdAt;
    }

    // Builder pattern
    public static AuditLogDtoBuilder builder() {
        return new AuditLogDtoBuilder();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }

    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getRequestUrl() { return requestUrl; }
    public void setRequestUrl(String requestUrl) { this.requestUrl = requestUrl; }

    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public AuditLevel getAuditLevel() { return auditLevel; }
    public void setAuditLevel(AuditLevel auditLevel) { this.auditLevel = auditLevel; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder class
    public static class AuditLogDtoBuilder {
        private Long id;
        private String userId;
        private String username;
        private String action;
        private String resourceType;
        private String resourceId;
        private String resourceName;
        private String details;
        private String ipAddress;
        private String userAgent;
        private String requestUrl;
        private String httpMethod;
        private Integer statusCode;
        private Long executionTimeMs;
        private AuditLevel auditLevel;
        private String sessionId;
        private LocalDateTime createdAt;

        public AuditLogDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AuditLogDtoBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public AuditLogDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AuditLogDtoBuilder action(String action) {
            this.action = action;
            return this;
        }

        public AuditLogDtoBuilder resourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        public AuditLogDtoBuilder resourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public AuditLogDtoBuilder resourceName(String resourceName) {
            this.resourceName = resourceName;
            return this;
        }

        public AuditLogDtoBuilder details(String details) {
            this.details = details;
            return this;
        }

        public AuditLogDtoBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public AuditLogDtoBuilder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public AuditLogDtoBuilder requestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }

        public AuditLogDtoBuilder httpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public AuditLogDtoBuilder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public AuditLogDtoBuilder executionTimeMs(Long executionTimeMs) {
            this.executionTimeMs = executionTimeMs;
            return this;
        }

        public AuditLogDtoBuilder auditLevel(AuditLevel auditLevel) {
            this.auditLevel = auditLevel;
            return this;
        }

        public AuditLogDtoBuilder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public AuditLogDtoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AuditLogDto build() {
            return new AuditLogDto(id, userId, username, action, resourceType, resourceId, 
                                 resourceName, details, ipAddress, userAgent, requestUrl, 
                                 httpMethod, statusCode, executionTimeMs, auditLevel, 
                                 sessionId, createdAt);
        }
    }
} 