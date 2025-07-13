package com.example.springbootapp.service;

import com.example.springbootapp.entity.FileUpload;
import com.example.springbootapp.entity.User;
import com.example.springbootapp.exception.ResourceNotFoundException;
import com.example.springbootapp.repository.FileUploadRepository;
import com.example.springbootapp.repository.UserRepository;
import com.example.springbootapp.specification.FileUploadSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads";
    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB

    // Basic CRUD operations
    @Override
    public FileUpload save(FileUpload fileUpload) {
        return fileUploadRepository.save(fileUpload);
    }

    @Override
    public Optional<FileUpload> findById(Long id) {
        return fileUploadRepository.findById(id);
    }

    @Override
    public List<FileUpload> findAll() {
        return fileUploadRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        fileUploadRepository.deleteById(id);
    }

    // File upload operations
    @Override
    public FileUpload uploadFile(MultipartFile file, String description, Boolean isPublic, Long userId) {
        String checksum = generateChecksum(file);
        return uploadFileWithChecksum(file, description, isPublic, userId, checksum);
    }

    @Override
    public FileUpload uploadFileWithChecksum(MultipartFile file, String description, Boolean isPublic, Long userId, String checksum) {
        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        if (!isValidFileSize(file.getSize())) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size");
        }

        // Check for duplicates
        if (isDuplicateFile(checksum)) {
            throw new IllegalArgumentException("File already exists (duplicate detected)");
        }

        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Generate unique filename
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = generateUniqueFileName(originalFileName);
        String fileType = getMimeType(originalFileName);
        String uploadDir = UPLOAD_DIR + "/" + user.getId();

        // Create upload directory
        Path uploadPath = Paths.get(uploadDir);
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory", e);
        }

        // Save file to disk
        Path filePath = uploadPath.resolve(uniqueFileName);
        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }

        // Create file upload entity
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFileName(uniqueFileName);
        fileUpload.setOriginalFileName(originalFileName);
        fileUpload.setFileType(fileType);
        fileUpload.setFileSize(file.getSize());
        fileUpload.setFilePath(filePath.toString());
        fileUpload.setUploadDir(uploadDir);
        fileUpload.setUploadedBy(user);
        fileUpload.setIsPublic(isPublic != null ? isPublic : false);
        fileUpload.setDescription(description);
        fileUpload.setChecksum(checksum);

        return fileUploadRepository.save(fileUpload);
    }

    // Repository-based queries
    @Override
    public Page<FileUpload> findByUploadedBy(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return fileUploadRepository.findByUploadedBy(user, pageable);
    }

    @Override
    public Page<FileUpload> findByIsPublicTrue(Pageable pageable) {
        return fileUploadRepository.findByIsPublicTrue(pageable);
    }

    @Override
    public List<FileUpload> findByFileType(String fileType) {
        return fileUploadRepository.findByFileType(fileType);
    }

    @Override
    public List<FileUpload> findByUploadedAtAfter(LocalDateTime date) {
        return fileUploadRepository.findByUploadedAtAfter(date);
    }

    @Override
    public Optional<FileUpload> findByChecksum(String checksum) {
        return fileUploadRepository.findByChecksum(checksum);
    }

    @Override
    public List<FileUpload> findByOriginalFileNameContainingIgnoreCase(String fileName) {
        return fileUploadRepository.findByOriginalFileNameContainingIgnoreCase(fileName);
    }

    @Override
    public List<FileUpload> findByUploadDir(String uploadDir) {
        return fileUploadRepository.findByUploadDir(uploadDir);
    }

    @Override
    public Page<FileUpload> findByUploadedByAndIsPublic(Long userId, Boolean isPublic, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return fileUploadRepository.findByUploadedByAndIsPublic(user, isPublic, pageable);
    }

    @Override
    public List<FileUpload> findByUploadedByAndFileType(Long userId, String fileType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return fileUploadRepository.findByUploadedByAndFileType(user, fileType);
    }

    @Override
    public long countByUploadedBy(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return fileUploadRepository.countByUploadedBy(user);
    }

    @Override
    public long countByFileType(String fileType) {
        return fileUploadRepository.countByFileType(fileType);
    }

    @Override
    public List<FileUpload> findFilesUploadedToday() {
        return fileUploadRepository.findFilesUploadedToday();
    }

    @Override
    public Page<FileUpload> findLargestFiles(Pageable pageable) {
        return fileUploadRepository.findLargestFiles(pageable);
    }

    @Override
    public Page<FileUpload> findRecentFiles(Pageable pageable) {
        return fileUploadRepository.findRecentFiles(pageable);
    }

    @Override
    public List<FileUpload> findByDescriptionContainingIgnoreCase(String description) {
        return fileUploadRepository.findByDescriptionContainingIgnoreCase(description);
    }

    @Override
    public List<FileUpload> findByFileTypes(List<String> fileTypes) {
        return fileUploadRepository.findByFileTypes(fileTypes);
    }

    @Override
    public List<FileUpload> findByUploadedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return fileUploadRepository.findByUploadedAtBetween(startDate, endDate);
    }

    @Override
    public List<FileUpload> findDuplicateFiles() {
        return fileUploadRepository.findDuplicateFiles();
    }

    @Override
    public List<FileUpload> findByUploadedByAndFileSizeGreaterThan(Long userId, Long minSize) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return fileUploadRepository.findByUploadedByAndFileSizeGreaterThan(user, minSize);
    }

    // Specification-based queries
    @Override
    public List<FileUpload> findBySpecification(Specification<FileUpload> spec) {
        return fileUploadRepository.findAll(spec);
    }

    @Override
    public Page<FileUpload> findBySpecification(Specification<FileUpload> spec, Pageable pageable) {
        return fileUploadRepository.findAll(spec, pageable);
    }

    // Advanced search methods using specifications
    @Override
    public List<FileUpload> searchFiles(String fileName, String fileType, Boolean isPublic, Long userId,
                                       LocalDateTime startDate, LocalDateTime endDate, Long minSize, Long maxSize) {
        Specification<FileUpload> spec = Specification.where(null);

        if (fileName != null && !fileName.trim().isEmpty()) {
            spec = spec.and(FileUploadSpecification.hasFileName(fileName));
        }

        if (fileType != null && !fileType.trim().isEmpty()) {
            spec = spec.and(FileUploadSpecification.hasFileType(fileType));
        }

        if (isPublic != null) {
            spec = spec.and(FileUploadSpecification.isPublic(isPublic));
        }

        if (userId != null) {
            spec = spec.and(FileUploadSpecification.uploadedBy(userId));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and(FileUploadSpecification.uploadedBetween(startDate, endDate));
        }

        if (minSize != null || maxSize != null) {
            spec = spec.and(FileUploadSpecification.hasFileSizeBetween(minSize, maxSize));
        }

        return fileUploadRepository.findAll(spec);
    }

    @Override
    public List<FileUpload> findImageFiles() {
        return findBySpecification(FileUploadSpecification.isImageFile());
    }

    @Override
    public List<FileUpload> findDocumentFiles() {
        return findBySpecification(FileUploadSpecification.isDocumentFile());
    }

    @Override
    public List<FileUpload> findVideoFiles() {
        return findBySpecification(FileUploadSpecification.isVideoFile());
    }

    @Override
    public List<FileUpload> findAudioFiles() {
        return findBySpecification(FileUploadSpecification.isAudioFile());
    }

    @Override
    public List<FileUpload> findFilesUploadedThisWeek() {
        return findBySpecification(FileUploadSpecification.uploadedThisWeek());
    }

    @Override
    public List<FileUpload> findFilesUploadedThisMonth() {
        return findBySpecification(FileUploadSpecification.uploadedThisMonth());
    }

    @Override
    public List<FileUpload> findLargeFiles(Long minSize) {
        return findBySpecification(FileUploadSpecification.hasFileSizeGreaterThan(minSize));
    }

    @Override
    public List<FileUpload> findSmallFiles(Long maxSize) {
        return findBySpecification(FileUploadSpecification.hasFileSizeLessThan(maxSize));
    }

    @Override
    public List<FileUpload> findPublicFilesByUser(Long userId) {
        return findBySpecification(
            FileUploadSpecification.uploadedBy(userId)
                .and(FileUploadSpecification.isPublic(true))
        );
    }

    @Override
    public List<FileUpload> findPrivateFilesByUser(Long userId) {
        return findBySpecification(
            FileUploadSpecification.uploadedBy(userId)
                .and(FileUploadSpecification.isPublic(false))
        );
    }

    // Statistics and analytics
    @Override
    public long getTotalFileCount() {
        return fileUploadRepository.count();
    }

    @Override
    public long getTotalFileSize() {
        return fileUploadRepository.findAll().stream()
                .mapToLong(FileUpload::getFileSize)
                .sum();
    }

    @Override
    public double getAverageFileSize() {
        List<FileUpload> files = fileUploadRepository.findAll();
        if (files.isEmpty()) {
            return 0.0;
        }
        return files.stream()
                .mapToLong(FileUpload::getFileSize)
                .average()
                .orElse(0.0);
    }

    @Override
    public long getFileCountByType(String fileType) {
        return fileUploadRepository.countByFileType(fileType);
    }

    @Override
    public long getFileCountByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return fileUploadRepository.countByUploadedBy(user);
    }

    @Override
    public long getPublicFileCount() {
        return fileUploadRepository.findByIsPublicTrue(Pageable.unpaged()).getTotalElements();
    }

    @Override
    public long getPrivateFileCount() {
        return getTotalFileCount() - getPublicFileCount();
    }

    // File management operations
    @Override
    public boolean deleteFile(Long fileId, Long userId) {
        Optional<FileUpload> fileOpt = fileUploadRepository.findById(fileId);
        if (fileOpt.isPresent()) {
            FileUpload file = fileOpt.get();
            if (file.getUploadedBy().getId().equals(userId)) {
                // Delete physical file
                try {
                    Files.deleteIfExists(Paths.get(file.getFilePath()));
                } catch (IOException e) {
                    // Log error but continue with database deletion
                }
                fileUploadRepository.deleteById(fileId);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateFileVisibility(Long fileId, Boolean isPublic, Long userId) {
        Optional<FileUpload> fileOpt = fileUploadRepository.findById(fileId);
        if (fileOpt.isPresent()) {
            FileUpload file = fileOpt.get();
            if (file.getUploadedBy().getId().equals(userId)) {
                file.setIsPublic(isPublic);
                fileUploadRepository.save(file);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateFileDescription(Long fileId, String description, Long userId) {
        Optional<FileUpload> fileOpt = fileUploadRepository.findById(fileId);
        if (fileOpt.isPresent()) {
            FileUpload file = fileOpt.get();
            if (file.getUploadedBy().getId().equals(userId)) {
                file.setDescription(description);
                fileUploadRepository.save(file);
                return true;
            }
        }
        return false;
    }

    // Duplicate detection
    @Override
    public boolean isDuplicateFile(String checksum) {
        return fileUploadRepository.findByChecksum(checksum).isPresent();
    }

    @Override
    public List<FileUpload> findDuplicatesByChecksum(String checksum) {
        return findBySpecification(FileUploadSpecification.hasChecksum(checksum));
    }

    // File validation
    @Override
    public boolean isValidFileType(String fileType) {
        if (fileType == null) return false;
        
        List<String> allowedTypes = List.of(
            "image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp",
            "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "text/plain", "text/html", "video/mp4", "video/avi", "video/mov",
            "audio/mpeg", "audio/wav", "audio/ogg"
        );
        
        return allowedTypes.contains(fileType.toLowerCase());
    }

    @Override
    public boolean isValidFileSize(Long fileSize) {
        return fileSize != null && fileSize > 0 && fileSize <= MAX_FILE_SIZE;
    }

    @Override
    public String generateChecksum(MultipartFile file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(file.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("Failed to generate checksum", e);
        }
    }

    // Storage operations
    @Override
    public String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + extension;
    }

    @Override
    public String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    @Override
    public String getMimeType(String fileName) {
        if (fileName == null) return "application/octet-stream";
        
        String extension = getFileExtension(fileName).toLowerCase();
        return switch (extension) {
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".png" -> "image/png";
            case ".gif" -> "image/gif";
            case ".bmp" -> "image/bmp";
            case ".webp" -> "image/webp";
            case ".pdf" -> "application/pdf";
            case ".doc" -> "application/msword";
            case ".docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case ".xls" -> "application/vnd.ms-excel";
            case ".xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case ".txt" -> "text/plain";
            case ".html", ".htm" -> "text/html";
            case ".mp4" -> "video/mp4";
            case ".avi" -> "video/avi";
            case ".mov" -> "video/mov";
            case ".mp3" -> "audio/mpeg";
            case ".wav" -> "audio/wav";
            case ".ogg" -> "audio/ogg";
            default -> "application/octet-stream";
        };
    }
} 