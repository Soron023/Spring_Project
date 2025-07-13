package com.example.springbootapp.service;

import com.example.springbootapp.entity.FileUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FileUploadService {
    
    // Basic CRUD operations
    FileUpload save(FileUpload fileUpload);
    Optional<FileUpload> findById(Long id);
    List<FileUpload> findAll();
    void deleteById(Long id);
    
    // File upload operations
    FileUpload uploadFile(MultipartFile file, String description, Boolean isPublic, Long userId);
    FileUpload uploadFileWithChecksum(MultipartFile file, String description, Boolean isPublic, Long userId, String checksum);
    
    // Repository-based queries
    Page<FileUpload> findByUploadedBy(Long userId, Pageable pageable);
    Page<FileUpload> findByIsPublicTrue(Pageable pageable);
    List<FileUpload> findByFileType(String fileType);
    List<FileUpload> findByUploadedAtAfter(LocalDateTime date);
    Optional<FileUpload> findByChecksum(String checksum);
    List<FileUpload> findByOriginalFileNameContainingIgnoreCase(String fileName);
    List<FileUpload> findByUploadDir(String uploadDir);
    Page<FileUpload> findByUploadedByAndIsPublic(Long userId, Boolean isPublic, Pageable pageable);
    List<FileUpload> findByUploadedByAndFileType(Long userId, String fileType);
    long countByUploadedBy(Long userId);
    long countByFileType(String fileType);
    List<FileUpload> findFilesUploadedToday();
    Page<FileUpload> findLargestFiles(Pageable pageable);
    Page<FileUpload> findRecentFiles(Pageable pageable);
    List<FileUpload> findByDescriptionContainingIgnoreCase(String description);
    List<FileUpload> findByFileTypes(List<String> fileTypes);
    List<FileUpload> findByUploadedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<FileUpload> findDuplicateFiles();
    List<FileUpload> findByUploadedByAndFileSizeGreaterThan(Long userId, Long minSize);
    
    // Specification-based queries
    List<FileUpload> findBySpecification(Specification<FileUpload> spec);
    Page<FileUpload> findBySpecification(Specification<FileUpload> spec, Pageable pageable);
    
    // Advanced search methods using specifications
    List<FileUpload> searchFiles(String fileName, String fileType, Boolean isPublic, Long userId, 
                                LocalDateTime startDate, LocalDateTime endDate, Long minSize, Long maxSize);
    
    List<FileUpload> findImageFiles();
    List<FileUpload> findDocumentFiles();
    List<FileUpload> findVideoFiles();
    List<FileUpload> findAudioFiles();
    
    List<FileUpload> findFilesUploadedThisWeek();
    List<FileUpload> findFilesUploadedThisMonth();
    
    List<FileUpload> findLargeFiles(Long minSize);
    List<FileUpload> findSmallFiles(Long maxSize);
    
    List<FileUpload> findPublicFilesByUser(Long userId);
    List<FileUpload> findPrivateFilesByUser(Long userId);
    
    // Statistics and analytics
    long getTotalFileCount();
    long getTotalFileSize();
    double getAverageFileSize();
    long getFileCountByType(String fileType);
    long getFileCountByUser(Long userId);
    long getPublicFileCount();
    long getPrivateFileCount();
    
    // File management operations
    boolean deleteFile(Long fileId, Long userId);
    boolean updateFileVisibility(Long fileId, Boolean isPublic, Long userId);
    boolean updateFileDescription(Long fileId, String description, Long userId);
    
    // Duplicate detection
    boolean isDuplicateFile(String checksum);
    List<FileUpload> findDuplicatesByChecksum(String checksum);
    
    // File validation
    boolean isValidFileType(String fileType);
    boolean isValidFileSize(Long fileSize);
    String generateChecksum(MultipartFile file);
    
    // Storage operations
    String generateUniqueFileName(String originalFileName);
    String getFileExtension(String fileName);
    String getMimeType(String fileName);
} 