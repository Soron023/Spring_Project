# New Modules User Guide

This guide covers the four new modules added to the Spring Boot e-commerce application: FileUpload, EmailTemplate, Report, and Workflow management systems.

## Table of Contents

1. [FileUpload Module](#fileupload-module)
2. [EmailTemplate Module](#emailtemplate-module)
3. [Report Module](#report-module)
4. [Workflow Module](#workflow-module)
5. [Database Schema](#database-schema)
6. [API Reference](#api-reference)
7. [Best Practices](#best-practices)
8. [Troubleshooting](#troubleshooting)

---

## FileUpload Module

### Overview
The FileUpload module provides comprehensive file management capabilities with duplicate detection, access control, and metadata tracking.

### Key Features
- **File Upload & Storage**: Secure file upload with unique naming
- **Duplicate Detection**: SHA-256 checksum-based duplicate prevention
- **Access Control**: Public/private file visibility
- **Metadata Tracking**: File type, size, upload date, and user information
- **Advanced Search**: Specification-based filtering and search
- **Storage Management**: Organized directory structure by user

### Use Cases
1. **Document Management**: Store and organize business documents
2. **Image Gallery**: Manage product images and media files
3. **Backup System**: Secure file backup with version tracking
4. **Collaboration**: Share files between team members

### Example Usage

```java
// Upload a file
FileUpload file = fileUploadService.uploadFile(
    multipartFile, 
    "Product catalog 2024", 
    true, 
    userId
);

// Search for image files
List<FileUpload> images = fileUploadService.findImageFiles();

// Find files by user
Page<FileUpload> userFiles = fileUploadService.findByUploadedBy(userId, pageable);

// Check for duplicates
boolean isDuplicate = fileUploadService.isDuplicateFile(checksum);
```

### File Types Supported
- **Images**: JPEG, PNG, GIF, BMP, WebP
- **Documents**: PDF, DOC, DOCX, XLS, XLSX, TXT, HTML
- **Media**: MP4, AVI, MOV, MP3, WAV, OGG

---

## EmailTemplate Module

### Overview
The EmailTemplate module enables dynamic email template management with multi-language support and version control.

### Key Features
- **Template Management**: Create, update, and version email templates
- **Multi-language Support**: Templates for different locales
- **Dynamic Content**: Variable substitution in templates
- **Version Control**: Track template changes and rollbacks
- **Active/Inactive States**: Control template availability
- **Advanced Search**: Find templates by various criteria

### Use Cases
1. **Marketing Campaigns**: Automated email marketing
2. **Order Notifications**: Order confirmations and updates
3. **User Registration**: Welcome emails and verification
4. **System Notifications**: Alerts and announcements

### Template Structure
```json
{
  "templateCode": "ORDER_CONFIRMATION",
  "name": "Order Confirmation Email",
  "subject": "Your order #{{orderNumber}} has been confirmed",
  "body": "Dear {{customerName}}, your order has been confirmed...",
  "language": "en",
  "version": "1.0",
  "isActive": true
}
```

### Example Usage

```java
// Create a new template
EmailTemplate template = new EmailTemplate();
template.setTemplateCode("WELCOME_EMAIL");
template.setName("Welcome Email");
template.setSubject("Welcome to our store, {{name}}!");
template.setBody("Dear {{name}}, welcome to our e-commerce platform...");
template.setLanguage("en");
template.setIsActive(true);
emailTemplateService.save(template);

// Find active templates by language
List<EmailTemplate> activeTemplates = emailTemplateService.findActiveTemplatesByLanguage("en");

// Search templates by name
List<EmailTemplate> welcomeTemplates = emailTemplateService.findByNameContainingIgnoreCase("welcome");
```

---

## Report Module

### Overview
The Report module provides comprehensive reporting capabilities with scheduling, multiple formats, and automated generation.

### Key Features
- **Report Generation**: Create reports in multiple formats (PDF, Excel, CSV)
- **Scheduling**: Automated report generation using cron expressions
- **Status Tracking**: Monitor report generation progress
- **File Management**: Store and retrieve generated reports
- **Parameter Support**: Dynamic report parameters
- **User Assignment**: Assign reports to specific users

### Use Cases
1. **Sales Reports**: Daily, weekly, monthly sales summaries
2. **Inventory Reports**: Stock levels and movement analysis
3. **User Activity Reports**: User engagement and behavior
4. **Financial Reports**: Revenue, profit, and expense analysis

### Report Types
- **SALES_SUMMARY**: Sales performance reports
- **INVENTORY_STATUS**: Stock level reports
- **USER_ACTIVITY**: User engagement reports
- **FINANCIAL_SUMMARY**: Financial performance reports
- **CUSTOM**: Custom parameterized reports

### Example Usage

```java
// Create a scheduled report
Report report = new Report();
report.setName("Daily Sales Report");
report.setReportType("SALES_SUMMARY");
report.setFormat("PDF");
report.setCronExpression("0 0 6 * * ?"); // Daily at 6 AM
report.setIsScheduled(true);
report.setIsActive(true);
report.setRequestedBy(user);
reportService.save(report);

// Find reports by status
List<Report> pendingReports = reportService.findByStatus("PENDING");

// Find scheduled reports due for execution
List<Report> dueReports = reportService.findScheduledReportsDueBefore(LocalDateTime.now());
```

---

## Workflow Module

### Overview
The Workflow module manages business processes with step-by-step progression, user assignments, and status tracking.

### Key Features
- **Process Management**: Define and execute business workflows
- **Step Tracking**: Monitor progress through workflow steps
- **User Assignment**: Assign workflows to specific users
- **Priority Management**: Set workflow priorities
- **Status Tracking**: Track workflow completion status
- **Comments & Notes**: Add context and communication

### Use Cases
1. **Order Processing**: Order approval and fulfillment workflows
2. **User Registration**: Account verification and approval
3. **Content Approval**: Content review and publishing workflows
4. **Issue Resolution**: Customer support ticket workflows

### Workflow Types
- **APPROVAL**: Approval-based workflows
- **REGISTRATION**: User registration workflows
- **ORDER**: Order processing workflows
- **SUPPORT**: Customer support workflows
- **CONTENT**: Content management workflows

### Workflow Statuses
- **PENDING**: Waiting to start
- **IN_PROGRESS**: Currently being processed
- **COMPLETED**: Successfully finished
- **CANCELLED**: Workflow cancelled
- **ON_HOLD**: Temporarily paused

### Example Usage

```java
// Create a new workflow
Workflow workflow = new Workflow();
workflow.setName("Order Approval Workflow");
workflow.setWorkflowType("APPROVAL");
workflow.setStatus("PENDING");
workflow.setPriority("HIGH");
workflow.setCurrentStep(1);
workflow.setTotalSteps(3);
workflow.setInitiatedBy(user);
workflow.setAssignedTo(approver);
workflow.setIsActive(true);
workflowService.save(workflow);

// Find workflows by user
Page<Workflow> userWorkflows = workflowService.findByAssignedTo(userId, pageable);

// Find high priority workflows
List<Workflow> highPriority = workflowService.findByPriority("HIGH");

// Check workflow progress
double progress = (double) workflow.getCurrentStep() / workflow.getTotalSteps() * 100;
```

---

## Database Schema

### FileUpload Table
```sql
CREATE TABLE file_upload (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(100),
    file_size BIGINT,
    file_path VARCHAR(500),
    upload_dir VARCHAR(500),
    checksum VARCHAR(64) UNIQUE,
    description TEXT,
    is_public BOOLEAN DEFAULT FALSE,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    uploaded_by_id BIGINT,
    FOREIGN KEY (uploaded_by_id) REFERENCES user(id)
);
```

### EmailTemplate Table
```sql
CREATE TABLE email_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_code VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    subject VARCHAR(500),
    body TEXT,
    language VARCHAR(10) DEFAULT 'en',
    version VARCHAR(20) DEFAULT '1.0',
    is_active BOOLEAN DEFAULT TRUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by_id BIGINT,
    FOREIGN KEY (created_by_id) REFERENCES user(id)
);
```

### Report Table
```sql
CREATE TABLE report (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    report_type VARCHAR(100) NOT NULL,
    format VARCHAR(20) DEFAULT 'PDF',
    status VARCHAR(50) DEFAULT 'PENDING',
    file_path VARCHAR(500),
    file_size BIGINT,
    parameters TEXT,
    cron_expression VARCHAR(100),
    is_scheduled BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    next_execution TIMESTAMP,
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    requested_by_id BIGINT,
    FOREIGN KEY (requested_by_id) REFERENCES user(id)
);
```

### Workflow Table
```sql
CREATE TABLE workflow (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    workflow_type VARCHAR(100) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    priority VARCHAR(20) DEFAULT 'MEDIUM',
    current_step INTEGER DEFAULT 1,
    total_steps INTEGER DEFAULT 1,
    description TEXT,
    comments TEXT,
    steps TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    initiated_by_id BIGINT,
    assigned_to_id BIGINT,
    FOREIGN KEY (initiated_by_id) REFERENCES user(id),
    FOREIGN KEY (assigned_to_id) REFERENCES user(id)
);
```

---

## API Reference

### FileUpload Endpoints
```
POST /api/files/upload - Upload a new file
GET /api/files - Get all files (with pagination)
GET /api/files/{id} - Get file by ID
GET /api/files/user/{userId} - Get files by user
GET /api/files/public - Get public files
DELETE /api/files/{id} - Delete file
PUT /api/files/{id}/visibility - Update file visibility
PUT /api/files/{id}/description - Update file description
```

### EmailTemplate Endpoints
```
POST /api/templates - Create new template
GET /api/templates - Get all templates
GET /api/templates/{id} - Get template by ID
GET /api/templates/code/{code} - Get template by code
GET /api/templates/language/{language} - Get templates by language
PUT /api/templates/{id} - Update template
DELETE /api/templates/{id} - Delete template
```

### Report Endpoints
```
POST /api/reports - Create new report
GET /api/reports - Get all reports
GET /api/reports/{id} - Get report by ID
GET /api/reports/status/{status} - Get reports by status
GET /api/reports/user/{userId} - Get reports by user
PUT /api/reports/{id} - Update report
DELETE /api/reports/{id} - Delete report
POST /api/reports/{id}/generate - Generate report
```

### Workflow Endpoints
```
POST /api/workflows - Create new workflow
GET /api/workflows - Get all workflows
GET /api/workflows/{id} - Get workflow by ID
GET /api/workflows/status/{status} - Get workflows by status
GET /api/workflows/user/{userId} - Get workflows by user
PUT /api/workflows/{id} - Update workflow
DELETE /api/workflows/{id} - Delete workflow
PUT /api/workflows/{id}/progress - Update workflow progress
```

---

## Best Practices

### FileUpload Best Practices
1. **File Size Limits**: Set appropriate file size limits (default: 100MB)
2. **File Type Validation**: Validate file types before upload
3. **Duplicate Prevention**: Use checksums to prevent duplicate uploads
4. **Access Control**: Implement proper access control for private files
5. **Storage Organization**: Organize files by user ID for better management

### EmailTemplate Best Practices
1. **Template Versioning**: Use semantic versioning for templates
2. **Language Support**: Provide templates for all supported languages
3. **Variable Documentation**: Document all template variables
4. **Testing**: Test templates with various data scenarios
5. **Backup**: Keep backup copies of important templates

### Report Best Practices
1. **Scheduling**: Use appropriate cron expressions for scheduling
2. **Error Handling**: Implement proper error handling for failed reports
3. **Storage Management**: Clean up old report files periodically
4. **Performance**: Optimize report queries for large datasets
5. **Security**: Implement proper access control for sensitive reports

### Workflow Best Practices
1. **Step Definition**: Clearly define workflow steps and requirements
2. **User Assignment**: Assign workflows to appropriate users
3. **Progress Tracking**: Regularly update workflow progress
4. **Communication**: Use comments to maintain workflow context
5. **Escalation**: Implement escalation procedures for stuck workflows

---

## Troubleshooting

### Common Issues

#### FileUpload Issues
- **File Upload Fails**: Check file size limits and disk space
- **Duplicate Detection**: Verify checksum generation is working
- **Access Denied**: Check file permissions and user access rights

#### EmailTemplate Issues
- **Template Not Found**: Verify template code and language
- **Variable Substitution**: Check variable names and syntax
- **Version Conflicts**: Ensure proper version management

#### Report Issues
- **Report Generation Fails**: Check database connectivity and permissions
- **Scheduling Issues**: Verify cron expression syntax
- **File Storage**: Ensure sufficient disk space for report files

#### Workflow Issues
- **Workflow Stuck**: Check user assignments and step requirements
- **Progress Not Updated**: Verify workflow update permissions
- **Status Issues**: Ensure proper status transitions

### Debugging Tips
1. **Check Logs**: Review application logs for error messages
2. **Database Queries**: Verify database connectivity and query performance
3. **File Permissions**: Check file system permissions for uploads and reports
4. **User Permissions**: Verify user roles and access rights
5. **Configuration**: Check application configuration settings

### Performance Optimization
1. **Database Indexing**: Ensure proper database indexes for queries
2. **File Storage**: Use appropriate storage solutions for large files
3. **Caching**: Implement caching for frequently accessed data
4. **Batch Processing**: Use batch operations for bulk data processing
5. **Monitoring**: Implement monitoring for system performance

---

## Conclusion

The new modules provide comprehensive functionality for file management, email templating, reporting, and workflow management. By following the best practices outlined in this guide, you can effectively utilize these modules to enhance your e-commerce application's capabilities.

For additional support or questions, please refer to the API documentation or contact the development team. 