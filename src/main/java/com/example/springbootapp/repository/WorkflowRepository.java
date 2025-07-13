package com.example.springbootapp.repository;

import com.example.springbootapp.entity.Workflow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Long> {
    
    // Find workflows by status
    List<Workflow> findByStatus(String status);
    
    // Find workflows by type
    List<Workflow> findByWorkflowType(String workflowType);
    
    // Find workflows by priority
    List<Workflow> findByPriority(String priority);
    
    // Find workflows by initiated user
    Page<Workflow> findByInitiatedBy_Id(Long userId, Pageable pageable);
    
    // Find workflows by assigned user
    Page<Workflow> findByAssignedTo_Id(Long userId, Pageable pageable);
    
    // Find active workflows
    List<Workflow> findByIsActiveTrue();
    
    // Find workflows by status and assigned user
    List<Workflow> findByStatusAndAssignedTo_Id(String status, Long userId);
    
    // Find workflows by type and status
    List<Workflow> findByWorkflowTypeAndStatus(String workflowType, String status);
    
    // Find workflows by priority and status
    List<Workflow> findByPriorityAndStatus(String priority, String status);
    
    // Find workflows by initiated user and status
    List<Workflow> findByInitiatedBy_IdAndStatus(Long userId, String status);
    
    // Find workflows by assigned user and status
    List<Workflow> findByAssignedTo_IdAndStatus(Long userId, String status);
    
    // Find workflows by current step
    List<Workflow> findByCurrentStep(Integer currentStep);
    
    // Find workflows by total steps
    List<Workflow> findByTotalSteps(Integer totalSteps);
    
    // Find workflows by current step and total steps
    List<Workflow> findByCurrentStepAndTotalSteps(Integer currentStep, Integer totalSteps);
    
    // Find workflows created after date
    List<Workflow> findByCreatedAtAfter(LocalDateTime date);
    
    // Find workflows updated after date
    List<Workflow> findByUpdatedAtAfter(LocalDateTime date);
    
    // Find workflows completed after date
    List<Workflow> findByCompletedAtAfter(LocalDateTime date);
    
    // Find workflows by name containing text
    List<Workflow> findByNameContainingIgnoreCase(String name);
    
    // Find workflows by description containing text
    List<Workflow> findByDescriptionContainingIgnoreCase(String description);
    
    // Find workflows by comments containing text
    List<Workflow> findByCommentsContainingIgnoreCase(String comments);
    
    // Find workflows by steps containing text
    List<Workflow> findByStepsContainingIgnoreCase(String steps);
    
    // Find workflows by multiple types
    @Query("SELECT w FROM Workflow w WHERE w.workflowType IN :workflowTypes")
    List<Workflow> findByWorkflowTypes(@Param("workflowTypes") List<String> workflowTypes);
    
    // Find workflows by multiple statuses
    @Query("SELECT w FROM Workflow w WHERE w.status IN :statuses")
    List<Workflow> findByStatuses(@Param("statuses") List<String> statuses);
    
    // Find workflows by multiple priorities
    @Query("SELECT w FROM Workflow w WHERE w.priority IN :priorities")
    List<Workflow> findByPriorities(@Param("priorities") List<String> priorities);
    
    // Find workflows created in date range
    @Query("SELECT w FROM Workflow w WHERE w.createdAt BETWEEN :startDate AND :endDate")
    List<Workflow> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    // Find workflows updated in date range
    @Query("SELECT w FROM Workflow w WHERE w.updatedAt BETWEEN :startDate AND :endDate")
    List<Workflow> findByUpdatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    // Find workflows completed in date range
    @Query("SELECT w FROM Workflow w WHERE w.completedAt BETWEEN :startDate AND :endDate")
    List<Workflow> findByCompletedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    // Find workflows by name and type
    List<Workflow> findByNameAndWorkflowType(String name, String workflowType);
    
    // Find workflows by name and status
    List<Workflow> findByNameAndStatus(String name, String status);
    
    // Find workflows by name and priority
    List<Workflow> findByNameAndPriority(String name, String priority);
    
    // Find workflows by description and type
    List<Workflow> findByDescriptionContainingIgnoreCaseAndWorkflowType(String description, String workflowType);
    
    // Find workflows by description and status
    List<Workflow> findByDescriptionContainingIgnoreCaseAndStatus(String description, String status);
    
    // Find workflows by description and priority
    List<Workflow> findByDescriptionContainingIgnoreCaseAndPriority(String description, String priority);
    
    // Count workflows by status
    long countByStatus(String status);
    
    // Count workflows by type
    long countByWorkflowType(String workflowType);
    
    // Count workflows by priority
    long countByPriority(String priority);
    
    // Count workflows by initiated user
    long countByInitiatedBy_Id(Long userId);
    
    // Count workflows by assigned user
    long countByAssignedTo_Id(Long userId);
    
    // Count active workflows
    long countByIsActiveTrue();
    
    // Count completed workflows
    long countByCompletedAtIsNotNull();
    
    // Find workflows by multiple criteria
    @Query("SELECT w FROM Workflow w WHERE " +
           "(:name IS NULL OR w.name LIKE %:name%) AND " +
           "(:workflowType IS NULL OR w.workflowType = :workflowType) AND " +
           "(:status IS NULL OR w.status = :status) AND " +
           "(:priority IS NULL OR w.priority = :priority) AND " +
           "(:isActive IS NULL OR w.isActive = :isActive)")
    Page<Workflow> findByMultipleCriteria(@Param("name") String name,
                                        @Param("workflowType") String workflowType,
                                        @Param("status") String status,
                                        @Param("priority") String priority,
                                        @Param("isActive") Boolean isActive,
                                        Pageable pageable);
    
    // Find workflows by current step range
    @Query("SELECT w FROM Workflow w WHERE w.currentStep BETWEEN :minStep AND :maxStep")
    List<Workflow> findByCurrentStepBetween(@Param("minStep") Integer minStep, @Param("maxStep") Integer maxStep);
    
    // Find workflows by total steps range
    @Query("SELECT w FROM Workflow w WHERE w.totalSteps BETWEEN :minSteps AND :maxSteps")
    List<Workflow> findByTotalStepsBetween(@Param("minSteps") Integer minSteps, @Param("maxSteps") Integer maxSteps);
    
    // Find workflows with progress percentage
    @Query("SELECT w FROM Workflow w WHERE (w.currentStep * 100.0 / w.totalSteps) >= :minProgress")
    List<Workflow> findByProgressGreaterThan(@Param("minProgress") Double minProgress);
    
    // Find workflows with progress percentage
    @Query("SELECT w FROM Workflow w WHERE (w.currentStep * 100.0 / w.totalSteps) <= :maxProgress")
    List<Workflow> findByProgressLessThan(@Param("maxProgress") Double maxProgress);
    
    // Find workflows by initiated user and type
    List<Workflow> findByInitiatedBy_IdAndWorkflowType(Long userId, String workflowType);
    
    // Find workflows by assigned user and type
    List<Workflow> findByAssignedTo_IdAndWorkflowType(Long userId, String workflowType);
    
    // Find workflows by initiated user and priority
    List<Workflow> findByInitiatedBy_IdAndPriority(Long userId, String priority);
    
    // Find workflows by assigned user and priority
    List<Workflow> findByAssignedTo_IdAndPriority(Long userId, String priority);
    
    // Find workflows by initiated user and active status
    List<Workflow> findByInitiatedBy_IdAndIsActive(Long userId, Boolean isActive);
    
    // Find workflows by assigned user and active status
    List<Workflow> findByAssignedTo_IdAndIsActive(Long userId, Boolean isActive);
    
    // Find workflows by name and initiated user
    List<Workflow> findByNameAndInitiatedBy_Id(String name, Long userId);
    
    // Find workflows by name and assigned user
    List<Workflow> findByNameAndAssignedTo_Id(String name, Long userId);
    
    // Find workflows by description and initiated user
    List<Workflow> findByDescriptionContainingIgnoreCaseAndInitiatedBy_Id(String description, Long userId);
    
    // Find workflows by description and assigned user
    List<Workflow> findByDescriptionContainingIgnoreCaseAndAssignedTo_Id(String description, Long userId);
    
    // Find workflows by comments and initiated user
    List<Workflow> findByCommentsContainingIgnoreCaseAndInitiatedBy_Id(String comments, Long userId);
    
    // Find workflows by comments and assigned user
    List<Workflow> findByCommentsContainingIgnoreCaseAndAssignedTo_Id(String comments, Long userId);
    
    // Find workflows by steps and initiated user
    List<Workflow> findByStepsContainingIgnoreCaseAndInitiatedBy_Id(String steps, Long userId);
    
    // Find workflows by steps and assigned user
    List<Workflow> findByStepsContainingIgnoreCaseAndAssignedTo_Id(String steps, Long userId);
    
    // Find workflows with comments
    @Query("SELECT w FROM Workflow w WHERE w.comments IS NOT NULL AND w.comments != ''")
    List<Workflow> findWorkflowsWithComments();
    
    // Find workflows without comments
    @Query("SELECT w FROM Workflow w WHERE w.comments IS NULL OR w.comments = ''")
    List<Workflow> findWorkflowsWithoutComments();
    
    // Find workflows by completed date and status
    @Query("SELECT w FROM Workflow w WHERE w.completedAt IS NOT NULL AND w.status = :status")
    List<Workflow> findByCompletedAtIsNotNullAndStatus(@Param("status") String status);
    
    // Find workflows by completed date and type
    @Query("SELECT w FROM Workflow w WHERE w.completedAt IS NOT NULL AND w.workflowType = :workflowType")
    List<Workflow> findByCompletedAtIsNotNullAndWorkflowType(@Param("workflowType") String workflowType);
    
    // Find workflows by completed date and priority
    @Query("SELECT w FROM Workflow w WHERE w.completedAt IS NOT NULL AND w.priority = :priority")
    List<Workflow> findByCompletedAtIsNotNullAndPriority(@Param("priority") String priority);
} 