package com.example.springbootapp.repository;

import com.example.springbootapp.entity.Report;
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
class ReportRepositoryTest {
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("reportuser");
        user.setEmail("report@example.com");
        user.setPassword("password");
        user = userRepository.save(user);
    }

    @Test
    @DisplayName("Should save and retrieve a report")
    void testSaveAndFindById() {
        Report report = new Report();
        report.setName("Sales Report");
        report.setReportType("SALES");
        report.setFormat("PDF");
        report.setStatus("COMPLETED");
        report.setRequestedBy(user);
        report.setParameters("{}");
        report.setDescription("Sales report");
        report.setIsScheduled(false);
        report.setIsActive(true);
        Report saved = reportRepository.save(report);
        Optional<Report> found = reportRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Sales Report");
    }

    @Test
    @DisplayName("Should find reports by type")
    void testFindByReportType() {
        Report report = new Report();
        report.setName("User Activity");
        report.setReportType("USER_ACTIVITY");
        report.setFormat("EXCEL");
        report.setStatus("PENDING");
        report.setRequestedBy(user);
        report.setParameters("{}");
        report.setDescription("User activity report");
        report.setIsScheduled(false);
        report.setIsActive(true);
        reportRepository.save(report);
        List<Report> found = reportRepository.findByReportType("USER_ACTIVITY");
        assertThat(found).isNotEmpty();
    }

    @Test
    @DisplayName("Should find scheduled reports")
    void testFindByIsScheduledTrue() {
        Report report = new Report();
        report.setName("Inventory Report");
        report.setReportType("INVENTORY");
        report.setFormat("PDF");
        report.setStatus("PENDING");
        report.setRequestedBy(user);
        report.setParameters("{}");
        report.setDescription("Inventory report");
        report.setIsScheduled(true);
        report.setIsActive(true);
        reportRepository.save(report);
        List<Report> found = reportRepository.findByIsScheduledTrue();
        assertThat(found).isNotEmpty();
    }
} 