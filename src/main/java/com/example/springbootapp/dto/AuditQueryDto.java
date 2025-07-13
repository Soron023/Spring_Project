package com.example.springbootapp.dto;

import com.example.springbootapp.entity.AuditLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public class AuditQueryDto {

    private String userId;
    private String username;
    private String action;
    private String resourceType;
    private String resourceId;
    private AuditLevel auditLevel;
    private String ipAddress;
    private String sessionId;
    private String httpMethod;
    private Integer statusCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    private String searchTerm;

    @Min(value = 0, message = "Page must be 0 or greater")
    private Integer page = 0;

    @Min(value = 1, message = "Size must be 1 or greater")
    private Integer size = 20;

    // Constructors
    public AuditQueryDto() {}

    // Builder pattern
    public static AuditQueryDtoBuilder builder() {
        return new AuditQueryDtoBuilder();
    }

    // Getters and Setters
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

    public AuditLevel getAuditLevel() { return auditLevel; }
    public void setAuditLevel(AuditLevel auditLevel) { this.auditLevel = auditLevel; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getSearchTerm() { return searchTerm; }
    public void setSearchTerm(String searchTerm) { this.searchTerm = searchTerm; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    // Builder class
    public static class AuditQueryDtoBuilder {
        private String userId;
        private String username;
        private String action;
        private String resourceType;
        private String resourceId;
        private AuditLevel auditLevel;
        private String ipAddress;
        private String sessionId;
        private String httpMethod;
        private Integer statusCode;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String searchTerm;
        private Integer page = 0;
        private Integer size = 20;

        public AuditQueryDtoBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public AuditQueryDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AuditQueryDtoBuilder action(String action) {
            this.action = action;
            return this;
        }

        public AuditQueryDtoBuilder resourceType(String resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        public AuditQueryDtoBuilder resourceId(String resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public AuditQueryDtoBuilder auditLevel(AuditLevel auditLevel) {
            this.auditLevel = auditLevel;
            return this;
        }

        public AuditQueryDtoBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public AuditQueryDtoBuilder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public AuditQueryDtoBuilder httpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public AuditQueryDtoBuilder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public AuditQueryDtoBuilder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public AuditQueryDtoBuilder endDate(LocalDateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        public AuditQueryDtoBuilder searchTerm(String searchTerm) {
            this.searchTerm = searchTerm;
            return this;
        }

        public AuditQueryDtoBuilder page(Integer page) {
            this.page = page;
            return this;
        }

        public AuditQueryDtoBuilder size(Integer size) {
            this.size = size;
            return this;
        }

        public AuditQueryDto build() {
            AuditQueryDto queryDto = new AuditQueryDto();
            queryDto.setUserId(userId);
            queryDto.setUsername(username);
            queryDto.setAction(action);
            queryDto.setResourceType(resourceType);
            queryDto.setResourceId(resourceId);
            queryDto.setAuditLevel(auditLevel);
            queryDto.setIpAddress(ipAddress);
            queryDto.setSessionId(sessionId);
            queryDto.setHttpMethod(httpMethod);
            queryDto.setStatusCode(statusCode);
            queryDto.setStartDate(startDate);
            queryDto.setEndDate(endDate);
            queryDto.setSearchTerm(searchTerm);
            queryDto.setPage(page);
            queryDto.setSize(size);
            return queryDto;
        }
    }
} 