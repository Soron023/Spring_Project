package com.example.springbootapp.repository;

import com.example.springbootapp.entity.EmailTemplate;
import com.example.springbootapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmailTemplateRepositoryTest {
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("templateuser");
        user.setEmail("template@example.com");
        user.setPassword("password");
        user = userRepository.save(user);
    }

    @Test
    @DisplayName("Should save and retrieve an email template")
    void testSaveAndFindById() {
        EmailTemplate template = new EmailTemplate();
        template.setTemplateCode("WELCOME");
        template.setName("Welcome Template");
        template.setSubject("Welcome!");
        template.setBody("Hello {{userName}}!");
        template.setLanguage("en");
        template.setIsActive(true);
        template.setCreatedBy(user);
        template.setDescription("Welcome email");
        template.setVersion("1.0");
        EmailTemplate saved = emailTemplateRepository.save(template);
        Optional<EmailTemplate> found = emailTemplateRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTemplateCode()).isEqualTo("WELCOME");
    }

    @Test
    @DisplayName("Should find template by code")
    void testFindByTemplateCode() {
        EmailTemplate template = new EmailTemplate();
        template.setTemplateCode("ORDER_CONFIRM");
        template.setName("Order Confirmation");
        template.setSubject("Order Confirmed");
        template.setBody("Order #{{orderId}} confirmed");
        template.setLanguage("en");
        template.setIsActive(true);
        template.setCreatedBy(user);
        template.setDescription("Order confirmation");
        template.setVersion("1.0");
        emailTemplateRepository.save(template);
        Optional<EmailTemplate> found = emailTemplateRepository.findByTemplateCode("ORDER_CONFIRM");
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("Should find active templates by language")
    void testFindByLanguageAndIsActive() {
        EmailTemplate template = new EmailTemplate();
        template.setTemplateCode("RESET");
        template.setName("Reset Password");
        template.setSubject("Reset");
        template.setBody("Reset your password");
        template.setLanguage("en");
        template.setIsActive(true);
        template.setCreatedBy(user);
        template.setDescription("Password reset");
        template.setVersion("1.0");
        emailTemplateRepository.save(template);
        List<EmailTemplate> found = emailTemplateRepository.findByLanguageAndIsActive("en", true);
        assertThat(found).isNotEmpty();
    }
} 