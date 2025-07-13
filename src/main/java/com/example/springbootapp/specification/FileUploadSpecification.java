package com.example.springbootapp.specification;

import com.example.springbootapp.entity.FileUpload;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class FileUploadSpecification {

    public static Specification<FileUpload> hasFileName(String fileName) {
        return (root, query, criteriaBuilder) -> {
            if (fileName == null || fileName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("originalFileName")),
                "%" + fileName.toLowerCase() + "%"
            );
        };
    }

    public static Specification<FileUpload> hasFileType(String fileType) {
        return (root, query, criteriaBuilder) -> {
            if (fileType == null || fileType.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("fileType"), fileType);
        };
    }

    public static Specification<FileUpload> hasFileTypes(List<String> fileTypes) {
        return (root, query, criteriaBuilder) -> {
            if (fileTypes == null || fileTypes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("fileType").in(fileTypes);
        };
    }

    public static Specification<FileUpload> isPublic(Boolean isPublic) {
        return (root, query, criteriaBuilder) -> {
            if (isPublic == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isPublic"), isPublic);
        };
    }

    public static Specification<FileUpload> uploadedBy(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("uploadedBy").get("id"), userId);
        };
    }

    public static Specification<FileUpload> uploadedAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("uploadedAt"), date);
        };
    }

    public static Specification<FileUpload> uploadedBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("uploadedAt"), date);
        };
    }

    public static Specification<FileUpload> uploadedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate == null) {
                return uploadedBefore(endDate).toPredicate(root, query, criteriaBuilder);
            }
            if (endDate == null) {
                return uploadedAfter(startDate).toPredicate(root, query, criteriaBuilder);
            }
            return criteriaBuilder.between(root.get("uploadedAt"), startDate, endDate);
        };
    }

    public static Specification<FileUpload> hasFileSizeGreaterThan(Long minSize) {
        return (root, query, criteriaBuilder) -> {
            if (minSize == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThan(root.get("fileSize"), minSize);
        };
    }

    public static Specification<FileUpload> hasFileSizeLessThan(Long maxSize) {
        return (root, query, criteriaBuilder) -> {
            if (maxSize == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThan(root.get("fileSize"), maxSize);
        };
    }

    public static Specification<FileUpload> hasFileSizeBetween(Long minSize, Long maxSize) {
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

    public static Specification<FileUpload> hasDescription(String description) {
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

    public static Specification<FileUpload> hasUploadDir(String uploadDir) {
        return (root, query, criteriaBuilder) -> {
            if (uploadDir == null || uploadDir.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("uploadDir"), uploadDir);
        };
    }

    public static Specification<FileUpload> hasChecksum(String checksum) {
        return (root, query, criteriaBuilder) -> {
            if (checksum == null || checksum.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("checksum"), checksum);
        };
    }

    public static Specification<FileUpload> isDuplicate() {
        return (root, query, criteriaBuilder) -> {
            query.groupBy(root.get("checksum"));
            query.having(criteriaBuilder.greaterThan(criteriaBuilder.count(root), 1L));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<FileUpload> uploadedToday() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime tomorrow = today.plusDays(1);
            return criteriaBuilder.between(root.get("uploadedAt"), today, tomorrow);
        };
    }

    public static Specification<FileUpload> uploadedThisWeek() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime weekStart = LocalDateTime.now()
                .with(java.time.DayOfWeek.MONDAY)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime weekEnd = weekStart.plusWeeks(1);
            return criteriaBuilder.between(root.get("uploadedAt"), weekStart, weekEnd);
        };
    }

    public static Specification<FileUpload> uploadedThisMonth() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime monthStart = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime monthEnd = monthStart.plusMonths(1);
            return criteriaBuilder.between(root.get("uploadedAt"), monthStart, monthEnd);
        };
    }

    public static Specification<FileUpload> isImageFile() {
        return (root, query, criteriaBuilder) -> {
            List<String> imageTypes = List.of(
                "image/jpeg", "image/png", "image/gif", "image/bmp", 
                "image/webp", "image/svg+xml", "image/tiff"
            );
            return root.get("fileType").in(imageTypes);
        };
    }

    public static Specification<FileUpload> isDocumentFile() {
        return (root, query, criteriaBuilder) -> {
            List<String> documentTypes = List.of(
                "application/pdf", "application/msword", 
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "text/plain", "text/html"
            );
            return root.get("fileType").in(documentTypes);
        };
    }

    public static Specification<FileUpload> isVideoFile() {
        return (root, query, criteriaBuilder) -> {
            List<String> videoTypes = List.of(
                "video/mp4", "video/avi", "video/mov", "video/wmv", 
                "video/flv", "video/webm", "video/mkv"
            );
            return root.get("fileType").in(videoTypes);
        };
    }

    public static Specification<FileUpload> isAudioFile() {
        return (root, query, criteriaBuilder) -> {
            List<String> audioTypes = List.of(
                "audio/mpeg", "audio/wav", "audio/ogg", "audio/mp4", 
                "audio/aac", "audio/flac"
            );
            return root.get("fileType").in(audioTypes);
        };
    }
} 