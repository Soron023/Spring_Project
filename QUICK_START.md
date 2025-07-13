# 🚀 Quick Start Guide

Get your Spring Boot E-Commerce application running in 5 minutes!

## Prerequisites Check

```bash
# Check if you have the required software
java -version    # Should be Java 17+
mvn -version     # Should be Maven 3.6+
docker --version # Should be Docker 20+
```

## 1. Clone and Setup

```bash
# Clone the repository
git clone <your-repository-url>
cd spring-boot-app

# Start database with Docker
docker run --name postgres-ecommerce \
  -e POSTGRES_DB=ecommerce \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d postgres:13
```

## 2. Configure Application

Edit `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=postgres
spring.datasource.password=password

# JWT
jwt.secret=your-secret-key-here
jwt.expiration=86400000

# Server
server.port=8080
```

## 3. Build and Run

```bash
# Build the application
mvn clean compile

# Run the application
mvn spring-boot:run
```

🎉 **Application is running at: http://localhost:8080**

## 4. Test the API

### Register a User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@example.com",
    "password": "admin123",
    "firstName": "Admin",
    "lastName": "User"
  }'
```

### Login and Get Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### Create a Category
```bash
# Replace YOUR_TOKEN with the token from login response
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices"
  }'
```

### Create a Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "iPhone 15",
    "description": "Latest iPhone",
    "price": 999.99,
    "stockQuantity": 50,
    "categoryId": 1,
    "discountPercentage": 10.0
  }'
```

### View Products (Public)
```bash
curl -X GET http://localhost:8080/api/products/public
```

## 5. What's Next?

- 📖 Read the [Complete User Guide](USER_GUIDE.md)
- 🧪 Test with [Postman Collection](Spring_Boot_App_API.postman_collection.json)
- 🐳 Use [Docker Compose](docker-compose.dev.yml) for development
- 📊 Set up [Monitoring](monitoring/) with Prometheus & Grafana

## Common Issues

### Database Connection Failed
```bash
# Check if PostgreSQL is running
docker ps | grep postgres

# Restart if needed
docker restart postgres-ecommerce
```

### Port Already in Use
```bash
# Change port in application.properties
server.port=8081
```

### Build Errors
```bash
# Clean and rebuild
mvn clean install
```

## Quick Commands

```bash
# Start everything with Docker
docker-compose -f docker-compose.dev.yml up -d

# Run tests
mvn test

# Check application health
curl http://localhost:8080/actuator/health

# View logs
docker logs ecommerce-app
```

---

**Need help?** Check the [Troubleshooting](USER_GUIDE.md#troubleshooting) section in the full guide. 