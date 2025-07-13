# 📚 API Reference Guide

Complete documentation for all REST API endpoints in the Spring Boot E-Commerce application.

## Table of Contents
1. [Authentication](#authentication)
2. [User Management](#user-management)
3. [Product Management](#product-management)
4. [Category Management](#category-management)
5. [Order Management](#order-management)
6. [Role & Permission Management](#role--permission-management)
7. [Audit System](#audit-system)
8. [Notification System](#notification-system)

## Base URL
```
http://localhost:8080/api
```

## Authentication

### Register User
**POST** `/auth/register`

Register a new user account.

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "roles": ["USER"]
  },
  "message": "User registered successfully"
}
```

### Login
**POST** `/auth/login`

Authenticate user and get JWT token.

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "data": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login successful"
}
```

## User Management

### Get All Users
**GET** `/users`

**Headers:** `Authorization: Bearer <token>`

**Query Parameters:**
- `page` (int): Page number (default: 0)
- `size` (int): Page size (default: 10)
- `sortBy` (string): Sort field (default: id)
- `sortOrder` (string): Sort direction (asc/desc)

**Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "username": "john_doe",
        "email": "john@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "roles": ["USER"],
        "enabled": true,
        "createdAt": "2024-01-01T10:00:00Z"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "currentPage": 0
  },
  "message": "Users retrieved successfully"
}
```

### Get User by ID
**GET** `/users/{id}`

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "roles": ["USER"],
    "enabled": true,
    "createdAt": "2024-01-01T10:00:00Z"
  },
  "message": "User retrieved successfully"
}
```

### Update User
**PUT** `/users/{id}`

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "firstName": "Updated",
  "lastName": "Name",
  "email": "updated@example.com"
}
```

### Delete User
**DELETE** `/users/{id}`

**Headers:** `Authorization: Bearer <token>`

### Get User Profile
**GET** `/users/profile`

**Headers:** `Authorization: Bearer <token>`

## Product Management

### Get All Products (Public)
**GET** `/products/public`

**Query Parameters:**
- `page` (int): Page number
- `size` (int): Page size
- `category` (string): Category name
- `minPrice` (decimal): Minimum price
- `maxPrice` (decimal): Maximum price
- `sortBy` (string): Sort field
- `sortOrder` (string): Sort direction

**Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "name": "iPhone 15",
        "description": "Latest iPhone model",
        "price": 999.99,
        "discountPercentage": 10.0,
        "finalPrice": 899.99,
        "stockQuantity": 50,
        "category": {
          "id": 1,
          "name": "Electronics"
        },
        "status": "IN_STOCK"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "currentPage": 0
  },
  "message": "Products retrieved successfully"
}
```

### Get All Products (Admin)
**GET** `/products`

**Headers:** `Authorization: Bearer <token>`

### Get Product by ID
**GET** `/products/{id}`

**Headers:** `Authorization: Bearer <token>`

### Create Product
**POST** `/products`

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "name": "iPhone 15",
  "description": "Latest iPhone model",
  "price": 999.99,
  "stockQuantity": 50,
  "categoryId": 1,
  "discountPercentage": 10.0
}
```

### Update Product
**PUT** `/products/{id}`

**Headers:** `Authorization: Bearer <token>`

### Delete Product
**DELETE** `/products/{id}`

**Headers:** `Authorization: Bearer <token>`

### Update Product Stock
**PUT** `/products/{id}/stock`

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "quantity": 100
}
```

### Get Low Stock Products
**GET** `/products/low-stock`

**Headers:** `Authorization: Bearer <token>`

**Query Parameters:**
- `threshold` (int): Stock threshold (default: 10)

### Search Products
**GET** `/products/search`

**Query Parameters:**
- `keyword` (string): Search keyword
- `page` (int): Page number
- `size` (int): Page size

## Category Management

### Get All Categories
**GET** `/categories`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Electronics",
      "description": "Electronic devices and gadgets"
    }
  ],
  "message": "Categories retrieved successfully"
}
```

### Get Category by ID
**GET** `/categories/{id}`

### Create Category
**POST** `/categories`

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "name": "Electronics",
  "description": "Electronic devices and gadgets"
}
```

### Update Category
**PUT** `/categories/{id}`

**Headers:** `Authorization: Bearer <token>`

### Delete Category
**DELETE** `/categories/{id}`

**Headers:** `Authorization: Bearer <token>`

## Order Management

### Create Sale Order
**POST** `/products/{productId}/sale`

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "quantity": 2,
  "customerEmail": "customer@example.com"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "productId": 1,
    "productName": "iPhone 15",
    "quantity": 2,
    "unitPrice": 999.99,
    "discountPercentage": 10.0,
    "totalAmount": 1799.98,
    "customerEmail": "customer@example.com",
    "orderDate": "2024-01-01T10:00:00Z",
    "status": "COMPLETED"
  },
  "message": "Sale completed successfully"
}
```

### Get All Orders
**GET** `/orders`

**Headers:** `Authorization: Bearer <token>`

**Query Parameters:**
- `page` (int): Page number
- `size` (int): Page size
- `startDate` (date): Start date filter
- `endDate` (date): End date filter

### Get Order by ID
**GET** `/orders/{id}`

**Headers:** `Authorization: Bearer <token>`

## Role & Permission Management

### Get All Permissions
**GET** `/permissions`

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "PRODUCT_CREATE",
      "description": "Create new products"
    }
  ],
  "message": "Permissions retrieved successfully"
}
```

### Create Permission
**POST** `/permissions`

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "name": "PRODUCT_CREATE",
  "description": "Create new products"
}
```

### Get All Roles
**GET** `/roles`

**Headers:** `Authorization: Bearer <token>`

### Create Role
**POST** `/roles`

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "name": "PRODUCT_MANAGER",
  "description": "Manage products",
  "permissionIds": [1, 2, 3]
}
```

### Assign Roles to User
**PUT** `/users/{userId}/roles`

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "roleIds": [1, 2]
}
```

## Audit System

### Get Audit Logs
**GET** `/audit`

**Headers:** `Authorization: Bearer <token>`

**Query Parameters:**
- `username` (string): Filter by username
- `action` (string): Filter by action
- `level` (string): Filter by audit level
- `startDate` (date): Start date filter
- `endDate` (date): End date filter
- `page` (int): Page number
- `size` (int): Page size

**Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "username": "john_doe",
        "action": "LOGIN",
        "details": "User logged in successfully",
        "ipAddress": "192.168.1.1",
        "userAgent": "Mozilla/5.0...",
        "level": "INFO",
        "timestamp": "2024-01-01T10:00:00Z"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "currentPage": 0
  },
  "message": "Audit logs retrieved successfully"
}
```

### Get Audit Log by ID
**GET** `/audit/{id}`

**Headers:** `Authorization: Bearer <token>`

### Export Audit Logs
**GET** `/audit/export`

**Headers:** `Authorization: Bearer <token>`

**Query Parameters:**
- `format` (string): Export format (csv, json)
- `startDate` (date): Start date filter
- `endDate` (date): End date filter

### Get Audit Statistics
**GET** `/audit/statistics`

**Headers:** `Authorization: Bearer <token>`

**Query Parameters:**
- `startDate` (date): Start date filter
- `endDate` (date): End date filter

**Response:**
```json
{
  "success": true,
  "data": {
    "totalLogs": 1000,
    "logsByLevel": {
      "INFO": 800,
      "WARN": 150,
      "ERROR": 50
    },
    "logsByAction": {
      "LOGIN": 300,
      "PRODUCT_CREATE": 200,
      "ORDER_CREATE": 500
    },
    "topUsers": [
      {
        "username": "admin",
        "actionCount": 500
      }
    ]
  },
  "message": "Audit statistics retrieved successfully"
}
```

## Notification System

### Get All Notifications
**GET** `/notifications`

**Headers:** `Authorization: Bearer <token>`

**Query Parameters:**
- `page` (int): Page number
- `size` (int): Page size
- `type` (string): Notification type filter
- `read` (boolean): Read status filter

### Create Notification
**POST** `/notifications`

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "type": "EMAIL",
  "recipient": "admin@example.com",
  "subject": "System Alert",
  "message": "Database backup completed successfully"
}
```

### Mark Notification as Read
**PUT** `/notifications/{id}/read`

**Headers:** `Authorization: Bearer <token>`

### Delete Notification
**DELETE** `/notifications/{id}`

**Headers:** `Authorization: Bearer <token>`

## Error Responses

### Validation Error (422)
```json
{
  "success": false,
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "Email must be valid"
    }
  ]
}
```

### Unauthorized (401)
```json
{
  "success": false,
  "message": "Unauthorized access",
  "timestamp": "2024-01-01T10:00:00Z"
}
```

### Forbidden (403)
```json
{
  "success": false,
  "message": "Access denied",
  "timestamp": "2024-01-01T10:00:00Z"
}
```

### Not Found (404)
```json
{
  "success": false,
  "message": "Resource not found",
  "timestamp": "2024-01-01T10:00:00Z"
}
```

### Internal Server Error (500)
```json
{
  "success": false,
  "message": "Internal server error",
  "timestamp": "2024-01-01T10:00:00Z"
}
```

## Rate Limiting

The API implements rate limiting to prevent abuse:
- **Authentication endpoints**: 5 requests per minute
- **Other endpoints**: 100 requests per minute per user

## Pagination

All list endpoints support pagination with the following parameters:
- `page`: Page number (0-based, default: 0)
- `size`: Page size (default: 10, max: 100)

**Response format:**
```json
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 10,
  "currentPage": 0,
  "size": 10,
  "first": true,
  "last": false
}
```

## Sorting

Most endpoints support sorting with:
- `sortBy`: Field name to sort by
- `sortOrder`: Sort direction (asc/desc)

**Example:**
```
GET /api/products?sortBy=price&sortOrder=desc
```

## Filtering

Many endpoints support filtering with various parameters:
- Date ranges: `startDate`, `endDate`
- Text search: `keyword`, `name`
- Numeric ranges: `minPrice`, `maxPrice`
- Status filters: `status`, `enabled`

## File Upload

For future features, file uploads will be supported with:
- **Content-Type**: `multipart/form-data`
- **Max file size**: 10MB
- **Supported formats**: JPG, PNG, PDF, DOC

## WebSocket Support

Real-time notifications are available via WebSocket:
- **Endpoint**: `ws://localhost:8080/ws`
- **Authentication**: JWT token in query parameter
- **Events**: Order updates, stock alerts, system notifications

## API Versioning

The API uses URL versioning:
- Current version: `/api/v1/`
- Future versions: `/api/v2/`, `/api/v3/`

## Health Check

**GET** `/actuator/health`

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```

## Metrics

**GET** `/actuator/metrics**

Available metrics:
- `http.server.requests`: HTTP request metrics
- `jvm.memory.used`: JVM memory usage
- `process.cpu.usage`: CPU usage
- `hikaricp.connections`: Database connection pool

---

For more information, see the [Complete User Guide](USER_GUIDE.md) or [Quick Start Guide](QUICK_START.md). 