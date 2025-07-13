-- Database initialization script for Spring Boot Application
-- This script runs when the PostgreSQL container starts for the first time

-- Create database if not exists (handled by environment variables)
-- CREATE DATABASE spring_boot_app;

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create additional schemas if needed
-- CREATE SCHEMA IF NOT EXISTS audit;

-- Grant permissions
-- GRANT ALL PRIVILEGES ON DATABASE spring_boot_app TO postgres;

-- Create any additional users or roles if needed
-- CREATE USER app_user WITH PASSWORD 'app_password';
-- GRANT CONNECT ON DATABASE spring_boot_app TO app_user;

-- Set timezone
SET timezone = 'UTC';

-- Log initialization
SELECT 'Database initialization completed at ' || now() as init_status; 