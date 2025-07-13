package com.example.springbootapp.service;

import com.example.springbootapp.dto.AuditLogDto;
import com.example.springbootapp.dto.AuditQueryDto;
import com.example.springbootapp.entity.AuditLevel;
import com.example.springbootapp.util.GenericResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AuditService {

    /**
     * Create a new audit log entry
     */
    AuditLogDto createAuditLog(AuditLogDto auditLogDto);

    /**
     * Create audit log with builder pattern
     */
    AuditLogDto createAuditLog(String userId, String username, String action, String resourceType,
                              String resourceId, String resourceName, String details, AuditLevel auditLevel);

    /**
     * Get audit log by ID
     */
    AuditLogDto getAuditLogById(Long id);

    /**
     * Get audit logs with filters
     */
    Page<AuditLogDto> getAuditLogs(AuditQueryDto queryDto);

    /**
     * Get audit logs by user ID
     */
    Page<AuditLogDto> getAuditLogsByUserId(String userId, int page, int size);

    /**
     * Get audit logs by action
     */
    Page<AuditLogDto> getAuditLogsByAction(String action, int page, int size);

    /**
     * Get audit logs by resource type
     */
    Page<AuditLogDto> getAuditLogsByResourceType(String resourceType, int page, int size);

    /**
     * Get audit logs by audit level
     */
    Page<AuditLogDto> getAuditLogsByLevel(AuditLevel auditLevel, int page, int size);

    /**
     * Get audit logs by date range
     */
    Page<AuditLogDto> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size);

    /**
     * Search audit logs by text
     */
    Page<AuditLogDto> searchAuditLogs(String searchTerm, int page, int size);

    /**
     * Get security events
     */
    Page<AuditLogDto> getSecurityEvents(int page, int size);

    /**
     * Get failed login attempts
     */
    Page<AuditLogDto> getFailedLogins(int page, int size);

    /**
     * Get successful logins
     */
    Page<AuditLogDto> getSuccessfulLogins(int page, int size);

    /**
     * Get data access events
     */
    Page<AuditLogDto> getDataAccessEvents(int page, int size);

    /**
     * Get audit statistics
     */
    Map<String, Object> getAuditStatistics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get action statistics
     */
    List<Map<String, Object>> getActionStatistics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get user activity statistics
     */
    List<Map<String, Object>> getUserActivityStatistics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get events by level
     */
    List<Map<String, Object>> getEventsByLevel(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get recent user activity
     */
    List<AuditLogDto> getRecentUserActivity(String userId, int limit);

    /**
     * Find suspicious activity
     */
    List<Map<String, Object>> findSuspiciousActivity(LocalDateTime startDate, LocalDateTime endDate, long threshold);

    /**
     * Log user login success
     */
    void logLoginSuccess(String userId, String username, String ipAddress, String userAgent, String sessionId);

    /**
     * Log user login failure
     */
    void logLoginFailure(String username, String ipAddress, String userAgent, String reason);

    /**
     * Log user logout
     */
    void logLogout(String userId, String username, String sessionId);

    /**
     * Log data access (CRUD operations)
     */
    void logDataAccess(String userId, String username, String action, String resourceType, 
                      String resourceId, String resourceName, String details);

    /**
     * Log security event
     */
    void logSecurityEvent(String userId, String username, String action, String details, AuditLevel level);

    /**
     * Log system event
     */
    void logSystemEvent(String action, String details, AuditLevel level);

    /**
     * Export audit logs to CSV
     */
    byte[] exportAuditLogsToCsv(AuditQueryDto queryDto);

    /**
     * Clean old audit logs
     */
    void cleanOldAuditLogs(LocalDateTime beforeDate);

    /**
     * Get audit log count by level
     */
    long getAuditLogCountByLevel(AuditLevel auditLevel, LocalDateTime startDate, LocalDateTime endDate);
} 