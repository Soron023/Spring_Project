package com.example.springbootapp.specification;

import com.example.springbootapp.entity.EmailTemplate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class EmailTemplateSpecification {

    public static Specification<EmailTemplate> hasTemplateCode(String templateCode) {
        return (root, query, criteriaBuilder) -> {
            if (templateCode == null || templateCode.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("templateCode"), templateCode);
        };
    }

    public static Specification<EmailTemplate> hasTemplateCodes(List<String> templateCodes) {
        return (root, query, criteriaBuilder) -> {
            if (templateCodes == null || templateCodes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("templateCode").in(templateCodes);
        };
    }

    public static Specification<EmailTemplate> hasName(String name) {
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

    public static Specification<EmailTemplate> hasLanguage(String language) {
        return (root, query, criteriaBuilder) -> {
            if (language == null || language.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("language"), language);
        };
    }

    public static Specification<EmailTemplate> hasLanguages(List<String> languages) {
        return (root, query, criteriaBuilder) -> {
            if (languages == null || languages.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("language").in(languages);
        };
    }

    public static Specification<EmailTemplate> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            if (isActive == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<EmailTemplate> hasVersion(String version) {
        return (root, query, criteriaBuilder) -> {
            if (version == null || version.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("version"), version);
        };
    }

    public static Specification<EmailTemplate> createdBy(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("createdBy").get("id"), userId);
        };
    }

    public static Specification<EmailTemplate> createdAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<EmailTemplate> createdBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    public static Specification<EmailTemplate> createdBetween(LocalDateTime startDate, LocalDateTime endDate) {
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

    public static Specification<EmailTemplate> updatedAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("updatedAt"), date);
        };
    }

    public static Specification<EmailTemplate> updatedBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("updatedAt"), date);
        };
    }

    public static Specification<EmailTemplate> updatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
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

    public static Specification<EmailTemplate> hasDescription(String description) {
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

    public static Specification<EmailTemplate> hasSubject(String subject) {
        return (root, query, criteriaBuilder) -> {
            if (subject == null || subject.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("subject")),
                "%" + subject.toLowerCase() + "%"
            );
        };
    }

    public static Specification<EmailTemplate> hasBody(String body) {
        return (root, query, criteriaBuilder) -> {
            if (body == null || body.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("body")),
                "%" + body.toLowerCase() + "%"
            );
        };
    }

    public static Specification<EmailTemplate> hasVariable(String variable) {
        return (root, query, criteriaBuilder) -> {
            if (variable == null || variable.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%{{" + variable + "}}%";
            return criteriaBuilder.like(root.get("body"), pattern);
        };
    }

    public static Specification<EmailTemplate> isWelcomeTemplate() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("templateCode")), "%welcome%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%welcome%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("subject")), "%welcome%")
            );
        };
    }

    public static Specification<EmailTemplate> isOrderTemplate() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("templateCode")), "%order%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%order%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("subject")), "%order%")
            );
        };
    }

    public static Specification<EmailTemplate> isPasswordResetTemplate() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("templateCode")), "%password%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("templateCode")), "%reset%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%password%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%reset%")
            );
        };
    }

    public static Specification<EmailTemplate> isNotificationTemplate() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("templateCode")), "%notification%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("templateCode")), "%alert%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%notification%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%alert%")
            );
        };
    }

    public static Specification<EmailTemplate> hasLatestVersion() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("version")));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<EmailTemplate> isDuplicateTemplateCode() {
        return (root, query, criteriaBuilder) -> {
            query.groupBy(root.get("templateCode"));
            query.having(criteriaBuilder.greaterThan(criteriaBuilder.count(root), 1L));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<EmailTemplate> createdToday() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime tomorrow = today.plusDays(1);
            return criteriaBuilder.between(root.get("createdAt"), today, tomorrow);
        };
    }

    public static Specification<EmailTemplate> updatedToday() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime tomorrow = today.plusDays(1);
            return criteriaBuilder.between(root.get("updatedAt"), today, tomorrow);
        };
    }

    public static Specification<EmailTemplate> isEnglishTemplate() {
        return hasLanguage("en");
    }

    public static Specification<EmailTemplate> isSpanishTemplate() {
        return hasLanguage("es");
    }

    public static Specification<EmailTemplate> isFrenchTemplate() {
        return hasLanguage("fr");
    }

    public static Specification<EmailTemplate> isGermanTemplate() {
        return hasLanguage("de");
    }

    public static Specification<EmailTemplate> isChineseTemplate() {
        return hasLanguage("zh");
    }

    public static Specification<EmailTemplate> isJapaneseTemplate() {
        return hasLanguage("ja");
    }
} 