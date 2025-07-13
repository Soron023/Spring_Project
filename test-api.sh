#!/bin/bash

# API Testing Script for Spring Boot Application
# This script tests the main API endpoints

BASE_URL="http://localhost:8080"
TOKEN=""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_success() {
    echo -e "${GREEN}✓${NC} $1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
}

print_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

# Function to make HTTP requests
make_request() {
    local method=$1
    local endpoint=$2
    local data=$3
    local auth_header=$4
    
    local curl_cmd="curl -s -w '\nHTTP_STATUS:%{http_code}' -X $method $BASE_URL$endpoint"
    
    if [ ! -z "$data" ]; then
        curl_cmd="$curl_cmd -H 'Content-Type: application/json' -d '$data'"
    fi
    
    if [ ! -z "$auth_header" ]; then
        curl_cmd="$curl_cmd -H 'Authorization: Bearer $auth_header'"
    fi
    
    local response=$(eval $curl_cmd)
    local http_status=$(echo "$response" | grep "HTTP_STATUS:" | cut -d':' -f2)
    local body=$(echo "$response" | sed '/HTTP_STATUS:/d')
    
    echo "$http_status|$body"
}

echo "🧪 Spring Boot Application API Testing"
echo "======================================"

# Check if server is running
print_info "Checking if server is running..."
response=$(make_request "GET" "/api/products" "" "")
http_status=$(echo "$response" | cut -d'|' -f1)

if [ "$http_status" = "200" ]; then
    print_success "Server is running"
else
    print_error "Server is not running. Please start the application first."
    print_info "Run: mvn spring-boot:run"
    exit 1
fi

echo ""
echo "🔐 Testing Authentication"
echo "========================="

# Test user registration
print_info "Testing user registration..."
register_data='{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "firstName": "Test",
  "lastName": "User"
}'

response=$(make_request "POST" "/api/auth/register" "$register_data" "")
http_status=$(echo "$response" | cut -d'|' -f1)
body=$(echo "$response" | cut -d'|' -f2)

if [ "$http_status" = "200" ] || [ "$http_status" = "201" ]; then
    print_success "User registration successful"
    # Extract token if present
    TOKEN=$(echo "$body" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
else
    print_warning "User registration failed (user might already exist): $body"
fi

# Test user login
print_info "Testing user login..."
login_data='{
  "username": "testuser",
  "password": "password123"
}'

response=$(make_request "POST" "/api/auth/login" "$login_data" "")
http_status=$(echo "$response" | cut -d'|' -f1)
body=$(echo "$response" | cut -d'|' -f2)

if [ "$http_status" = "200" ]; then
    print_success "User login successful"
    TOKEN=$(echo "$body" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
    print_info "Token obtained: ${TOKEN:0:20}..."
else
    print_error "User login failed: $body"
    exit 1
fi

echo ""
echo "📦 Testing Product Management"
echo "============================="

# Test get all products
print_info "Testing get all products..."
response=$(make_request "GET" "/api/products" "" "")
http_status=$(echo "$response" | cut -d'|' -f1)

if [ "$http_status" = "200" ]; then
    print_success "Get all products successful"
else
    print_error "Get all products failed: $(echo "$response" | cut -d'|' -f2)"
fi

# Test product search
print_info "Testing product search..."
response=$(make_request "GET" "/api/products/search?keyword=test&page=0&size=5" "" "")
http_status=$(echo "$response" | cut -d'|' -f1)

if [ "$http_status" = "200" ]; then
    print_success "Product search successful"
else
    print_error "Product search failed: $(echo "$response" | cut -d'|' -f2)"
fi

echo ""
echo "🏷️  Testing Category Management"
echo "==============================="

# Test get all categories
print_info "Testing get all categories..."
response=$(make_request "GET" "/api/categories" "" "")
http_status=$(echo "$response" | cut -d'|' -f1)

if [ "$http_status" = "200" ]; then
    print_success "Get all categories successful"
else
    print_error "Get all categories failed: $(echo "$response" | cut -d'|' -f2)"
fi

echo ""
echo "👥 Testing User Management"
echo "=========================="

# Test get user profile
print_info "Testing get user profile..."
response=$(make_request "GET" "/api/users/me" "" "$TOKEN")
http_status=$(echo "$response" | cut -d'|' -f1)

if [ "$http_status" = "200" ]; then
    print_success "Get user profile successful"
else
    print_error "Get user profile failed: $(echo "$response" | cut -d'|' -f2)"
fi

echo ""
echo "🔐 Testing Role Management"
echo "=========================="

# Test get all roles
print_info "Testing get all roles..."
response=$(make_request "GET" "/api/roles" "" "$TOKEN")
http_status=$(echo "$response" | cut -d'|' -f1)

if [ "$http_status" = "200" ]; then
    print_success "Get all roles successful"
else
    print_error "Get all roles failed: $(echo "$response" | cut -d'|' -f2)"
fi

echo ""
echo "✅ API Testing Complete!"
echo "======================="
print_info "All endpoints have been tested"
print_info "Check the results above for any failures"
print_info "For detailed API documentation, see USER_GUIDE.md" 