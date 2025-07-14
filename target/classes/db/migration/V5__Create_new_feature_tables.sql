-- Migration: Create new feature tables
-- Description: Adds tables for FileUpload, EmailTemplate, Report, and Workflow entities

-- File Uploads Table
CREATE TABLE file_uploads (
    id BIGSERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    upload_dir VARCHAR(255) NOT NULL,
    user_id BIGINT REFERENCES users(id),
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    description TEXT,
    checksum VARCHAR(255) NOT NULL
);

-- Create indexes for file_uploads
CREATE INDEX idx_file_uploads_user_id ON file_uploads(user_id);
CREATE INDEX idx_file_uploads_file_type ON file_uploads(file_type);
CREATE INDEX idx_file_uploads_uploaded_at ON file_uploads(uploaded_at);
CREATE INDEX idx_file_uploads_is_public ON file_uploads(is_public);
CREATE INDEX idx_file_uploads_checksum ON file_uploads(checksum);
CREATE INDEX idx_file_uploads_file_size ON file_uploads(file_size);

-- Email Templates Table
CREATE TABLE email_templates (
    id BIGSERIAL PRIMARY KEY,
    template_code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    subject TEXT NOT NULL,
    body TEXT NOT NULL,
    language VARCHAR(10) NOT NULL DEFAULT 'en',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT REFERENCES users(id),
    description TEXT,
    version VARCHAR(20) NOT NULL DEFAULT '1.0'
);

-- Create indexes for email_templates
CREATE INDEX idx_email_templates_template_code ON email_templates(template_code);
CREATE INDEX idx_email_templates_language ON email_templates(language);
CREATE INDEX idx_email_templates_is_active ON email_templates(is_active);
CREATE INDEX idx_email_templates_created_by ON email_templates(created_by);
CREATE INDEX idx_email_templates_version ON email_templates(version);

-- Reports Table
CREATE TABLE reports (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    report_type VARCHAR(100) NOT NULL,
    format VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    requested_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    file_path VARCHAR(500),
    file_size BIGINT,
    requested_by BIGINT REFERENCES users(id),
    parameters TEXT,
    description TEXT,
    is_scheduled BOOLEAN NOT NULL DEFAULT FALSE,
    cron_expression VARCHAR(100),
    next_execution TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create indexes for reports
CREATE INDEX idx_reports_requested_by ON reports(requested_by);
CREATE INDEX idx_reports_report_type ON reports(report_type);
CREATE INDEX idx_reports_status ON reports(status);
CREATE INDEX idx_reports_format ON reports(format);
CREATE INDEX idx_reports_requested_at ON reports(requested_at);
CREATE INDEX idx_reports_is_scheduled ON reports(is_scheduled);
CREATE INDEX idx_reports_is_active ON reports(is_active);
CREATE INDEX idx_reports_next_execution ON reports(next_execution);

-- Workflows Table
CREATE TABLE workflows (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    workflow_type VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    initiated_by BIGINT REFERENCES users(id),
    assigned_to BIGINT REFERENCES users(id),
    description TEXT,
    steps TEXT,
    current_step INTEGER NOT NULL DEFAULT 1,
    total_steps INTEGER NOT NULL DEFAULT 1,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    completed_at TIMESTAMP,
    comments TEXT,
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM'
);

-- Create indexes for workflows
CREATE INDEX idx_workflows_initiated_by ON workflows(initiated_by);
CREATE INDEX idx_workflows_assigned_to ON workflows(assigned_to);
CREATE INDEX idx_workflows_workflow_type ON workflows(workflow_type);
CREATE INDEX idx_workflows_status ON workflows(status);
CREATE INDEX idx_workflows_priority ON workflows(priority);
CREATE INDEX idx_workflows_created_at ON workflows(created_at);
CREATE INDEX idx_workflows_is_active ON workflows(is_active);
CREATE INDEX idx_workflows_current_step ON workflows(current_step);
CREATE INDEX idx_workflows_total_steps ON workflows(total_steps);

-- Create views for better reporting

-- File Upload Statistics View
CREATE VIEW file_upload_stats AS
SELECT 
    COUNT(*) as total_files,
    SUM(file_size) as total_size,
    AVG(file_size) as avg_file_size,
    COUNT(DISTINCT user_id) as unique_users,
    COUNT(CASE WHEN is_public = true THEN 1 END) as public_files,
    COUNT(CASE WHEN is_public = false THEN 1 END) as private_files,
    file_type,
    DATE(uploaded_at) as upload_date
FROM file_uploads
GROUP BY file_type, DATE(uploaded_at);

-- Email Template Statistics View
CREATE VIEW email_template_stats AS
SELECT 
    COUNT(*) as total_templates,
    COUNT(CASE WHEN is_active = true THEN 1 END) as active_templates,
    COUNT(CASE WHEN is_active = false THEN 1 END) as inactive_templates,
    language,
    version
FROM email_templates
GROUP BY language, version;

-- Report Statistics View
CREATE VIEW report_stats AS
SELECT 
    COUNT(*) as total_reports,
    COUNT(CASE WHEN status = 'COMPLETED' THEN 1 END) as completed_reports,
    COUNT(CASE WHEN status = 'PENDING' THEN 1 END) as pending_reports,
    COUNT(CASE WHEN status = 'FAILED' THEN 1 END) as failed_reports,
    COUNT(CASE WHEN is_scheduled = true THEN 1 END) as scheduled_reports,
    report_type,
    format,
    DATE(requested_at) as request_date
FROM reports
GROUP BY report_type, format, DATE(requested_at);

-- Workflow Statistics View
CREATE VIEW workflow_stats AS
SELECT 
    COUNT(*) as total_workflows,
    COUNT(CASE WHEN status = 'COMPLETED' THEN 1 END) as completed_workflows,
    COUNT(CASE WHEN status = 'PENDING' THEN 1 END) as pending_workflows,
    COUNT(CASE WHEN status = 'IN_PROGRESS' THEN 1 END) as in_progress_workflows,
    COUNT(CASE WHEN is_active = true THEN 1 END) as active_workflows,
    AVG(current_step * 100.0 / total_steps) as avg_progress_percentage,
    workflow_type,
    priority,
    DATE(created_at) as creation_date
FROM workflows
GROUP BY workflow_type, priority, DATE(created_at);

-- Insert sample data for email templates
INSERT INTO email_templates (template_code, name, subject, body, language, is_active, created_by, description, version) VALUES
('WELCOME_EMAIL', 'Welcome Email', 'Welcome to Our Platform', 
 '<h1>Welcome {{userName}}!</h1><p>Thank you for joining our platform. We are excited to have you on board.</p>', 
 'en', true, 1, 'Welcome email sent to new users', '1.0'),
('ORDER_CONFIRMATION', 'Order Confirmation', 'Your Order Has Been Confirmed', 
 '<h1>Order Confirmation</h1><p>Dear {{customerName}}, your order #{{orderId}} has been confirmed.</p>', 
 'en', true, 1, 'Order confirmation email', '1.0'),
('PASSWORD_RESET', 'Password Reset', 'Password Reset Request', 
 '<h1>Password Reset</h1><p>Click the link below to reset your password: {{resetLink}}</p>', 
 'en', true, 1, 'Password reset email template', '1.0');

-- Insert sample data for reports
INSERT INTO reports (name, report_type, format, status, requested_by, description, is_scheduled, is_active) VALUES
('Daily Sales Report', 'SALES', 'PDF', 'COMPLETED', 1, 'Daily sales summary report', false, true),
('Monthly User Activity', 'USER_ACTIVITY', 'EXCEL', 'PENDING', 1, 'Monthly user activity report', true, true),
('Inventory Status', 'INVENTORY', 'PDF', 'COMPLETED', 1, 'Current inventory status report', false, true);

-- Insert sample data for workflows
INSERT INTO workflows (name, workflow_type, status, initiated_by, assigned_to, description, steps, total_steps, is_active, priority) VALUES
('Product Approval Process', 'APPROVAL', 'IN_PROGRESS', 1, 2, 'Product approval workflow', 
 '["Submit for review", "Review by manager", "Final approval", "Publish"]', 4, true, 'HIGH'),
('User Registration', 'REGISTRATION', 'COMPLETED', 1, 1, 'User registration workflow', 
 '["Create account", "Verify email", "Complete profile", "Activate account"]', 4, true, 'MEDIUM'),
('Order Processing', 'ORDER', 'PENDING', 1, 2, 'Order processing workflow', 
 '["Receive order", "Validate payment", "Process order", "Ship order", "Deliver"]', 5, true, 'HIGH');

-- Create triggers for updated_at timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_email_templates_updated_at BEFORE UPDATE ON email_templates
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_workflows_updated_at BEFORE UPDATE ON workflows
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Create function to calculate workflow progress
CREATE OR REPLACE FUNCTION calculate_workflow_progress(current_step INTEGER, total_steps INTEGER)
RETURNS DECIMAL AS $$
BEGIN
    RETURN ROUND((current_step * 100.0 / total_steps), 2);
END;
$$ LANGUAGE plpgsql;

-- Create function to get file size in human readable format
CREATE OR REPLACE FUNCTION format_file_size(size_bytes BIGINT)
RETURNS TEXT AS $$
BEGIN
    IF size_bytes < 1024 THEN
        RETURN size_bytes || ' B';
    ELSIF size_bytes < 1024 * 1024 THEN
        RETURN ROUND(size_bytes / 1024.0, 2) || ' KB';
    ELSIF size_bytes < 1024 * 1024 * 1024 THEN
        RETURN ROUND(size_bytes / (1024.0 * 1024.0), 2) || ' MB';
    ELSE
        RETURN ROUND(size_bytes / (1024.0 * 1024.0 * 1024.0), 2) || ' GB';
    END IF;
END;
$$ LANGUAGE plpgsql; 