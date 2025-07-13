# 🚀 Getting Started - Spring Boot Application

## Quick Start (5 minutes)

### 1. Prerequisites Check
```bash
# Check Java version (needs Java 17+)
java -version

# Check Maven
mvn -version

# Check PostgreSQL (optional - will be checked by script)
psql --version
```

### 2. Run the Quick Start Script
```bash
./quick-start.sh
```

This script will:
- ✅ Check all prerequisites
- 🏗️ Build the project
- 🗄️ Test database connection
- 🚀 Start the application

### 3. Alternative Manual Start
```bash
# Build the project
mvn clean install

# Start the application
mvn spring-boot:run
```

The application will be available at: **http://localhost:8080**

## 🧪 Test the API

### Option 1: Use the Test Script
```bash
./test-api.sh
```

### Option 2: Manual Testing

#### 1. Register a User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

#### 2. Login and Get Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

#### 3. Use the Token
```bash
# Store token
TOKEN="your_jwt_token_here"

# Get all products
curl -X GET http://localhost:8080/api/products

# Get user profile (with token)
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer $TOKEN"
```

## 📚 What's Available

### 🔐 Authentication
- User registration and login
- JWT token-based authentication
- Role-based access control

### 👥 User Management
- Complete CRUD operations
- Role assignment
- Profile management

### 📦 Product Management
- Product catalog with categories
- Stock management
- Search and filtering
- Sales functionality

### 🏷️ Category Management
- Product categorization
- Category CRUD operations

### 🔐 Role & Permission System
- Advanced authorization
- Permission management
- Role assignment

### 🌍 Internationalization
- Multi-language support
- Localized error messages

## 🛠️ Configuration

### Database Setup
The application uses PostgreSQL by default. Update `src/main/resources/application.properties`:

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/spring_boot_app
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT
jwt.secret=your-secure-secret-key
```

### Create Database
```sql
CREATE DATABASE spring_boot_app;
```

## 📖 Documentation

- **Complete Guide**: `USER_GUIDE.md`
- **API Reference**: See Postman collection in the project
- **Quick Start**: This file

## 🚨 Troubleshooting

### Common Issues

1. **Port 8080 in use**
   ```bash
   # Change port in application.properties
   server.port=8081
   ```

2. **Database connection failed**
   - Ensure PostgreSQL is running
   - Check credentials in `application.properties`
   - Create database if it doesn't exist

3. **Build fails**
   ```bash
   # Clean and rebuild
   mvn clean install
   ```

## 🎯 Next Steps

1. **Explore the API**: Use the test script or Postman collection
2. **Add Products**: Create categories and products
3. **Test Authentication**: Register users and test roles
4. **Customize**: Modify the application for your needs

## 📞 Need Help?

- Check the logs for error messages
- Review `USER_GUIDE.md` for detailed documentation
- Ensure all prerequisites are met
- Verify database configuration

---

**Happy Coding! 🎉** 