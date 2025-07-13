package com.example.springbootapp.repository;

import com.example.springbootapp.entity.Workflow;
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
class WorkflowRepositoryTest {
    @Autowired
    private WorkflowRepository workflowRepository;
    @Autowired
    private UserRepository userRepository;

    private User initiator;
    private User assignee;

    @BeforeEach
    void setUp() {
        initiator = new User();
        initiator.setUsername("initiator");
        initiator.setEmail("initiator@example.com");
        initiator.setPassword("password");
        initiator = userRepository.save(initiator);
        assignee = new User();
        assignee.setUsername("assignee");
        assignee.setEmail("assignee@example.com");
        assignee.setPassword("password");
        assignee = userRepository.save(assignee);
    }

    @Test
    @DisplayName("Should save and retrieve a workflow")
    void testSaveAndFindById() {
        Workflow workflow = new Workflow();
        workflow.setName("Approval Workflow");
        workflow.setWorkflowType("APPROVAL");
        workflow.setStatus("IN_PROGRESS");
        workflow.setInitiatedBy(initiator);
        workflow.setAssignedTo(assignee);
        workflow.setDescription("Approval process");
        workflow.setSteps("[\"Step 1\", \"Step 2\"]");
        workflow.setCurrentStep(1);
        workflow.setTotalSteps(2);
        workflow.setIsActive(true);
        workflow.setPriority("HIGH");
        Workflow saved = workflowRepository.save(workflow);
        Optional<Workflow> found = workflowRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Approval Workflow");
    }

    @Test
    @DisplayName("Should find workflows by type")
    void testFindByWorkflowType() {
        Workflow workflow = new Workflow();
        workflow.setName("Registration Workflow");
        workflow.setWorkflowType("REGISTRATION");
        workflow.setStatus("PENDING");
        workflow.setInitiatedBy(initiator);
        workflow.setAssignedTo(assignee);
        workflow.setDescription("Registration process");
        workflow.setSteps("[\"Step 1\", \"Step 2\"]");
        workflow.setCurrentStep(1);
        workflow.setTotalSteps(2);
        workflow.setIsActive(true);
        workflow.setPriority("MEDIUM");
        workflowRepository.save(workflow);
        List<Workflow> found = workflowRepository.findByWorkflowType("REGISTRATION");
        assertThat(found).isNotEmpty();
    }

    @Test
    @DisplayName("Should find active workflows")
    void testFindByIsActiveTrue() {
        Workflow workflow = new Workflow();
        workflow.setName("Order Workflow");
        workflow.setWorkflowType("ORDER");
        workflow.setStatus("IN_PROGRESS");
        workflow.setInitiatedBy(initiator);
        workflow.setAssignedTo(assignee);
        workflow.setDescription("Order process");
        workflow.setSteps("[\"Step 1\", \"Step 2\"]");
        workflow.setCurrentStep(1);
        workflow.setTotalSteps(2);
        workflow.setIsActive(true);
        workflow.setPriority("HIGH");
        workflowRepository.save(workflow);
        List<Workflow> found = workflowRepository.findByIsActiveTrue();
        assertThat(found).isNotEmpty();
    }
} 