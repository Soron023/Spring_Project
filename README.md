# Spring Boot Application with User Management and Product Categories

A comprehensive Spring Boot application featuring user management, product categories, and JWT-based authentication with full CRUD operations for all modules.

## 🚀 Features

### Authentication & Authorization
- JWT-based authentication
- Role-based access control (USER, MODERATOR, ADMIN)
- User registration and login
- Password encryption with BCrypt
- Token validation and refresh

### User Management
- Complete CRUD operations for users
- User profile management
- Account activation/deactivation
- Password management
- Role assignment

### Product Management
- Complete CRUD operations for products
- Product categorization
- Stock management
- Product search and filtering
- Price range filtering
- Availability status

### Category Management
- Complete CRUD operations for categories
- Category-product relationships
- Category search functionality
- Product count per category

### Security Features
- JWT token authentication
- Role-based authorization
- Input validation
- Global exception handling
- CORS configuration

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.2.0
- **Database**: MySQL 8.0
- **Security**: Spring Security with JWT
- **ORM**: Spring Data JPA with Hibernate
- **Validation**: Bean Validation (Jakarta)
- **Build Tool**: Maven
- **Java Version**: 17

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd spring-boot-app
```

### 2. Database Setup
Create a MySQL database and update the configuration in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring_boot_app?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. JWT Configuration
Update the JWT secret in `application.properties`:
```properties
jwt.secret=your-very-long-and-secure-secret-key-here
```

### 4. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "user",
  "password": "password123"
}
```

#### Validate Token
```http
POST /api/auth/validate
Authorization: Bearer <jwt-token>
```

### User Management Endpoints

#### Get All Users (Admin only)
```http
GET /api/users
Authorization: Bearer <jwt-token>
```

#### Get User by ID
```http
GET /api/users/{id}
Authorization: Bearer <jwt-token>
```

#### Update User
```http
PUT /api/users/{id}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "firstName": "Updated",
  "lastName": "Name",
  "email": "updated@example.com",
  "role": "USER"
}
```

#### Delete User (Admin only)
```http
DELETE /api/users/{id}
Authorization: Bearer <jwt-token>
```

### Category Management Endpoints

#### Get All Categories
```http
GET /api/categories
```

#### Create Category (Admin/Moderator)
```http
POST /api/categories
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "New Category",
  "description": "Category description"
}
```

#### Update Category (Admin/Moderator)
```http
PUT /api/categories/{id}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Updated Category",
  "description": "Updated description"
}
```

#### Delete Category (Admin only)
```http
DELETE /api/categories/{id}
Authorization: Bearer <jwt-token>
```

### Product Management Endpoints

#### Get All Products
```http
GET /api/products
```

#### Create Product (Admin/Moderator)
```http
POST /api/products
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "New Product",
  "description": "Product description",
  "price": 99.99,
  "stockQuantity": 100,
  "categoryId": 1
}
```

#### Update Product (Admin/Moderator)
```http
PUT /api/products/{id}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Updated Product",
  "description": "Updated description",
  "price": 89.99,
  "stockQuantity": 50,
  "categoryId": 1
}
```

#### Delete Product (Admin only)
```http
DELETE /api/products/{id}
Authorization: Bearer <jwt-token>
```

#### Search Products
```http
GET /api/products/search?keyword=phone&page=0&size=10
```

#### Filter by Price Range
```http
GET /api/products/price-range?minPrice=50&maxPrice=200&page=0&size=10
```

### Public Endpoints (No Authentication Required)

#### Get Public Products
```http
GET /api/public/products
```

#### Get Public Categories
```http
GET /api/public/categories
```

## 🔐 Default Users

The application comes with pre-configured users:

| Username | Password | Role | Email |
|----------|----------|------|-------|
| admin | admin123 | ADMIN | admin@example.com |
| moderator | mod123 | MODERATOR | moderator@example.com |
| user | user123 | USER | user@example.com |

## 🏗️ Project Structure

```
src/main/java/com/example/springbootapp/
├── config/                 # Configuration classes
│   ├── SecurityConfig.java
│   └── DataInitializer.java
├── controller/             # REST controllers
│   ├── AuthController.java
│   ├── UserController.java
│   ├── CategoryController.java
│   ├── ProductController.java
│   └── PublicController.java
├── dto/                   # Data Transfer Objects
│   ├── UserRegistrationDto.java
│   ├── LoginDto.java
│   ├── AuthResponseDto.java
│   ├── ProductDto.java
│   └── CategoryDto.java
├── entity/                # JPA entities
│   ├── User.java
│   ├── Product.java
│   ├── Category.java
│   └── Role.java
├── exception/             # Custom exceptions
│   ├── ResourceNotFoundException.java
│   ├── UserAlreadyExistsException.java
│   └── GlobalExceptionHandler.java
├── repository/            # Data access layer
│   ├── UserRepository.java
│   ├── ProductRepository.java
│   └── CategoryRepository.java
├── security/              # Security components
│   ├── JwtTokenProvider.java
│   └── JwtAuthenticationFilter.java
├── service/               # Business logic
│   ├── UserService.java
│   ├── ProductService.java
│   ├── CategoryService.java
│   └── AuthService.java
└── SpringBootAppApplication.java
```

## 🔧 Configuration

### Application Properties
Key configuration options in `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/spring_boot_app
spring.datasource.username=root
spring.datasource.password=password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=your-secret-key
jwt.expiration=86400000

# Server
server.port=8080
```

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Manual Testing with curl

#### Register a new user
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

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

#### Get all products (with token)
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🚀 Deployment

### Build JAR
```bash
mvn clean package
```

### Run JAR
```bash
java -jar target/spring-boot-app-0.0.1-SNAPSHOT.jar
```

### Docker (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/spring-boot-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions, please open an issue in the repository or contact the development team.

## 🔄 Version History

- **v1.0.0** - Initial release with full CRUD operations
- Complete user management system
- Product and category management
- JWT authentication and authorization
- Comprehensive API documentation # Spring_Project
