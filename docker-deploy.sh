#!/bin/bash

# Docker Deployment Script for Spring Boot Application
# This script helps you manage Docker containers for different environments

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
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

# Function to show usage
show_usage() {
    echo "🐳 Docker Deployment Script for Spring Boot Application"
    echo "======================================================"
    echo ""
    echo "Usage: $0 [COMMAND] [ENVIRONMENT]"
    echo ""
    echo "Commands:"
    echo "  build     - Build Docker image"
    echo "  start     - Start containers"
    echo "  stop      - Stop containers"
    echo "  restart   - Restart containers"
    echo "  logs      - Show container logs"
    echo "  status    - Show container status"
    echo "  clean     - Remove containers and volumes"
    echo "  shell     - Open shell in app container"
    echo "  backup    - Backup database"
    echo "  restore   - Restore database"
    echo ""
    echo "Environments:"
    echo "  dev       - Development environment (default)"
    echo "  staging   - Staging environment"
    echo "  prod      - Production environment"
    echo ""
    echo "Examples:"
    echo "  $0 build dev"
    echo "  $0 start prod"
    echo "  $0 logs staging"
}

# Function to check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        print_error "Docker is not running. Please start Docker first."
        exit 1
    fi
}

# Function to build Docker image
build_image() {
    print_info "Building Docker image..."
    docker build -t spring-boot-app:latest .
    print_success "Docker image built successfully"
}

# Function to start containers
start_containers() {
    local env=$1
    local compose_file="docker-compose.yml"
    
    case $env in
        "staging")
            compose_file="docker-compose.staging.yml"
            ;;
        "prod")
            compose_file="docker-compose.prod.yml"
            ;;
    esac
    
    print_info "Starting containers for $env environment..."
    docker-compose -f $compose_file up -d
    print_success "Containers started successfully"
    
    # Show status
    docker-compose -f $compose_file ps
}

# Function to stop containers
stop_containers() {
    local env=$1
    local compose_file="docker-compose.yml"
    
    case $env in
        "staging")
            compose_file="docker-compose.staging.yml"
            ;;
        "prod")
            compose_file="docker-compose.prod.yml"
            ;;
    esac
    
    print_info "Stopping containers for $env environment..."
    docker-compose -f $compose_file down
    print_success "Containers stopped successfully"
}

# Function to restart containers
restart_containers() {
    local env=$1
    stop_containers $env
    start_containers $env
}

# Function to show logs
show_logs() {
    local env=$1
    local compose_file="docker-compose.yml"
    
    case $env in
        "staging")
            compose_file="docker-compose.staging.yml"
            ;;
        "prod")
            compose_file="docker-compose.prod.yml"
            ;;
    esac
    
    print_info "Showing logs for $env environment..."
    docker-compose -f $compose_file logs -f
}

# Function to show status
show_status() {
    local env=$1
    local compose_file="docker-compose.yml"
    
    case $env in
        "staging")
            compose_file="docker-compose.staging.yml"
            ;;
        "prod")
            compose_file="docker-compose.prod.yml"
            ;;
    esac
    
    print_info "Container status for $env environment:"
    docker-compose -f $compose_file ps
}

# Function to clean containers and volumes
clean_containers() {
    local env=$1
    local compose_file="docker-compose.yml"
    
    case $env in
        "staging")
            compose_file="docker-compose.staging.yml"
            ;;
        "prod")
            compose_file="docker-compose.prod.yml"
            ;;
    esac
    
    print_warning "This will remove all containers and volumes for $env environment!"
    read -p "Are you sure? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_info "Cleaning containers and volumes..."
        docker-compose -f $compose_file down -v --remove-orphans
        print_success "Cleanup completed"
    else
        print_info "Cleanup cancelled"
    fi
}

# Function to open shell in app container
open_shell() {
    local env=$1
    local container_name="spring_boot_app"
    
    case $env in
        "staging")
            container_name="spring_boot_app_staging"
            ;;
        "prod")
            container_name="spring_boot_app_prod"
            ;;
    esac
    
    print_info "Opening shell in $container_name..."
    docker exec -it $container_name /bin/bash
}

# Function to backup database
backup_database() {
    local env=$1
    local backup_dir="./backup"
    local timestamp=$(date +%Y%m%d_%H%M%S)
    
    mkdir -p $backup_dir
    
    case $env in
        "staging")
            docker exec spring_boot_app_db_staging pg_dump -U postgres spring_boot_app_staging > "$backup_dir/staging_backup_$timestamp.sql"
            ;;
        "prod")
            docker exec spring_boot_app_db_prod pg_dump -U postgres spring_boot_app_prod > "$backup_dir/prod_backup_$timestamp.sql"
            ;;
        *)
            docker exec spring_boot_app_db pg_dump -U postgres spring_boot_app > "$backup_dir/dev_backup_$timestamp.sql"
            ;;
    esac
    
    print_success "Database backup created: $backup_dir/${env}_backup_$timestamp.sql"
}

# Function to restore database
restore_database() {
    local env=$1
    local backup_file=$2
    
    if [ -z "$backup_file" ]; then
        print_error "Please specify a backup file to restore"
        echo "Usage: $0 restore $env <backup_file>"
        exit 1
    fi
    
    if [ ! -f "$backup_file" ]; then
        print_error "Backup file not found: $backup_file"
        exit 1
    fi
    
    print_warning "This will overwrite the current database!"
    read -p "Are you sure? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        case $env in
            "staging")
                docker exec -i spring_boot_app_db_staging psql -U postgres spring_boot_app_staging < "$backup_file"
                ;;
            "prod")
                docker exec -i spring_boot_app_db_prod psql -U postgres spring_boot_app_prod < "$backup_file"
                ;;
            *)
                docker exec -i spring_boot_app_db psql -U postgres spring_boot_app < "$backup_file"
                ;;
        esac
        print_success "Database restored successfully"
    else
        print_info "Restore cancelled"
    fi
}

# Main script logic
main() {
    local command=$1
    local environment=${2:-dev}
    
    # Check if Docker is running
    check_docker
    
    case $command in
        "build")
            build_image
            ;;
        "start")
            start_containers $environment
            ;;
        "stop")
            stop_containers $environment
            ;;
        "restart")
            restart_containers $environment
            ;;
        "logs")
            show_logs $environment
            ;;
        "status")
            show_status $environment
            ;;
        "clean")
            clean_containers $environment
            ;;
        "shell")
            open_shell $environment
            ;;
        "backup")
            backup_database $environment
            ;;
        "restore")
            restore_database $environment $3
            ;;
        *)
            show_usage
            exit 1
            ;;
    esac
}

# Check if no arguments provided
if [ $# -eq 0 ]; then
    show_usage
    exit 1
fi

# Run main function
main "$@" 