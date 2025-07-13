package com.example.springbootapp.service;

import com.example.springbootapp.entity.EmailTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmailTemplateService {
    
    // Basic CRUD operations
    EmailTemplate save(EmailTemplate emailTemplate);
    Optional<EmailTemplate> findById(Long id);
    List<EmailTemplate> findAll();
    void deleteById(Long id);
    
    // Repository-based queries
    Optional<EmailTemplate> findByTemplateCode(String templateCode);
    List<EmailTemplate> findByIsActiveTrue();
    List<EmailTemplate> findByLanguage(String language);
    List<EmailTemplate> findByLanguageAndIsActive(String language, Boolean isActive);
    List<EmailTemplate> findByNameContainingIgnoreCase(String name);
    List<EmailTemplate> findByDescriptionContainingIgnoreCase(String description);
    List<EmailTemplate> findByVersion(String version);
    Page<EmailTemplate> findByCreatedBy_Id(Long userId, Pageable pageable);
    Optional<EmailTemplate> findByTemplateCodeAndLanguage(String templateCode, String language);
    List<EmailTemplate> findByTemplateCodeAndIsActive(String templateCode, Boolean isActive);
    List<EmailTemplate> findLatestVersionByTemplateCode(String templateCode);
    List<EmailTemplate> findByLanguages(List<String> languages);
    List<EmailTemplate> findByTemplateCodes(List<String> templateCodes);
    List<EmailTemplate> findActiveTemplatesByLanguage(String language);
    List<EmailTemplate> findByCreatedAtAfter(LocalDateTime date);
    List<EmailTemplate> findByUpdatedAtAfter(LocalDateTime date);
    long countByLanguage(String language);
    long countByIsActiveTrue();
    long countByCreatedBy_Id(Long userId);
    List<EmailTemplate> findByNameAndLanguage(String name, String language);
    List<EmailTemplate> findByNameContainingIgnoreCaseAndIsActive(String name, Boolean isActive);
    List<EmailTemplate> findByDescriptionContainingIgnoreCaseAndLanguage(String description, String language);
    List<EmailTemplate> findByVersionAndIsActive(String version, Boolean isActive);
    Page<EmailTemplate> findByMultipleCriteria(String name, String language, Boolean isActive, Pageable pageable);
    List<String> findDuplicateTemplateCodes();
    List<EmailTemplate> findBySubjectContainingIgnoreCase(String subject);
    List<EmailTemplate> findByBodyContainingIgnoreCase(String body);
    
    // Specification-based queries
    List<EmailTemplate> findBySpecification(Specification<EmailTemplate> spec);
    Page<EmailTemplate> findBySpecification(Specification<EmailTemplate> spec, Pageable pageable);
    
    // Advanced search methods using specifications
    List<EmailTemplate> searchTemplates(String templateCode, String name, String language, 
                                       Boolean isActive, String version, Long createdBy);
    
    // Template category methods
    List<EmailTemplate> findWelcomeTemplates();
    List<EmailTemplate> findOrderTemplates();
    List<EmailTemplate> findPasswordResetTemplates();
    List<EmailTemplate> findNotificationTemplates();
    
    // Language-specific methods
    List<EmailTemplate> findEnglishTemplates();
    List<EmailTemplate> findSpanishTemplates();
    List<EmailTemplate> findFrenchTemplates();
    List<EmailTemplate> findGermanTemplates();
    List<EmailTemplate> findChineseTemplates();
    List<EmailTemplate> findJapaneseTemplates();
    
    // Version management
    List<EmailTemplate> findLatestVersions();
    List<EmailTemplate> findTemplatesByVersion(String version);
    List<EmailTemplate> findActiveTemplatesByVersion(String version);
    
    // Time-based queries
    List<EmailTemplate> findTemplatesCreatedToday();
    List<EmailTemplate> findTemplatesUpdatedToday();
    List<EmailTemplate> findTemplatesCreatedThisWeek();
    List<EmailTemplate> findTemplatesUpdatedThisWeek();
    
    // Template management operations
    EmailTemplate createTemplate(EmailTemplate template, Long createdBy);
    EmailTemplate updateTemplate(Long id, EmailTemplate template, Long updatedBy);
    boolean activateTemplate(Long id);
    boolean deactivateTemplate(Long id);
    boolean deleteTemplate(Long id, Long userId);
    
    // Template rendering
    String renderTemplate(String templateCode, Map<String, Object> variables);
    String renderTemplate(String templateCode, String language, Map<String, Object> variables);
    String renderTemplateById(Long templateId, Map<String, Object> variables);
    
    // Template validation
    boolean isValidTemplateCode(String templateCode);
    boolean isValidLanguage(String language);
    boolean isValidVersion(String version);
    List<String> validateTemplateVariables(String body);
    
    // Template statistics
    long getTotalTemplateCount();
    long getActiveTemplateCount();
    long getInactiveTemplateCount();
    long getTemplateCountByLanguage(String language);
    long getTemplateCountByUser(Long userId);
    long getTemplateCountByVersion(String version);
    
    // Template duplication and versioning
    EmailTemplate duplicateTemplate(Long templateId, String newVersion, Long createdBy);
    EmailTemplate createNewVersion(Long templateId, String newVersion, Long createdBy);
    List<EmailTemplate> getTemplateHistory(String templateCode);
    
    // Template import/export
    String exportTemplateToJson(Long templateId);
    EmailTemplate importTemplateFromJson(String jsonTemplate, Long createdBy);
    
    // Template variables
    List<String> getTemplateVariables(String templateCode);
    List<String> getTemplateVariablesById(Long templateId);
    boolean validateTemplateVariables(String templateCode, Map<String, Object> variables);
    
    // Template categories
    List<String> getAvailableLanguages();
    List<String> getAvailableVersions();
    List<String> getTemplateCategories();
} 