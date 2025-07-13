package com.example.springbootapp.repository;

import com.example.springbootapp.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    
    // Find reports by status
    List<Report> findByStatus(String status);
    
    // Find reports by type
    List<Report> findByReportType(String reportType);
    
    // Find reports by format
    List<Report> findByFormat(String format);
    
    // Find reports by user
    Page<Report> findByRequestedBy_Id(Long userId, Pageable pageable);
    
    // Find scheduled reports
    List<Report> findByIsScheduledTrue();
    
    // Find active reports
    List<Report> findByIsActiveTrue();
    
    // Find reports by status and user
    List<Report> findByStatusAndRequestedBy_Id(String status, Long userId);
    
    // Find reports by type and status
    List<Report> findByReportTypeAndStatus(String reportType, String status);
    
    // Find reports by format and status
    List<Report> findByFormatAndStatus(String format, String status);
    
    // Find reports requested after date
    List<Report> findByRequestedAtAfter(LocalDateTime date);
    
    // Find reports completed after date
    List<Report> findByCompletedAtAfter(LocalDateTime date);
    
    // Find reports by name containing text
    List<Report> findByNameContainingIgnoreCase(String name);
    
    // Find reports by description containing text
    List<Report> findByDescriptionContainingIgnoreCase(String description);
    
    // Find reports by cron expression
    List<Report> findByCronExpression(String cronExpression);
    
    // Find reports with next execution before date
    @Query("SELECT r FROM Report r WHERE r.nextExecution <= :date AND r.isScheduled = true")
    List<Report> findScheduledReportsDueBefore(@Param("date") LocalDateTime date);
    
    // Find reports by multiple types
    @Query("SELECT r FROM Report r WHERE r.reportType IN :reportTypes")
    List<Report> findByReportTypes(@Param("reportTypes") List<String> reportTypes);
    
    // Find reports by multiple formats
    @Query("SELECT r FROM Report r WHERE r.format IN :formats")
    List<Report> findByFormats(@Param("formats") List<String> formats);
    
    // Find reports by multiple statuses
    @Query("SELECT r FROM Report r WHERE r.status IN :statuses")
    List<Report> findByStatuses(@Param("statuses") List<String> statuses);
    
    // Find reports requested in date range
    @Query("SELECT r FROM Report r WHERE r.requestedAt BETWEEN :startDate AND :endDate")
    List<Report> findByRequestedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    // Find reports completed in date range
    @Query("SELECT r FROM Report r WHERE r.completedAt BETWEEN :startDate AND :endDate")
    List<Report> findByCompletedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    // Find reports by file size range
    @Query("SELECT r FROM Report r WHERE r.fileSize BETWEEN :minSize AND :maxSize")
    List<Report> findByFileSizeBetween(@Param("minSize") Long minSize, @Param("maxSize") Long maxSize);
    
    // Find reports by user and type
    List<Report> findByRequestedBy_IdAndReportType(Long userId, String reportType);
    
    // Find reports by user and status
    List<Report> findByRequestedBy_IdAndStatus(Long userId, String status);
    
    // Find reports by user and format
    List<Report> findByRequestedBy_IdAndFormat(Long userId, String format);
    
    // Find reports by user and scheduled status
    List<Report> findByRequestedBy_IdAndIsScheduled(Long userId, Boolean isScheduled);
    
    // Find reports by user and active status
    List<Report> findByRequestedBy_IdAndIsActive(Long userId, Boolean isActive);
    
    // Count reports by status
    long countByStatus(String status);
    
    // Count reports by type
    long countByReportType(String reportType);
    
    // Count reports by format
    long countByFormat(String format);
    
    // Count reports by user
    long countByRequestedBy_Id(Long userId);
    
    // Count scheduled reports
    long countByIsScheduledTrue();
    
    // Count active reports
    long countByIsActiveTrue();
    
    // Find reports by name and type
    List<Report> findByNameAndReportType(String name, String reportType);
    
    // Find reports by name and format
    List<Report> findByNameAndFormat(String name, String format);
    
    // Find reports by name and status
    List<Report> findByNameAndStatus(String name, String status);
    
    // Find reports by description and type
    List<Report> findByDescriptionContainingIgnoreCaseAndReportType(String description, String reportType);
    
    // Find reports by description and status
    List<Report> findByDescriptionContainingIgnoreCaseAndStatus(String description, String status);
    
    // Find reports by multiple criteria
    @Query("SELECT r FROM Report r WHERE " +
           "(:name IS NULL OR r.name LIKE %:name%) AND " +
           "(:reportType IS NULL OR r.reportType = :reportType) AND " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:format IS NULL OR r.format = :format) AND " +
           "(:isScheduled IS NULL OR r.isScheduled = :isScheduled) AND " +
           "(:isActive IS NULL OR r.isActive = :isActive)")
    Page<Report> findByMultipleCriteria(@Param("name") String name,
                                      @Param("reportType") String reportType,
                                      @Param("status") String status,
                                      @Param("format") String format,
                                      @Param("isScheduled") Boolean isScheduled,
                                      @Param("isActive") Boolean isActive,
                                      Pageable pageable);
    
    // Find reports with file path
    @Query("SELECT r FROM Report r WHERE r.filePath IS NOT NULL")
    List<Report> findReportsWithFiles();
    
    // Find reports without file path
    @Query("SELECT r FROM Report r WHERE r.filePath IS NULL")
    List<Report> findReportsWithoutFiles();
    
    // Find reports by parameters containing text
    @Query("SELECT r FROM Report r WHERE r.parameters LIKE %:parameter%")
    List<Report> findByParametersContaining(@Param("parameter") String parameter);
    
    // Find reports by cron expression and active status
    List<Report> findByCronExpressionAndIsActive(String cronExpression, Boolean isActive);
    
    // Find reports by next execution and active status
    @Query("SELECT r FROM Report r WHERE r.nextExecution <= :date AND r.isActive = true")
    List<Report> findActiveReportsDueBefore(@Param("date") LocalDateTime date);
} 