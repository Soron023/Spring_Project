package com.example.springbootapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "username")
    private String username;

    @NotBlank
    @Column(name = "action", nullable = false)
    private String action;

    @NotBlank
    @Column(name = "resource_type", nullable = false)
    private String resourceType;

    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "request_url")
    private String requestUrl;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "audit_level", nullable = false)
    private AuditLevel auditLevel;

    @Column(name = "session_id")
    private String sessionId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public AuditLog() {}

    public AuditLog(String userId, String username, String action, String resourceType, 
                   String resourceId, String resourceName, String details, AuditLevel auditLevel) {
        this.userId = userId;
        this.username = username;
        this.action = action;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.details = details;
        this.auditLevel = auditLevel;
    }

    // Builder pattern
    public static AuditLogBuilder builder() {
        return new AuditLogBuilder();
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
    public static class AuditLogBuilder {
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
        private AuditLevel auditLevel = AuditLevel.INFO;
        private String sessionId;

        public AuditLogBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public AuditLogBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AuditLogBuilder action(String action) {
            this.action = action;
            return this;
        }

        public AuditLogBuilder resourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        public AuditLogBuilder resourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public AuditLogBuilder resourceName(String resourceName) {
            this.resourceName = resourceName;
            return this;
        }

        public AuditLogBuilder details(String details) {
            this.details = details;
            return this;
        }

        public AuditLogBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public AuditLogBuilder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public AuditLogBuilder requestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }

        public AuditLogBuilder httpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public AuditLogBuilder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public AuditLogBuilder executionTimeMs(Long executionTimeMs) {
            this.executionTimeMs = executionTimeMs;
            return this;
        }

        public AuditLogBuilder auditLevel(AuditLevel auditLevel) {
            this.auditLevel = auditLevel;
            return this;
        }

        public AuditLogBuilder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public AuditLog build() {
            AuditLog auditLog = new AuditLog();
            auditLog.setUserId(userId);
            auditLog.setUsername(username);
            auditLog.setAction(action);
            auditLog.setResourceType(resourceType);
            auditLog.setResourceId(resourceId);
            auditLog.setResourceName(resourceName);
            auditLog.setDetails(details);
            auditLog.setIpAddress(ipAddress);
            auditLog.setUserAgent(userAgent);
            auditLog.setRequestUrl(requestUrl);
            auditLog.setHttpMethod(httpMethod);
            auditLog.setStatusCode(statusCode);
            auditLog.setExecutionTimeMs(executionTimeMs);
            auditLog.setAuditLevel(auditLevel);
            auditLog.setSessionId(sessionId);
            return auditLog;
        }
    }
} 