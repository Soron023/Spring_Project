package com.example.springbootapp.repository;

import com.example.springbootapp.entity.EmailTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long>, JpaSpecificationExecutor<EmailTemplate> {
    
    // Find template by code
    Optional<EmailTemplate> findByTemplateCode(String templateCode);
    
    // Find active templates
    List<EmailTemplate> findByIsActiveTrue();
    
    // Find templates by language
    List<EmailTemplate> findByLanguage(String language);
    
    // Find templates by language and active status
    List<EmailTemplate> findByLanguageAndIsActive(String language, Boolean isActive);
    
    // Find templates by name containing text
    List<EmailTemplate> findByNameContainingIgnoreCase(String name);
    
    // Find templates by description containing text
    List<EmailTemplate> findByDescriptionContainingIgnoreCase(String description);
    
    // Find templates by version
    List<EmailTemplate> findByVersion(String version);
    
    // Find templates created by user
    Page<EmailTemplate> findByCreatedBy_Id(Long userId, Pageable pageable);
    
    // Find templates by code and language
    Optional<EmailTemplate> findByTemplateCodeAndLanguage(String templateCode, String language);
    
    // Find templates by code and active status
    List<EmailTemplate> findByTemplateCodeAndIsActive(String templateCode, Boolean isActive);
    
    // Find latest version of template by code
    @Query("SELECT e FROM EmailTemplate e WHERE e.templateCode = :templateCode " +
           "ORDER BY e.version DESC")
    List<EmailTemplate> findLatestVersionByTemplateCode(@Param("templateCode") String templateCode);
    
    // Find templates by multiple languages
    @Query("SELECT e FROM EmailTemplate e WHERE e.language IN :languages")
    List<EmailTemplate> findByLanguages(@Param("languages") List<String> languages);
    
    // Find templates by multiple codes
    @Query("SELECT e FROM EmailTemplate e WHERE e.templateCode IN :templateCodes")
    List<EmailTemplate> findByTemplateCodes(@Param("templateCodes") List<String> templateCodes);
    
    // Find active templates by language
    @Query("SELECT e FROM EmailTemplate e WHERE e.language = :language AND e.isActive = true")
    List<EmailTemplate> findActiveTemplatesByLanguage(@Param("language") String language);
    
    // Find templates created after date
    @Query("SELECT e FROM EmailTemplate e WHERE e.createdAt >= :date")
    List<EmailTemplate> findByCreatedAtAfter(@Param("date") java.time.LocalDateTime date);
    
    // Find templates updated after date
    @Query("SELECT e FROM EmailTemplate e WHERE e.updatedAt >= :date")
    List<EmailTemplate> findByUpdatedAtAfter(@Param("date") java.time.LocalDateTime date);
    
    // Count templates by language
    long countByLanguage(String language);
    
    // Count active templates
    long countByIsActiveTrue();
    
    // Count templates by user
    long countByCreatedBy_Id(Long userId);
    
    // Find templates by name and language
    List<EmailTemplate> findByNameAndLanguage(String name, String language);
    
    // Find templates by name containing and active status
    List<EmailTemplate> findByNameContainingIgnoreCaseAndIsActive(String name, Boolean isActive);
    
    // Find templates by description containing and language
    List<EmailTemplate> findByDescriptionContainingIgnoreCaseAndLanguage(String description, String language);
    
    // Find templates by version and active status
    List<EmailTemplate> findByVersionAndIsActive(String version, Boolean isActive);
    
    // Find templates by multiple criteria
    @Query("SELECT e FROM EmailTemplate e WHERE " +
           "(:name IS NULL OR e.name LIKE %:name%) AND " +
           "(:language IS NULL OR e.language = :language) AND " +
           "(:isActive IS NULL OR e.isActive = :isActive)")
    Page<EmailTemplate> findByMultipleCriteria(@Param("name") String name,
                                             @Param("language") String language,
                                             @Param("isActive") Boolean isActive,
                                             Pageable pageable);
    
    // Find duplicate template codes
    @Query("SELECT e.templateCode FROM EmailTemplate e GROUP BY e.templateCode HAVING COUNT(e) > 1")
    List<String> findDuplicateTemplateCodes();
    
    // Find templates by subject containing text
    List<EmailTemplate> findBySubjectContainingIgnoreCase(String subject);
    
    // Find templates by body containing text
    List<EmailTemplate> findByBodyContainingIgnoreCase(String body);
} 