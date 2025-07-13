-- Create audit_logs table
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    username VARCHAR(255),
    action VARCHAR(100) NOT NULL,
    resource_type VARCHAR(100) NOT NULL,
    resource_id VARCHAR(255),
    resource_name VARCHAR(255),
    details TEXT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    request_url TEXT,
    http_method VARCHAR(10),
    status_code INTEGER,
    execution_time_ms BIGINT,
    audit_level VARCHAR(20) NOT NULL DEFAULT 'INFO',
    session_id VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_username ON audit_logs(username);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
CREATE INDEX idx_audit_logs_resource_type ON audit_logs(resource_type);
CREATE INDEX idx_audit_logs_resource_id ON audit_logs(resource_id);
CREATE INDEX idx_audit_logs_audit_level ON audit_logs(audit_level);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
CREATE INDEX idx_audit_logs_ip_address ON audit_logs(ip_address);
CREATE INDEX idx_audit_logs_session_id ON audit_logs(session_id);
CREATE INDEX idx_audit_logs_http_method ON audit_logs(http_method);
CREATE INDEX idx_audit_logs_status_code ON audit_logs(status_code);

-- Create composite indexes for common queries
CREATE INDEX idx_audit_logs_user_created ON audit_logs(user_id, created_at DESC);
CREATE INDEX idx_audit_logs_action_created ON audit_logs(action, created_at DESC);
CREATE INDEX idx_audit_logs_level_created ON audit_logs(audit_level, created_at DESC);
CREATE INDEX idx_audit_logs_resource_created ON audit_logs(resource_type, created_at DESC);

-- Create index for date range queries
CREATE INDEX idx_audit_logs_date_range ON audit_logs(created_at DESC);

-- Create index for text search
CREATE INDEX idx_audit_logs_details_gin ON audit_logs USING gin(to_tsvector('english', details));
CREATE INDEX idx_audit_logs_resource_name_gin ON audit_logs USING gin(to_tsvector('english', resource_name));

-- Add comments for documentation
COMMENT ON TABLE audit_logs IS 'Audit trail for tracking user actions and system events';
COMMENT ON COLUMN audit_logs.id IS 'Primary key';
COMMENT ON COLUMN audit_logs.user_id IS 'ID of the user who performed the action';
COMMENT ON COLUMN audit_logs.username IS 'Username of the user who performed the action';
COMMENT ON COLUMN audit_logs.action IS 'Action performed (CREATE, READ, UPDATE, DELETE, etc.)';
COMMENT ON COLUMN audit_logs.resource_type IS 'Type of resource being accessed (USER, PRODUCT, etc.)';
COMMENT ON COLUMN audit_logs.resource_id IS 'ID of the specific resource being accessed';
COMMENT ON COLUMN audit_logs.resource_name IS 'Human-readable name of the resource';
COMMENT ON COLUMN audit_logs.details IS 'Additional details about the action';
COMMENT ON COLUMN audit_logs.ip_address IS 'IP address of the client';
COMMENT ON COLUMN audit_logs.user_agent IS 'User agent string from the client';
COMMENT ON COLUMN audit_logs.request_url IS 'URL of the request';
COMMENT ON COLUMN audit_logs.http_method IS 'HTTP method used (GET, POST, PUT, DELETE)';
COMMENT ON COLUMN audit_logs.status_code IS 'HTTP status code of the response';
COMMENT ON COLUMN audit_logs.execution_time_ms IS 'Request execution time in milliseconds';
COMMENT ON COLUMN audit_logs.audit_level IS 'Audit level (INFO, WARNING, ERROR, SECURITY, CRITICAL)';
COMMENT ON COLUMN audit_logs.session_id IS 'Session ID of the user';
COMMENT ON COLUMN audit_logs.created_at IS 'Timestamp when the audit log was created';

-- Create a function to automatically clean old audit logs
CREATE OR REPLACE FUNCTION clean_old_audit_logs(days_to_keep INTEGER DEFAULT 90)
RETURNS INTEGER AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM audit_logs 
    WHERE created_at < CURRENT_TIMESTAMP - INTERVAL '1 day' * days_to_keep;
    
    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- Create a view for security events
CREATE VIEW security_events AS
SELECT 
    id,
    user_id,
    username,
    action,
    resource_type,
    resource_id,
    resource_name,
    details,
    ip_address,
    created_at
FROM audit_logs 
WHERE audit_level IN ('SECURITY', 'CRITICAL')
ORDER BY created_at DESC;

-- Create a view for failed login attempts
CREATE VIEW failed_logins AS
SELECT 
    id,
    username,
    ip_address,
    user_agent,
    details,
    created_at
FROM audit_logs 
WHERE action = 'LOGIN_FAILED'
ORDER BY created_at DESC;

-- Create a view for successful logins
CREATE VIEW successful_logins AS
SELECT 
    id,
    user_id,
    username,
    ip_address,
    user_agent,
    created_at
FROM audit_logs 
WHERE action = 'LOGIN_SUCCESS'
ORDER BY created_at DESC;

-- Create a view for data access events
CREATE VIEW data_access_events AS
SELECT 
    id,
    user_id,
    username,
    action,
    resource_type,
    resource_id,
    resource_name,
    details,
    ip_address,
    created_at
FROM audit_logs 
WHERE action IN ('CREATE', 'READ', 'UPDATE', 'DELETE')
ORDER BY created_at DESC;

-- Grant permissions (adjust as needed for your database setup)
-- GRANT SELECT, INSERT, UPDATE, DELETE ON audit_logs TO your_app_user;
-- GRANT USAGE, SELECT ON SEQUENCE audit_logs_id_seq TO your_app_user;
-- GRANT SELECT ON security_events TO your_app_user;
-- GRANT SELECT ON failed_logins TO your_app_user;
-- GRANT SELECT ON successful_logins TO your_app_user;
-- GRANT SELECT ON data_access_events TO your_app_user; 