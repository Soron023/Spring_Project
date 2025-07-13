package com.example.springbootapp.specification;

import com.example.springbootapp.entity.Report;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class ReportSpecification {

    public static Specification<Report> hasName(String name) {
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

    public static Specification<Report> hasReportType(String reportType) {
        return (root, query, criteriaBuilder) -> {
            if (reportType == null || reportType.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("reportType"), reportType);
        };
    }

    public static Specification<Report> hasReportTypes(List<String> reportTypes) {
        return (root, query, criteriaBuilder) -> {
            if (reportTypes == null || reportTypes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("reportType").in(reportTypes);
        };
    }

    public static Specification<Report> hasFormat(String format) {
        return (root, query, criteriaBuilder) -> {
            if (format == null || format.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("format"), format);
        };
    }

    public static Specification<Report> hasFormats(List<String> formats) {
        return (root, query, criteriaBuilder) -> {
            if (formats == null || formats.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("format").in(formats);
        };
    }

    public static Specification<Report> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Report> hasStatuses(List<String> statuses) {
        return (root, query, criteriaBuilder) -> {
            if (statuses == null || statuses.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("status").in(statuses);
        };
    }

    public static Specification<Report> requestedBy(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("requestedBy").get("id"), userId);
        };
    }

    public static Specification<Report> requestedAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("requestedAt"), date);
        };
    }

    public static Specification<Report> requestedBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("requestedAt"), date);
        };
    }

    public static Specification<Report> requestedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate == null) {
                return requestedBefore(endDate).toPredicate(root, query, criteriaBuilder);
            }
            if (endDate == null) {
                return requestedAfter(startDate).toPredicate(root, query, criteriaBuilder);
            }
            return criteriaBuilder.between(root.get("requestedAt"), startDate, endDate);
        };
    }

    public static Specification<Report> completedAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("completedAt"), date);
        };
    }

    public static Specification<Report> completedBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("completedAt"), date);
        };
    }

    public static Specification<Report> completedBetween(LocalDateTime startDate, LocalDateTime endDate) {
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

    public static Specification<Report> isScheduled(Boolean isScheduled) {
        return (root, query, criteriaBuilder) -> {
            if (isScheduled == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isScheduled"), isScheduled);
        };
    }

    public static Specification<Report> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<Report> hasCronExpression(String cronExpression) {
        return (root, query, criteriaBuilder) -> {
            if (cronExpression == null || cronExpression.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("cronExpression"), cronExpression);
        };
    }

    public static Specification<Report> hasNextExecutionBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("nextExecution"), date);
        };
    }

    public static Specification<Report> hasDescription(String description) {
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

    public static Specification<Report> hasParameters(String parameter) {
        return (root, query, criteriaBuilder) -> {
            if (parameter == null || parameter.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("parameters"), "%" + parameter + "%");
        };
    }

    public static Specification<Report> hasFileSizeGreaterThan(Long minSize) {
        return (root, query, criteriaBuilder) -> {
            if (minSize == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThan(root.get("fileSize"), minSize);
        };
    }

    public static Specification<Report> hasFileSizeLessThan(Long maxSize) {
        return (root, query, criteriaBuilder) -> {
            if (maxSize == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThan(root.get("fileSize"), maxSize);
        };
    }

    public static Specification<Report> hasFileSizeBetween(Long minSize, Long maxSize) {
        return (root, query, criteriaBuilder) -> {
            if (minSize == null && maxSize == null) {
                return criteriaBuilder.conjunction();
            }
            if (minSize == null) {
                return hasFileSizeLessThan(maxSize).toPredicate(root, query, criteriaBuilder);
            }
            if (maxSize == null) {
                return hasFileSizeGreaterThan(minSize).toPredicate(root, query, criteriaBuilder);
            }
            return criteriaBuilder.between(root.get("fileSize"), minSize, maxSize);
        };
    }

    public static Specification<Report> hasFilePath() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNotNull(root.get("filePath"));
        };
    }

    public static Specification<Report> hasNoFilePath() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNull(root.get("filePath"));
        };
    }

    public static Specification<Report> isCompleted() {
        return hasStatus("COMPLETED");
    }

    public static Specification<Report> isPending() {
        return hasStatus("PENDING");
    }

    public static Specification<Report> isFailed() {
        return hasStatus("FAILED");
    }

    public static Specification<Report> isInProgress() {
        return hasStatus("IN_PROGRESS");
    }

    public static Specification<Report> isSalesReport() {
        return hasReportType("SALES");
    }

    public static Specification<Report> isUserActivityReport() {
        return hasReportType("USER_ACTIVITY");
    }

    public static Specification<Report> isInventoryReport() {
        return hasReportType("INVENTORY");
    }

    public static Specification<Report> isAuditReport() {
        return hasReportType("AUDIT");
    }

    public static Specification<Report> isPDFFormat() {
        return hasFormat("PDF");
    }

    public static Specification<Report> isExcelFormat() {
        return hasFormat("EXCEL");
    }

    public static Specification<Report> isCSVFormat() {
        return hasFormat("CSV");
    }

    public static Specification<Report> requestedToday() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime tomorrow = today.plusDays(1);
            return criteriaBuilder.between(root.get("requestedAt"), today, tomorrow);
        };
    }

    public static Specification<Report> completedToday() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime tomorrow = today.plusDays(1);
            return criteriaBuilder.between(root.get("completedAt"), today, tomorrow);
        };
    }

    public static Specification<Report> dueForExecution() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(
                criteriaBuilder.equal(root.get("isScheduled"), true),
                criteriaBuilder.equal(root.get("isActive"), true),
                criteriaBuilder.lessThanOrEqualTo(root.get("nextExecution"), LocalDateTime.now())
            );
        };
    }

    public static Specification<Report> isOverdue() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(
                criteriaBuilder.equal(root.get("status"), "PENDING"),
                criteriaBuilder.lessThan(root.get("requestedAt"), LocalDateTime.now().minusDays(1))
            );
        };
    }
} 