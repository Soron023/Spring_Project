# Spring Boot E-Commerce Application - Complete User Guide

## Table of Contents
1. [Project Overview](#project-overview)
2. [Prerequisites](#prerequisites)
3. [Initial Setup](#initial-setup)
4. [Project Structure](#project-structure)
5. [Core Features](#core-features)
6. [Authentication & Authorization](#authentication--authorization)
7. [Product Management](#product-management)
8. [Order Management](#order-management)
9. [Notification System](#notification-system)
10. [Audit System](#audit-system)
11. [Role & Permission Management](#role--permission-management)
12. [API Testing](#api-testing)
13. [Development Workflow](#development-workflow)
14. [Deployment](#deployment)
15. [Monitoring & Maintenance](#monitoring--maintenance)
16. [Troubleshooting](#troubleshooting)

## Project Overview

This is a comprehensive Spring Boot e-commerce application with the following features:
- **Authentication & Authorization**: JWT-based authentication with role-based access control
- **Product Management**: CRUD operations with inventory tracking and discount support
- **Order Management**: Sale processing with stock validation
- **Notification System**: Automated low-stock alerts and order notifications
- **Audit System**: Complete activity tracking and logging
- **Role & Permission Management**: Flexible permission system
- **Internationalization**: Multi-language support
- **Docker Support**: Containerized deployment
- **CI/CD Pipeline**: Jenkins automation
- **Monitoring**: Prometheus and Grafana integration

## Prerequisites

Before starting, ensure you have the following installed:

### Required Software
- **Java 17+** (OpenJDK or Oracle JDK)
- **Maven 3.6+**
- **Git**
- **Docker & Docker Compose** (for containerized deployment)
- **PostgreSQL 13+** (or use Docker)

### Optional Software
- **IntelliJ IDEA** or **Eclipse** (for development)
- **Postman** (for API testing)
- **Jenkins** (for CI/CD)

### System Requirements
- **RAM**: Minimum 4GB, Recommended 8GB+
- **Storage**: 2GB free space
- **OS**: Windows, macOS, or Linux

## Initial Setup

### 1. Clone the Repository
```bash
git clone <your-repository-url>
cd spring-boot-app
```

### 2. Database Setup

#### Option A: Using Docker (Recommended)
```bash
# Start PostgreSQL with Docker
docker run --name postgres-ecommerce \
  -e POSTGRES_DB=ecommerce \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d postgres:13
```

#### Option B: Local PostgreSQL Installation
1. Install PostgreSQL on your system
2. Create a database named `ecommerce`
3. Create a user with appropriate permissions

### 3. Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=postgres
spring.datasource.password=password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Configuration
jwt.secret=your-secret-key-here
jwt.expiration=86400000

# Server Configuration
server.port=8080
```

### 4. Build and Run

```bash
# Clean and compile
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Project Structure

```
spring-boot-app/
├── src/main/java/com/example/springbootapp/
│   ├── config/                 # Configuration classes
│   ├── controller/             # REST controllers
│   ├── dto/                   # Data Transfer Objects
│   ├── entity/                # JPA entities
│   ├── exception/             # Custom exceptions
│   ├── interceptor/           # Request interceptors
│   ├── repository/            # Data access layer
│   ├── scheduler/             # Scheduled tasks
│   ├── security/              # Security configuration
│   ├── service/               # Business logic interfaces
│   │   └── impl/             # Service implementations
│   ├── specification/         # JPA specifications
│   └── util/                  # Utility classes
├── src/main/resources/
│   ├── db/migration/          # Flyway migration scripts
│   ├── messages/              # Internationalization files
│   └── application.properties # Application configuration
└── src/test/                  # Test files
```

## Core Features

### 1. User Management

#### Register a New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

Response will include a JWT token:
```json
{
  "success": true,
  "data": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login successful"
}
```

### 2. Product Management

#### Create a Product (Admin Only)
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "iPhone 15",
    "description": "Latest iPhone model",
    "price": 999.99,
    "stockQuantity": 50,
    "categoryId": 1,
    "discountPercentage": 10.0
  }'
```

#### Get All Products (Public)
```bash
curl -X GET http://localhost:8080/api/products/public
```

#### Get Products with Filters
```bash
curl -X GET "http://localhost:8080/api/products?category=Electronics&minPrice=100&maxPrice=1000&sortBy=price&sortOrder=asc"
```

### 3. Order Management

#### Create a Sale Order
```bash
curl -X POST http://localhost:8080/api/products/1/sale \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "quantity": 2,
    "customerEmail": "customer@example.com"
  }'
```

### 4. Category Management

#### Create Category
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices and gadgets"
  }'
```

## Authentication & Authorization

### JWT Token Usage

After login, include the JWT token in all protected requests:

```bash
curl -X GET http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Role-Based Access

The application supports the following roles:
- **USER**: Basic user access
- **ADMIN**: Full administrative access
- **MODERATOR**: Limited administrative access

### Security Features
- JWT-based authentication
- Role-based authorization
- Password encryption
- Request validation
- CORS configuration

## Product Management

### Product Features
- **CRUD Operations**: Create, read, update, delete products
- **Inventory Tracking**: Automatic stock management
- **Discount Support**: Percentage-based discounts
- **Category Organization**: Hierarchical product categories
- **Advanced Filtering**: Price, category, availability filters
- **Low Stock Alerts**: Automatic notifications

### Product States
- **IN_STOCK**: Available for purchase
- **LOW_STOCK**: Quantity below threshold
- **OUT_OF_STOCK**: No inventory available

### Stock Management
```bash
# Check low stock products
curl -X GET http://localhost:8080/api/products/low-stock \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Update product stock
curl -X PUT http://localhost:8080/api/products/1/stock \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "quantity": 100
  }'
```

## Order Management

### Order Process
1. **Product Selection**: Choose product and quantity
2. **Stock Validation**: Check availability
3. **Price Calculation**: Apply discounts
4. **Order Creation**: Generate order record
5. **Stock Update**: Reduce inventory
6. **Notification**: Send order confirmation

### Order Features
- **Automatic Stock Reduction**: Inventory updated on purchase
- **Discount Application**: Automatic price calculation
- **Order History**: Complete transaction records
- **Email Notifications**: Order confirmations

## Notification System

### Automatic Notifications
- **Low Stock Alerts**: Daily notifications for products below threshold
- **Order Confirmations**: Email notifications for successful purchases
- **System Alerts**: Important system events

### Notification Types
- **EMAIL**: Email notifications
- **SMS**: Text message notifications (future feature)
- **IN_APP**: In-application notifications

### Manual Notifications
```bash
# Send custom notification
curl -X POST http://localhost:8080/api/notifications \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "type": "EMAIL",
    "recipient": "admin@example.com",
    "subject": "System Alert",
    "message": "Database backup completed"
  }'
```

## Audit System

### Automatic Audit Logging
The system automatically logs:
- **User Actions**: Login, logout, data modifications
- **Security Events**: Failed login attempts, permission violations
- **Data Access**: CRUD operations on sensitive data
- **System Events**: Application startup, configuration changes

### Audit Features
- **Comprehensive Logging**: All user activities tracked
- **Advanced Queries**: Filter by user, action, date range
- **CSV Export**: Download audit logs for analysis
- **Retention Policy**: Automatic cleanup of old logs

### Audit Queries
```bash
# Get audit logs for specific user
curl -X GET "http://localhost:8080/api/audit?username=john_doe&startDate=2024-01-01&endDate=2024-12-31" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Export audit logs
curl -X GET "http://localhost:8080/api/audit/export?format=csv" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  --output audit_logs.csv
```

### Audit Levels
- **INFO**: General information
- **WARN**: Warning messages
- **ERROR**: Error conditions
- **SECURITY**: Security-related events

## Role & Permission Management

### Permission System
- **Granular Permissions**: Fine-grained access control
- **Role Assignment**: Assign permissions to roles
- **User Roles**: Assign roles to users
- **Dynamic Permissions**: Runtime permission checking

### Permission Management
```bash
# Create permission
curl -X POST http://localhost:8080/api/permissions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "PRODUCT_CREATE",
    "description": "Create new products"
  }'

# Create role
curl -X POST http://localhost:8080/api/roles \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "PRODUCT_MANAGER",
    "description": "Manage products",
    "permissionIds": [1, 2, 3]
  }'

# Assign role to user
curl -X PUT http://localhost:8080/api/users/1/roles \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "roleIds": [1, 2]
  }'
```

## API Testing

### Using Postman Collection
1. Import `Spring_Boot_App_API.postman_collection.json` into Postman
2. Set up environment variables:
   - `baseUrl`: `http://localhost:8080`
   - `token`: Your JWT token (after login)

### Using cURL Scripts
```bash
# Run the API testing script
chmod +x scripts/test-api.sh
./scripts/test-api.sh
```

### Automated Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Generate test coverage report
mvn jacoco:report
```

## Development Workflow

### 1. Local Development
```bash
# Start development environment
docker-compose -f docker-compose.dev.yml up -d

# Run application in development mode
mvn spring-boot:run -Dspring.profiles.active=dev
```

### 2. Code Quality
```bash
# Run code analysis
mvn spotbugs:check

# Format code
mvn spring-javaformat:apply

# Check code style
mvn checkstyle:check
```

### 3. Database Migrations
```bash
# Create new migration
# Add SQL file to src/main/resources/db/migration/

# Run migrations
mvn flyway:migrate

# Check migration status
mvn flyway:info
```

### 4. Testing Strategy
- **Unit Tests**: Test individual components
- **Integration Tests**: Test component interactions
- **API Tests**: Test REST endpoints
- **Performance Tests**: Load testing (future)

## Deployment

### 1. Docker Deployment

#### Development Environment
```bash
# Build and run development environment
docker-compose -f docker-compose.dev.yml up --build
```

#### Production Environment
```bash
# Build production image
docker build -t ecommerce-app:latest .

# Run production environment
docker-compose -f docker-compose.prod.yml up -d
```

### 2. Traditional Deployment

#### Build Application
```bash
# Create executable JAR
mvn clean package -DskipTests

# Run application
java -jar target/spring-boot-app-1.0.0.jar
```

#### Production Configuration
```properties
# application-prod.properties
spring.profiles.active=prod
server.port=8080
spring.datasource.url=jdbc:postgresql://prod-db:5432/ecommerce
logging.level.root=WARN
```

### 3. Cloud Deployment

#### AWS Deployment
```bash
# Deploy to AWS ECS
./scripts/deploy-aws.sh

# Deploy to AWS EC2
./scripts/deploy-ec2.sh
```

#### Kubernetes Deployment
```bash
# Deploy to Kubernetes
kubectl apply -f k8s/
```

## Monitoring & Maintenance

### 1. Application Monitoring

#### Prometheus Metrics
- **JVM Metrics**: Memory, CPU, garbage collection
- **Application Metrics**: Request rates, response times
- **Business Metrics**: Orders, revenue, user activity

#### Grafana Dashboards
- **System Health**: Application performance overview
- **Business Metrics**: Sales and user activity
- **Error Tracking**: Error rates and types

### 2. Log Management
```bash
# View application logs
docker logs ecommerce-app

# View database logs
docker logs postgres-ecommerce

# Monitor log files
tail -f logs/application.log
```

### 3. Database Maintenance
```bash
# Backup database
pg_dump -h localhost -U postgres ecommerce > backup.sql

# Restore database
psql -h localhost -U postgres ecommerce < backup.sql

# Run database maintenance
mvn flyway:repair
```

### 4. Performance Optimization
- **Database Indexing**: Optimize query performance
- **Caching**: Implement Redis caching (future)
- **Connection Pooling**: Optimize database connections
- **Load Balancing**: Distribute traffic (future)

## Troubleshooting

### Common Issues

#### 1. Database Connection Issues
```bash
# Check database status
docker ps | grep postgres

# Test database connection
psql -h localhost -U postgres -d ecommerce

# Reset database
docker-compose down -v
docker-compose up -d
```

#### 2. JWT Token Issues
```bash
# Check token expiration
# Verify token format
# Ensure proper Authorization header
```

#### 3. Build Issues
```bash
# Clean and rebuild
mvn clean install

# Check Java version
java -version

# Verify Maven installation
mvn -version
```

#### 4. Port Conflicts
```bash
# Check port usage
lsof -i :8080

# Change application port
java -jar app.jar --server.port=8081
```

### Error Codes

| Code | Description | Solution |
|------|-------------|----------|
| 401 | Unauthorized | Check JWT token |
| 403 | Forbidden | Verify user permissions |
| 404 | Not Found | Check resource existence |
| 422 | Validation Error | Verify request data |
| 500 | Internal Server Error | Check application logs |

### Debug Mode
```bash
# Enable debug logging
java -jar app.jar --logging.level.com.example=DEBUG

# Enable SQL logging
java -jar app.jar --spring.jpa.show-sql=true
```

### Support Resources
- **Application Logs**: `logs/application.log`
- **Database Logs**: PostgreSQL logs
- **Docker Logs**: `docker logs <container-name>`
- **Maven Logs**: `mvn -X` for verbose output

## Best Practices

### 1. Security
- Use strong passwords
- Regularly rotate JWT secrets
- Implement rate limiting
- Monitor security events
- Keep dependencies updated

### 2. Performance
- Use database indexes
- Implement caching
- Monitor application metrics
- Optimize database queries
- Use connection pooling

### 3. Development
- Write unit tests
- Follow coding standards
- Use meaningful commit messages
- Review code before merging
- Document API changes

### 4. Deployment
- Use environment-specific configurations
- Implement health checks
- Set up monitoring
- Plan for rollbacks
- Test in staging environment

## Conclusion

This comprehensive guide covers all aspects of the Spring Boot e-commerce application. The application is designed to be scalable, maintainable, and production-ready with features like:

- **Complete Authentication System**
- **Product Management with Inventory**
- **Order Processing**
- **Notification System**
- **Audit Logging**
- **Role-Based Access Control**
- **Docker Support**
- **CI/CD Pipeline**
- **Monitoring and Alerting**

For additional support or questions, refer to the project documentation or contact the development team. 