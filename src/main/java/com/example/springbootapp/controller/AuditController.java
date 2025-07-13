package com.example.springbootapp.controller;

import com.example.springbootapp.dto.AuditLogDto;
import com.example.springbootapp.dto.AuditQueryDto;
import com.example.springbootapp.entity.AuditLevel;
import com.example.springbootapp.service.AuditService;
import com.example.springbootapp.util.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audit")
@PreAuthorize("hasRole('ADMIN')")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Get audit logs with filters
     */
    @PostMapping("/logs/search")
    public ResponseEntity<GenericResponse<Page<AuditLogDto>>> getAuditLogs(
            @Valid @RequestBody AuditQueryDto queryDto) {
        Page<AuditLogDto> auditLogs = auditService.getAuditLogs(queryDto);
        return ResponseEntity.ok(GenericResponse.success(auditLogs, "Audit logs retrieved successfully"));
    }

    /**
     * Get audit log by ID
     */
    @GetMapping("/logs/{id}")
    public ResponseEntity<GenericResponse<AuditLogDto>> getAuditLogById(@PathVariable Long id) {
        AuditLogDto auditLog = auditService.getAuditLogById(id);
        return ResponseEntity.ok(GenericResponse.success(auditLog, "Audit log retrieved successfully"));
    }

    /**
     * Get audit logs by user ID
     */
    @GetMapping("/logs/user/{userId}")
    public ResponseEntity<GenericResponse<Page<AuditLogDto>>> getAuditLogsByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AuditLogDto> auditLogs = auditService.getAuditLogsByUserId(userId, page, size);
        return ResponseEntity.ok(GenericResponse.success(auditLogs, "User audit logs retrieved successfully"));
    }

    /**
     * Get audit logs by action
     */
    @GetMapping("/logs/action/{action}")
    public ResponseEntity<GenericResponse<Page<AuditLogDto>>> getAuditLogsByAction(
            @PathVariable String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AuditLogDto> auditLogs = auditService.getAuditLogsByAction(action, page, size);
        return ResponseEntity.ok(GenericResponse.success(auditLogs, "Action audit logs retrieved successfully"));
    }

    /**
     * Get audit logs by resource type
     */
    @GetMapping("/logs/resource/{resourceType}")
    public ResponseEntity<GenericResponse<Page<AuditLogDto>>> getAuditLogsByResourceType(
            @PathVariable String resourceType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AuditLogDto> auditLogs = auditService.getAuditLogsByResourceType(resourceType, page, size);
        return ResponseEntity.ok(GenericResponse.success(auditLogs, "Resource audit logs retrieved successfully"));
    }

    /**
     * Get audit logs by level
     */
    @GetMapping("/logs/level/{level}")
    public ResponseEntity<GenericResponse<Page<AuditLogDto>>> getAuditLogsByLevel(
            @PathVariable AuditLevel level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AuditLogDto> auditLogs = auditService.getAuditLogsByLevel(level, page, size);
        return ResponseEntity.ok(GenericResponse.success(auditLogs, "Level audit logs retrieved successfully"));
    }

    /**
     * Get audit logs by date range
     */
    @GetMapping("/logs/date-range")
    public ResponseEntity<GenericResponse<Page<AuditLogDto>>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AuditLogDto> auditLogs = auditService.getAuditLogsByDateRange(startDate, endDate, page, size);
        return ResponseEntity.ok(GenericResponse.success(auditLogs, "Date range audit logs retrieved successfully"));
    }

    /**
     * Search audit logs by text
     */
    @GetMapping("/logs/search")
    public ResponseEntity<GenericResponse<Page<AuditLogDto>>> searchAuditLogs(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AuditLogDto> auditLogs = auditService.searchAuditLogs(searchTerm, page, size);
        return ResponseEntity.ok(GenericResponse.success(auditLogs, "Search results retrieved successfully"));
    }

    /**
     * Get security events
     */
    @GetMapping("/logs/security")
    public ResponseEntity<GenericResponse<Page<AuditLogDto>>> getSecurityEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AuditLogDto> securityEvents = auditService.getSecurityEvents(page, size);
        return ResponseEntity.ok(GenericResponse.success(securityEvents, "Security events retrieved successfully"));
    }

    /**
     * Get failed login attempts
     */
    @GetMapping("/logs/failed-logins")
    public ResponseEntity<GenericResponse<Page<AuditLogDto>>> getFailedLogins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AuditLogDto> failedLogins = auditService.getFailedLogins(page, size);
        return ResponseEntity.ok(GenericResponse.success(failedLogins, "Failed logins retrieved successfully"));
    }

    /**
     * Get successful logins
     */
    @GetMapping("/logs/successful-logins")
    public ResponseEntity<GenericResponse<Page<AuditLogDto>>> getSuccessfulLogins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AuditLogDto> successfulLogins = auditService.getSuccessfulLogins(page, size);
        return ResponseEntity.ok(GenericResponse.success(successfulLogins, "Successful logins retrieved successfully"));
    }

    /**
     * Get data access events
     */
    @GetMapping("/logs/data-access")
    public ResponseEntity<GenericResponse<Page<AuditLogDto>>> getDataAccessEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AuditLogDto> dataAccessEvents = auditService.getDataAccessEvents(page, size);
        return ResponseEntity.ok(GenericResponse.success(dataAccessEvents, "Data access events retrieved successfully"));
    }

    /**
     * Get audit statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<GenericResponse<Map<String, Object>>> getAuditStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        Map<String, Object> statistics = auditService.getAuditStatistics(startDate, endDate);
        return ResponseEntity.ok(GenericResponse.success(statistics, "Audit statistics retrieved successfully"));
    }

    /**
     * Get action statistics
     */
    @GetMapping("/statistics/actions")
    public ResponseEntity<GenericResponse<List<Map<String, Object>>>> getActionStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        List<Map<String, Object>> actionStats = auditService.getActionStatistics(startDate, endDate);
        return ResponseEntity.ok(GenericResponse.success(actionStats, "Action statistics retrieved successfully"));
    }

    /**
     * Get user activity statistics
     */
    @GetMapping("/statistics/user-activity")
    public ResponseEntity<GenericResponse<List<Map<String, Object>>>> getUserActivityStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        List<Map<String, Object>> userStats = auditService.getUserActivityStatistics(startDate, endDate);
        return ResponseEntity.ok(GenericResponse.success(userStats, "User activity statistics retrieved successfully"));
    }

    /**
     * Get events by level
     */
    @GetMapping("/statistics/events-by-level")
    public ResponseEntity<GenericResponse<List<Map<String, Object>>>> getEventsByLevel(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        List<Map<String, Object>> levelStats = auditService.getEventsByLevel(startDate, endDate);
        return ResponseEntity.ok(GenericResponse.success(levelStats, "Events by level retrieved successfully"));
    }

    /**
     * Get recent user activity
     */
    @GetMapping("/logs/user/{userId}/recent")
    public ResponseEntity<GenericResponse<List<AuditLogDto>>> getRecentUserActivity(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<AuditLogDto> recentActivity = auditService.getRecentUserActivity(userId, limit);
        return ResponseEntity.ok(GenericResponse.success(recentActivity, "Recent user activity retrieved successfully"));
    }

    /**
     * Find suspicious activity
     */
    @GetMapping("/logs/suspicious")
    public ResponseEntity<GenericResponse<List<Map<String, Object>>>> findSuspiciousActivity(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(defaultValue = "100") long threshold) {
        List<Map<String, Object>> suspiciousActivity = auditService.findSuspiciousActivity(startDate, endDate, threshold);
        return ResponseEntity.ok(GenericResponse.success(suspiciousActivity, "Suspicious activity retrieved successfully"));
    }

    /**
     * Export audit logs to CSV
     */
    @PostMapping("/logs/export/csv")
    public ResponseEntity<byte[]> exportAuditLogsToCsv(@Valid @RequestBody AuditQueryDto queryDto) {
        byte[] csvData = auditService.exportAuditLogsToCsv(queryDto);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "audit_logs.csv");
        
        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

    /**
     * Clean old audit logs
     */
    @DeleteMapping("/logs/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse<Void>> cleanOldAuditLogs(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beforeDate) {
        auditService.cleanOldAuditLogs(beforeDate);
        return ResponseEntity.ok(GenericResponse.success(null, "Old audit logs cleaned successfully"));
    }

    /**
     * Get audit log count by level
     */
    @GetMapping("/logs/count/level/{level}")
    public ResponseEntity<GenericResponse<Long>> getAuditLogCountByLevel(
            @PathVariable AuditLevel level,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        long count = auditService.getAuditLogCountByLevel(level, startDate, endDate);
        return ResponseEntity.ok(GenericResponse.success(count, "Audit log count retrieved successfully"));
    }

    /**
     * Create a custom audit log entry
     */
    @PostMapping("/logs")
    public ResponseEntity<GenericResponse<AuditLogDto>> createAuditLog(
            @Valid @RequestBody AuditLogDto auditLogDto,
            HttpServletRequest request) {
        
        // Add request information to the audit log
        auditLogDto.setIpAddress(getClientIpAddress(request));
        auditLogDto.setUserAgent(request.getHeader("User-Agent"));
        auditLogDto.setRequestUrl(request.getRequestURL().toString());
        auditLogDto.setHttpMethod(request.getMethod());
        
        AuditLogDto createdAuditLog = auditService.createAuditLog(auditLogDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GenericResponse.success(createdAuditLog, "Audit log created successfully"));
    }

    /**
     * Get all audit levels
     */
    @GetMapping("/levels")
    public ResponseEntity<GenericResponse<AuditLevel[]>> getAuditLevels() {
        AuditLevel[] levels = AuditLevel.values();
        return ResponseEntity.ok(GenericResponse.success(levels, "Audit levels retrieved successfully"));
    }

    // Helper method to get client IP address
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
} 