package com.example.springbootapp.repository;

import com.example.springbootapp.entity.FileUpload;
import com.example.springbootapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FileUploadRepositoryTest {
    @Autowired
    private FileUploadRepository fileUploadRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user = userRepository.save(user);
    }

    @Test
    @DisplayName("Should save and retrieve a file upload")
    void testSaveAndFindById() {
        FileUpload file = new FileUpload();
        file.setFileName("file1.pdf");
        file.setOriginalFileName("original.pdf");
        file.setFileType("application/pdf");
        file.setFileSize(1024L);
        file.setFilePath("/files/file1.pdf");
        file.setUploadDir("/files");
        file.setUploadedBy(user);
        file.setIsPublic(false);
        file.setDescription("Test file");
        file.setChecksum("abc123");
        FileUpload saved = fileUploadRepository.save(file);
        Optional<FileUpload> found = fileUploadRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getFileName()).isEqualTo("file1.pdf");
    }

    @Test
    @DisplayName("Should find files by user")
    void testFindByUploadedBy() {
        FileUpload file = new FileUpload();
        file.setFileName("file2.pdf");
        file.setOriginalFileName("original2.pdf");
        file.setFileType("application/pdf");
        file.setFileSize(2048L);
        file.setFilePath("/files/file2.pdf");
        file.setUploadDir("/files");
        file.setUploadedBy(user);
        file.setIsPublic(true);
        file.setDescription("Test file 2");
        file.setChecksum("def456");
        fileUploadRepository.save(file);
        var page = fileUploadRepository.findByUploadedBy(user, PageRequest.of(0, 10));
        assertThat(page.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("Should find files by checksum")
    void testFindByChecksum() {
        FileUpload file = new FileUpload();
        file.setFileName("file3.pdf");
        file.setOriginalFileName("original3.pdf");
        file.setFileType("application/pdf");
        file.setFileSize(4096L);
        file.setFilePath("/files/file3.pdf");
        file.setUploadDir("/files");
        file.setUploadedBy(user);
        file.setIsPublic(false);
        file.setDescription("Test file 3");
        file.setChecksum("xyz789");
        fileUploadRepository.save(file);
        Optional<FileUpload> found = fileUploadRepository.findByChecksum("xyz789");
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("Should find public files")
    void testFindByIsPublicTrue() {
        FileUpload file = new FileUpload();
        file.setFileName("file4.pdf");
        file.setOriginalFileName("original4.pdf");
        file.setFileType("application/pdf");
        file.setFileSize(512L);
        file.setFilePath("/files/file4.pdf");
        file.setUploadDir("/files");
        file.setUploadedBy(user);
        file.setIsPublic(true);
        file.setDescription("Test file 4");
        file.setChecksum("public123");
        fileUploadRepository.save(file);
        var page = fileUploadRepository.findByIsPublicTrue(PageRequest.of(0, 10));
        assertThat(page.getContent()).isNotEmpty();
    }
} 