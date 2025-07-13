#!/bin/bash

# Spring Boot Application Quick Start Script
# This script helps you quickly set up and run the Spring Boot application

set -e  # Exit on any error

echo "🚀 Spring Boot Application Quick Start"
echo "======================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}✓${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
}

print_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

# Check prerequisites
echo ""
echo "📋 Checking prerequisites..."

# Check Java
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ge 17 ]; then
        print_status "Java $JAVA_VERSION found"
    else
        print_error "Java 17 or higher required. Found version $JAVA_VERSION"
        exit 1
    fi
else
    print_error "Java not found. Please install Java 17 or higher"
    exit 1
fi

# Check Maven
if command -v mvn &> /dev/null; then
    print_status "Maven found"
else
    print_error "Maven not found. Please install Maven 3.6 or higher"
    exit 1
fi

# Check if PostgreSQL is running
if command -v psql &> /dev/null; then
    if pg_isready -q; then
        print_status "PostgreSQL is running"
    else
        print_warning "PostgreSQL is not running. Please start PostgreSQL"
        echo "   On macOS: brew services start postgresql"
        echo "   On Ubuntu: sudo systemctl start postgresql"
        echo "   On Windows: Start PostgreSQL service"
    fi
else
    print_warning "PostgreSQL not found. Please install PostgreSQL"
fi

echo ""
echo "🔧 Setting up the application..."

# Check if application.properties exists
if [ ! -f "src/main/resources/application.properties" ]; then
    print_error "application.properties not found"
    exit 1
fi

# Build the project
echo ""
echo "🏗️  Building the project..."
if mvn clean install -q; then
    print_status "Project built successfully"
else
    print_error "Build failed. Please check the errors above"
    exit 1
fi

# Check if database is accessible
echo ""
echo "🗄️  Checking database connection..."
if mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=test" -q &> /dev/null & then
    PID=$!
    sleep 10
    if kill -0 $PID 2>/dev/null; then
        kill $PID
        print_status "Database connection successful"
    else
        print_warning "Database connection failed. Please check your database configuration"
        print_info "Make sure PostgreSQL is running and the database exists"
        print_info "You can create the database with:"
        echo "   createdb spring_boot_app"
    fi
else
    print_warning "Could not test database connection"
fi

echo ""
echo "🎯 Quick Start Options:"
echo "======================="
echo "1. Run in development mode (with hot reload)"
echo "2. Run in production mode"
echo "3. Run tests"
echo "4. Show API documentation"
echo "5. Exit"

read -p "Choose an option (1-5): " choice

case $choice in
    1)
        echo ""
        echo "🚀 Starting development server..."
        print_info "The application will be available at http://localhost:8080"
        print_info "Press Ctrl+C to stop the server"
        mvn spring-boot:run
        ;;
    2)
        echo ""
        echo "🚀 Starting production server..."
        print_info "The application will be available at http://localhost:8080"
        print_info "Press Ctrl+C to stop the server"
        java -jar target/spring-boot-app-0.0.1-SNAPSHOT.jar
        ;;
    3)
        echo ""
        echo "🧪 Running tests..."
        mvn test
        ;;
    4)
        echo ""
        echo "📚 API Documentation:"
        echo "===================="
        echo "• Authentication: POST /api/auth/register, POST /api/auth/login"
        echo "• Users: GET/POST/PUT/DELETE /api/users"
        echo "• Products: GET/POST/PUT/DELETE /api/products"
        echo "• Categories: GET/POST/PUT/DELETE /api/categories"
        echo "• Roles: GET/POST/PUT/DELETE /api/roles"
        echo ""
        echo "📖 For detailed documentation, see USER_GUIDE.md"
        ;;
    5)
        echo "👋 Goodbye!"
        exit 0
        ;;
    *)
        print_error "Invalid option"
        exit 1
        ;;
esac 