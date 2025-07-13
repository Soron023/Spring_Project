package com.example.springbootapp.repository;

import com.example.springbootapp.entity.AuditLog;
import com.example.springbootapp.entity.AuditLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Find by user ID
    Page<AuditLog> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    // Find by username
    Page<AuditLog> findByUsernameOrderByCreatedAtDesc(String username, Pageable pageable);

    // Find by action
    Page<AuditLog> findByActionOrderByCreatedAtDesc(String action, Pageable pageable);

    // Find by resource type
    Page<AuditLog> findByResourceTypeOrderByCreatedAtDesc(String resourceType, Pageable pageable);

    // Find by audit level
    Page<AuditLog> findByAuditLevelOrderByCreatedAtDesc(AuditLevel auditLevel, Pageable pageable);

    // Find by date range
    Page<AuditLog> findByCreatedAtBetweenOrderByCreatedAtDesc(
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Find by user and date range
    Page<AuditLog> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            String userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Find by action and date range
    Page<AuditLog> findByActionAndCreatedAtBetweenOrderByCreatedAtDesc(
            String action, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Find by audit level and date range
    Page<AuditLog> findByAuditLevelAndCreatedAtBetweenOrderByCreatedAtDesc(
            AuditLevel auditLevel, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Find by IP address
    Page<AuditLog> findByIpAddressOrderByCreatedAtDesc(String ipAddress, Pageable pageable);

    // Find by session ID
    Page<AuditLog> findBySessionIdOrderByCreatedAtDesc(String sessionId, Pageable pageable);

    // Find by HTTP method
    Page<AuditLog> findByHttpMethodOrderByCreatedAtDesc(String httpMethod, Pageable pageable);

    // Find by status code
    Page<AuditLog> findByStatusCodeOrderByCreatedAtDesc(Integer statusCode, Pageable pageable);

    // Find by resource ID
    Page<AuditLog> findByResourceIdOrderByCreatedAtDesc(String resourceId, Pageable pageable);

    // Complex queries with multiple criteria
    @Query("SELECT a FROM AuditLog a WHERE " +
           "(:userId IS NULL OR a.userId = :userId) AND " +
           "(:action IS NULL OR a.action = :action) AND " +
           "(:resourceType IS NULL OR a.resourceType = :resourceType) AND " +
           "(:auditLevel IS NULL OR a.auditLevel = :auditLevel) AND " +
           "(:startDate IS NULL OR a.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR a.createdAt <= :endDate) " +
           "ORDER BY a.createdAt DESC")
    Page<AuditLog> findWithFilters(
            @Param("userId") String userId,
            @Param("action") String action,
            @Param("resourceType") String resourceType,
            @Param("auditLevel") AuditLevel auditLevel,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    // Search by text in details
    @Query("SELECT a FROM AuditLog a WHERE " +
           "LOWER(a.details) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.resourceName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY a.createdAt DESC")
    Page<AuditLog> searchByText(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Get audit statistics
    @Query("SELECT a.action, COUNT(a) FROM AuditLog a " +
           "WHERE a.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY a.action ORDER BY COUNT(a) DESC")
    List<Object[]> getActionStatistics(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Get user activity statistics
    @Query("SELECT a.userId, a.username, COUNT(a) FROM AuditLog a " +
           "WHERE a.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY a.userId, a.username ORDER BY COUNT(a) DESC")
    List<Object[]> getUserActivityStatistics(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Get security events
    @Query("SELECT a FROM AuditLog a WHERE a.auditLevel IN ('SECURITY', 'CRITICAL') " +
           "ORDER BY a.createdAt DESC")
    Page<AuditLog> findSecurityEvents(Pageable pageable);

    // Get failed login attempts
    @Query("SELECT a FROM AuditLog a WHERE a.action = 'LOGIN_FAILED' " +
           "ORDER BY a.createdAt DESC")
    Page<AuditLog> findFailedLogins(Pageable pageable);

    // Get successful logins
    @Query("SELECT a FROM AuditLog a WHERE a.action = 'LOGIN_SUCCESS' " +
           "ORDER BY a.createdAt DESC")
    Page<AuditLog> findSuccessfulLogins(Pageable pageable);

    // Get data access events
    @Query("SELECT a FROM AuditLog a WHERE a.action IN ('READ', 'CREATE', 'UPDATE', 'DELETE') " +
           "ORDER BY a.createdAt DESC")
    Page<AuditLog> findDataAccessEvents(Pageable pageable);

    // Count events by level for a specific time period
    @Query("SELECT a.auditLevel, COUNT(a) FROM AuditLog a " +
           "WHERE a.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY a.auditLevel")
    List<Object[]> countByLevel(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Find recent activity for a specific user
    @Query("SELECT a FROM AuditLog a WHERE a.userId = :userId " +
           "ORDER BY a.createdAt DESC LIMIT :limit")
    List<AuditLog> findRecentUserActivity(
            @Param("userId") String userId,
            @Param("limit") int limit);

    // Find suspicious activity (high frequency of actions)
    @Query("SELECT a.userId, a.username, COUNT(a) as actionCount FROM AuditLog a " +
           "WHERE a.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY a.userId, a.username " +
           "HAVING COUNT(a) > :threshold " +
           "ORDER BY actionCount DESC")
    List<Object[]> findSuspiciousActivity(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("threshold") long threshold);
} 