package com.example.springbootapp.repository;

import com.example.springbootapp.entity.FileUpload;
import com.example.springbootapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
    
    // Find files by user
    Page<FileUpload> findByUploadedBy(User user, Pageable pageable);
    
    // Find public files
    Page<FileUpload> findByIsPublicTrue(Pageable pageable);
    
    // Find files by file type
    List<FileUpload> findByFileType(String fileType);
    
    // Find files uploaded after a specific date
    List<FileUpload> findByUploadedAtAfter(LocalDateTime date);
    
    // Find files by size range
    @Query("SELECT f FROM FileUpload f WHERE f.fileSize BETWEEN :minSize AND :maxSize")
    List<FileUpload> findByFileSizeBetween(@Param("minSize") Long minSize, @Param("maxSize") Long maxSize);
    
    // Find files by checksum
    Optional<FileUpload> findByChecksum(String checksum);
    
    // Find files by original filename
    List<FileUpload> findByOriginalFileNameContainingIgnoreCase(String fileName);
    
    // Find files by upload directory
    List<FileUpload> findByUploadDir(String uploadDir);
    
    // Find files uploaded by user and public status
    Page<FileUpload> findByUploadedByAndIsPublic(User user, Boolean isPublic, Pageable pageable);
    
    // Find files by user and file type
    List<FileUpload> findByUploadedByAndFileType(User user, String fileType);
    
    // Count files by user
    long countByUploadedBy(User user);
    
    // Count files by file type
    long countByFileType(String fileType);
    
    // Find files uploaded today
    @Query("SELECT f FROM FileUpload f WHERE DATE(f.uploadedAt) = CURRENT_DATE")
    List<FileUpload> findFilesUploadedToday();
    
    // Find largest files
    @Query("SELECT f FROM FileUpload f ORDER BY f.fileSize DESC")
    Page<FileUpload> findLargestFiles(Pageable pageable);
    
    // Find recent files
    @Query("SELECT f FROM FileUpload f ORDER BY f.uploadedAt DESC")
    Page<FileUpload> findRecentFiles(Pageable pageable);
    
    // Find files by description containing text
    List<FileUpload> findByDescriptionContainingIgnoreCase(String description);
    
    // Find files by multiple file types
    @Query("SELECT f FROM FileUpload f WHERE f.fileType IN :fileTypes")
    List<FileUpload> findByFileTypes(@Param("fileTypes") List<String> fileTypes);
    
    // Find files uploaded in date range
    @Query("SELECT f FROM FileUpload f WHERE f.uploadedAt BETWEEN :startDate AND :endDate")
    List<FileUpload> findByUploadedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
    
    // Find duplicate files by checksum
    @Query("SELECT f FROM FileUpload f WHERE f.checksum IN " +
           "(SELECT f2.checksum FROM FileUpload f2 GROUP BY f2.checksum HAVING COUNT(f2) > 1)")
    List<FileUpload> findDuplicateFiles();
    
    // Find files by user with size greater than
    @Query("SELECT f FROM FileUpload f WHERE f.uploadedBy = :user AND f.fileSize > :minSize")
    List<FileUpload> findByUploadedByAndFileSizeGreaterThan(@Param("user") User user, 
                                                           @Param("minSize") Long minSize);
} 