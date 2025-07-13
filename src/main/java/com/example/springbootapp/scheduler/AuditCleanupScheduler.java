package com.example.springbootapp.scheduler;

import com.example.springbootapp.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AuditCleanupScheduler.class);

    private final AuditService auditService;

    @Value("${audit.retention.days:90}")
    private int auditRetentionDays;

    public AuditCleanupScheduler(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Clean old audit logs every day at 2:00 AM
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanOldAuditLogs() {
        try {
            logger.info("Starting scheduled audit log cleanup...");
            
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(auditRetentionDays);
            auditService.cleanOldAuditLogs(cutoffDate);
            
            logger.info("Audit log cleanup completed successfully. Removed logs older than {} days", auditRetentionDays);
            
        } catch (Exception e) {
            logger.error("Error during audit log cleanup: {}", e.getMessage(), e);
        }
    }

    /**
     * Log audit cleanup statistics weekly on Sunday at 3:00 AM
     */
    @Scheduled(cron = "0 0 3 * * SUN")
    public void logAuditStatistics() {
        try {
            logger.info("Generating weekly audit statistics...");
            
            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusDays(7);
            
            var statistics = auditService.getAuditStatistics(startDate, endDate);
            
            logger.info("Weekly audit statistics: {}", statistics);
            
        } catch (Exception e) {
            logger.error("Error generating audit statistics: {}", e.getMessage(), e);
        }
    }
} 