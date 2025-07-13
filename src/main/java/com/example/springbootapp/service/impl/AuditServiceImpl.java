package com.example.springbootapp.service.impl;

import com.example.springbootapp.dto.AuditLogDto;
import com.example.springbootapp.dto.AuditQueryDto;
import com.example.springbootapp.entity.AuditLog;
import com.example.springbootapp.entity.AuditLevel;
import com.example.springbootapp.exception.ResourceNotFoundException;
import com.example.springbootapp.repository.AuditLogRepository;
import com.example.springbootapp.service.AuditService;
import com.example.springbootapp.util.StreamUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public AuditLogDto createAuditLog(AuditLogDto auditLogDto) {
        AuditLog auditLog = convertToEntity(auditLogDto);
        AuditLog savedAuditLog = auditLogRepository.save(auditLog);
        return convertToDto(savedAuditLog);
    }

    @Override
    public AuditLogDto createAuditLog(String userId, String username, String action, String resourceType,
                                    String resourceId, String resourceName, String details, AuditLevel auditLevel) {
        AuditLog auditLog = AuditLog.builder()
                .userId(userId)
                .username(username)
                .action(action)
                .resourceType(resourceType)
                .resourceId(resourceId)
                .resourceName(resourceName)
                .details(details)
                .auditLevel(auditLevel)
                .build();

        AuditLog savedAuditLog = auditLogRepository.save(auditLog);
        return convertToDto(savedAuditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLogDto getAuditLogById(Long id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit log not found with id: " + id));
        return convertToDto(auditLog);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getAuditLogs(AuditQueryDto queryDto) {
        Pageable pageable = PageRequest.of(queryDto.getPage(), queryDto.getSize());
        
        if (queryDto.getSearchTerm() != null && !queryDto.getSearchTerm().trim().isEmpty()) {
            return auditLogRepository.searchByText(queryDto.getSearchTerm(), pageable)
                    .map(this::convertToDto);
        }
        
        return auditLogRepository.findWithFilters(
                queryDto.getUserId(),
                queryDto.getAction(),
                queryDto.getResourceType(),
                queryDto.getAuditLevel(),
                queryDto.getStartDate(),
                queryDto.getEndDate(),
                pageable
        ).map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getAuditLogsByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getAuditLogsByAction(String action, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findByActionOrderByCreatedAtDesc(action, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getAuditLogsByResourceType(String resourceType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findByResourceTypeOrderByCreatedAtDesc(resourceType, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getAuditLogsByLevel(AuditLevel auditLevel, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findByAuditLevelOrderByCreatedAtDesc(auditLevel, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDate, endDate, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> searchAuditLogs(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.searchByText(searchTerm, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getSecurityEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findSecurityEvents(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getFailedLogins(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findFailedLogins(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getSuccessfulLogins(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findSuccessfulLogins(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getDataAccessEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return auditLogRepository.findDataAccessEvents(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAuditStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> statistics = new HashMap<>();
        
        // Get action statistics
        List<Map<String, Object>> actionStats = getActionStatistics(startDate, endDate);
        statistics.put("actionStatistics", actionStats);
        
        // Get user activity statistics
        List<Map<String, Object>> userStats = getUserActivityStatistics(startDate, endDate);
        statistics.put("userActivityStatistics", userStats);
        
        // Get events by level
        List<Map<String, Object>> levelStats = getEventsByLevel(startDate, endDate);
        statistics.put("eventsByLevel", levelStats);
        
        // Get suspicious activity
        List<Map<String, Object>> suspiciousActivity = findSuspiciousActivity(startDate, endDate, 100);
        statistics.put("suspiciousActivity", suspiciousActivity);
        
        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getActionStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.getActionStatistics(startDate, endDate)
                .stream()
                .map(result -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("action", result[0]);
                    stat.put("count", result[1]);
                    return stat;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUserActivityStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.getUserActivityStatistics(startDate, endDate)
                .stream()
                .map(result -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("userId", result[0]);
                    stat.put("username", result[1]);
                    stat.put("activityCount", result[2]);
                    return stat;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getEventsByLevel(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.countByLevel(startDate, endDate)
                .stream()
                .map(result -> {
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("level", result[0]);
                    stat.put("count", result[1]);
                    return stat;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDto> getRecentUserActivity(String userId, int limit) {
        return auditLogRepository.findRecentUserActivity(userId, limit)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> findSuspiciousActivity(LocalDateTime startDate, LocalDateTime endDate, long threshold) {
        return auditLogRepository.findSuspiciousActivity(startDate, endDate, threshold)
                .stream()
                .map(result -> {
                    Map<String, Object> activity = new HashMap<>();
                    activity.put("userId", result[0]);
                    activity.put("username", result[1]);
                    activity.put("actionCount", result[2]);
                    return activity;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void logLoginSuccess(String userId, String username, String ipAddress, String userAgent, String sessionId) {
        createAuditLog(userId, username, "LOGIN_SUCCESS", "AUTH", null, "User Login", 
                      "User logged in successfully", AuditLevel.INFO);
    }

    @Override
    public void logLoginFailure(String username, String ipAddress, String userAgent, String reason) {
        createAuditLog("SYSTEM", username, "LOGIN_FAILED", "AUTH", null, "User Login", 
                      "Login failed: " + reason, AuditLevel.SECURITY);
    }

    @Override
    public void logLogout(String userId, String username, String sessionId) {
        createAuditLog(userId, username, "LOGOUT", "AUTH", null, "User Logout", 
                      "User logged out", AuditLevel.INFO);
    }

    @Override
    public void logDataAccess(String userId, String username, String action, String resourceType, 
                            String resourceId, String resourceName, String details) {
        AuditLevel level = determineAuditLevel(action);
        createAuditLog(userId, username, action, resourceType, resourceId, resourceName, details, level);
    }

    @Override
    public void logSecurityEvent(String userId, String username, String action, String details, AuditLevel level) {
        createAuditLog(userId, username, action, "SECURITY", null, "Security Event", details, level);
    }

    @Override
    public void logSystemEvent(String action, String details, AuditLevel level) {
        createAuditLog("SYSTEM", "SYSTEM", action, "SYSTEM", null, "System Event", details, level);
    }

    @Override
    public byte[] exportAuditLogsToCsv(AuditQueryDto queryDto) {
        // Implementation for CSV export
        // This would generate a CSV file with audit log data
        return new byte[0]; // Placeholder
    }

    @Override
    public void cleanOldAuditLogs(LocalDateTime beforeDate) {
        // Implementation for cleaning old audit logs
        // This would delete audit logs older than the specified date
    }

    @Override
    @Transactional(readOnly = true)
    public long getAuditLogCountByLevel(AuditLevel auditLevel, LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.countByLevel(startDate, endDate)
                .stream()
                .filter(result -> auditLevel.equals(result[0]))
                .mapToLong(result -> (Long) result[1])
                .findFirst()
                .orElse(0L);
    }

    // Helper methods
    private AuditLog convertToEntity(AuditLogDto dto) {
        return AuditLog.builder()
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .action(dto.getAction())
                .resourceType(dto.getResourceType())
                .resourceId(dto.getResourceId())
                .resourceName(dto.getResourceName())
                .details(dto.getDetails())
                .ipAddress(dto.getIpAddress())
                .userAgent(dto.getUserAgent())
                .requestUrl(dto.getRequestUrl())
                .httpMethod(dto.getHttpMethod())
                .statusCode(dto.getStatusCode())
                .executionTimeMs(dto.getExecutionTimeMs())
                .auditLevel(dto.getAuditLevel())
                .sessionId(dto.getSessionId())
                .build();
    }

    private AuditLogDto convertToDto(AuditLog entity) {
        return AuditLogDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .action(entity.getAction())
                .resourceType(entity.getResourceType())
                .resourceId(entity.getResourceId())
                .resourceName(entity.getResourceName())
                .details(entity.getDetails())
                .ipAddress(entity.getIpAddress())
                .userAgent(entity.getUserAgent())
                .requestUrl(entity.getRequestUrl())
                .httpMethod(entity.getHttpMethod())
                .statusCode(entity.getStatusCode())
                .executionTimeMs(entity.getExecutionTimeMs())
                .auditLevel(entity.getAuditLevel())
                .sessionId(entity.getSessionId())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private AuditLevel determineAuditLevel(String action) {
        return switch (action.toUpperCase()) {
            case "DELETE" -> AuditLevel.WARNING;
            case "UPDATE" -> AuditLevel.INFO;
            case "CREATE" -> AuditLevel.INFO;
            case "READ" -> AuditLevel.INFO;
            default -> AuditLevel.INFO;
        };
    }
} 