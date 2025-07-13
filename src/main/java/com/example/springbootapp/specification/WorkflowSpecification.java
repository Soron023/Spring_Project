package com.example.springbootapp.specification;

import com.example.springbootapp.entity.Workflow;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class WorkflowSpecification {

    public static Specification<Workflow> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + name.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Workflow> hasWorkflowType(String workflowType) {
        return (root, query, criteriaBuilder) -> {
            if (workflowType == null || workflowType.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("workflowType"), workflowType);
        };
    }

    public static Specification<Workflow> hasWorkflowTypes(List<String> workflowTypes) {
        return (root, query, criteriaBuilder) -> {
            if (workflowTypes == null || workflowTypes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("workflowType").in(workflowTypes);
        };
    }

    public static Specification<Workflow> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Workflow> hasStatuses(List<String> statuses) {
        return (root, query, criteriaBuilder) -> {
            if (statuses == null || statuses.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("status").in(statuses);
        };
    }

    public static Specification<Workflow> hasPriority(String priority) {
        return (root, query, criteriaBuilder) -> {
            if (priority == null || priority.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("priority"), priority);
        };
    }

    public static Specification<Workflow> hasPriorities(List<String> priorities) {
        return (root, query, criteriaBuilder) -> {
            if (priorities == null || priorities.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("priority").in(priorities);
        };
    }

    public static Specification<Workflow> initiatedBy(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("initiatedBy").get("id"), userId);
        };
    }

    public static Specification<Workflow> assignedTo(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("assignedTo").get("id"), userId);
        };
    }

    public static Specification<Workflow> createdAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<Workflow> createdBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<Workflow> createdBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate == null) {
                return createdBefore(endDate).toPredicate(root, query, criteriaBuilder);
            }
            if (endDate == null) {
                return createdAfter(startDate).toPredicate(root, query, criteriaBuilder);
            }
            return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
        };
    }

    public static Specification<Workflow> updatedAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), date);
        };
    }

    public static Specification<Workflow> updatedBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), date);
        };
    }

    public static Specification<Workflow> updatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate == null) {
                return updatedBefore(endDate).toPredicate(root, query, criteriaBuilder);
            }
            if (endDate == null) {
                return updatedAfter(startDate).toPredicate(root, query, criteriaBuilder);
            }
            return criteriaBuilder.between(root.get("updatedAt"), startDate, endDate);
        };
    }

    public static Specification<Workflow> completedAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("completedAt"), date);
        };
    }

    public static Specification<Workflow> completedBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("completedAt"), date);
        };
    }

    public static Specification<Workflow> completedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate == null) {
                return completedBefore(endDate).toPredicate(root, query, criteriaBuilder);
            }
            if (endDate == null) {
                return completedAfter(startDate).toPredicate(root, query, criteriaBuilder);
            }
            return criteriaBuilder.between(root.get("completedAt"), startDate, endDate);
        };
    }

    public static Specification<Workflow> hasDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null || description.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("description")),
                "%" + description.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Workflow> hasComments(String comments) {
        return (root, query, criteriaBuilder) -> {
            if (comments == null || comments.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("comments")),
                "%" + comments.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Workflow> hasSteps(String step) {
        return (root, query, criteriaBuilder) -> {
            if (step == null || step.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("steps")),
                "%" + step.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Workflow> hasCurrentStep(Integer currentStep) {
        return (root, query, criteriaBuilder) -> {
            if (currentStep == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("currentStep"), currentStep);
        };
    }

    public static Specification<Workflow> hasTotalSteps(Integer totalSteps) {
        return (root, query, criteriaBuilder) -> {
            if (totalSteps == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("totalSteps"), totalSteps);
        };
    }

    public static Specification<Workflow> hasCurrentStepBetween(Integer minStep, Integer maxStep) {
        return (root, query, criteriaBuilder) -> {
            if (minStep == null && maxStep == null) {
                return criteriaBuilder.conjunction();
            }
            if (minStep == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("currentStep"), maxStep);
            }
            if (maxStep == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("currentStep"), minStep);
            }
            return criteriaBuilder.between(root.get("currentStep"), minStep, maxStep);
        };
    }

    public static Specification<Workflow> hasTotalStepsBetween(Integer minSteps, Integer maxSteps) {
        return (root, query, criteriaBuilder) -> {
            if (minSteps == null && maxSteps == null) {
                return criteriaBuilder.conjunction();
            }
            if (minSteps == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("totalSteps"), maxSteps);
            }
            if (maxSteps == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("totalSteps"), minSteps);
            }
            return criteriaBuilder.between(root.get("totalSteps"), minSteps, maxSteps);
        };
    }

    public static Specification<Workflow> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<Workflow> hasProgressGreaterThan(Double minProgress) {
        return (root, query, criteriaBuilder) -> {
            if (minProgress == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(
                criteriaBuilder.toDouble(criteriaBuilder.quot(root.get("currentStep"), root.get("totalSteps"))),
                criteriaBuilder.literal(minProgress / 100.0)
            );
        };
    }

    public static Specification<Workflow> hasProgressLessThan(Double maxProgress) {
        return (root, query, criteriaBuilder) -> {
            if (maxProgress == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(
                criteriaBuilder.toDouble(criteriaBuilder.quot(root.get("currentStep"), root.get("totalSteps"))),
                criteriaBuilder.literal(maxProgress / 100.0)
            );
        };
    }

    public static Specification<Workflow> hasProgressBetween(Double minProgress, Double maxProgress) {
        return (root, query, criteriaBuilder) -> {
            if (minProgress == null && maxProgress == null) {
                return criteriaBuilder.conjunction();
            }
            if (minProgress == null) {
                return hasProgressLessThan(maxProgress).toPredicate(root, query, criteriaBuilder);
            }
            if (maxProgress == null) {
                return hasProgressGreaterThan(minProgress).toPredicate(root, query, criteriaBuilder);
            }
            return criteriaBuilder.and(
                criteriaBuilder.greaterThanOrEqualTo(
                    criteriaBuilder.toDouble(criteriaBuilder.quot(root.get("currentStep"), root.get("totalSteps"))),
                    criteriaBuilder.literal(minProgress / 100.0)
                ),
                criteriaBuilder.lessThanOrEqualTo(
                    criteriaBuilder.toDouble(criteriaBuilder.quot(root.get("currentStep"), root.get("totalSteps"))),
                    criteriaBuilder.literal(maxProgress / 100.0)
                )
            );
        };
    }

    public static Specification<Workflow> hasCompletedStatus() {
        return hasStatus("COMPLETED");
    }

    public static Specification<Workflow> isPending() {
        return hasStatus("PENDING");
    }

    public static Specification<Workflow> isInProgress() {
        return hasStatus("IN_PROGRESS");
    }

    public static Specification<Workflow> isCancelled() {
        return hasStatus("CANCELLED");
    }

    public static Specification<Workflow> isApprovalWorkflow() {
        return hasWorkflowType("APPROVAL");
    }

    public static Specification<Workflow> isRegistrationWorkflow() {
        return hasWorkflowType("REGISTRATION");
    }

    public static Specification<Workflow> isOrderWorkflow() {
        return hasWorkflowType("ORDER");
    }

    public static Specification<Workflow> isHighPriority() {
        return hasPriority("HIGH");
    }

    public static Specification<Workflow> isMediumPriority() {
        return hasPriority("MEDIUM");
    }

    public static Specification<Workflow> isLowPriority() {
        return hasPriority("LOW");
    }

    public static Specification<Workflow> hasComments() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(
                criteriaBuilder.isNotNull(root.get("comments")),
                criteriaBuilder.notEqual(root.get("comments"), "")
            );
        };
    }

    public static Specification<Workflow> hasNoComments() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
                criteriaBuilder.isNull(root.get("comments")),
                criteriaBuilder.equal(root.get("comments"), "")
            );
        };
    }

    public static Specification<Workflow> isCompleted() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNotNull(root.get("completedAt"));
        };
    }

    public static Specification<Workflow> isNotCompleted() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNull(root.get("completedAt"));
        };
    }

    public static Specification<Workflow> createdToday() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime tomorrow = today.plusDays(1);
            return criteriaBuilder.between(root.get("createdAt"), today, tomorrow);
        };
    }

    public static Specification<Workflow> updatedToday() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime tomorrow = today.plusDays(1);
            return criteriaBuilder.between(root.get("updatedAt"), today, tomorrow);
        };
    }

    public static Specification<Workflow> completedToday() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime tomorrow = today.plusDays(1);
            return criteriaBuilder.between(root.get("completedAt"), today, tomorrow);
        };
    }

    public static Specification<Workflow> isOverdue() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(
                criteriaBuilder.equal(root.get("status"), "PENDING"),
                criteriaBuilder.lessThan(root.get("createdAt"), LocalDateTime.now().minusDays(7))
            );
        };
    }

    public static Specification<Workflow> isStuck() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(
                criteriaBuilder.equal(root.get("status"), "IN_PROGRESS"),
                criteriaBuilder.lessThan(root.get("updatedAt"), LocalDateTime.now().minusDays(3))
            );
        };
    }
} 