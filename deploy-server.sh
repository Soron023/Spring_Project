#!/bin/bash

# 🚀 Spring Boot Application Server Deployment Script
# This script deploys your Spring Boot application to a server

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
APP_NAME="spring-boot-app"
APP_VERSION="0.0.1-SNAPSHOT"
DEFAULT_PORT="8080"
DEFAULT_DB_PORT="5432"

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -h, --help              Show this help message"
    echo "  -m, --method METHOD     Deployment method (docker|jar|systemd)"
    echo "  -s, --server SERVER     Server IP or hostname"
    echo "  -u, --user USER         SSH username"
    echo "  -p, --port PORT         Application port (default: 8080)"
    echo "  -d, --db-host HOST      Database host (default: localhost)"
    echo "  -b, --db-port PORT      Database port (default: 5432)"
    echo "  -n, --db-name NAME      Database name"
    echo "  -w, --db-user USER      Database username"
    echo "  -k, --db-pass PASS      Database password"
    echo "  -j, --jwt-secret SECRET JWT secret key"
    echo "  -e, --env ENV           Environment (dev|staging|prod)"
    echo "  --ssl                   Enable SSL/HTTPS"
    echo "  --domain DOMAIN         Domain name for SSL"
    echo "  --monitoring            Deploy with monitoring (Prometheus/Grafana)"
    echo ""
    echo "Examples:"
    echo "  $0 -m docker -s 192.168.1.100 -u ubuntu -e prod"
    echo "  $0 -m jar -s myserver.com -u admin -e staging --ssl --domain app.example.com"
    echo "  $0 -m systemd -s 10.0.0.50 -u root -e prod --monitoring"
}

# Function to validate required parameters
validate_params() {
    if [[ -z "$DEPLOY_METHOD" ]]; then
        print_error "Deployment method is required"
        show_usage
        exit 1
    fi
    
    if [[ -z "$SERVER_HOST" ]]; then
        print_error "Server host is required"
        show_usage
        exit 1
    fi
    
    if [[ -z "$SSH_USER" ]]; then
        print_error "SSH user is required"
        show_usage
        exit 1
    fi
}

# Function to build application
build_app() {
    print_status "Building Spring Boot application..."
    
    if ! mvn clean package -DskipTests; then
        print_error "Build failed"
        exit 1
    fi
    
    print_success "Application built successfully"
}

# Function to deploy with Docker
deploy_docker() {
    print_status "Deploying with Docker to $SERVER_HOST..."
    
    # Create deployment package
    tar -czf deploy-package.tar.gz \
        docker-compose.prod.yml \
        Dockerfile \
        target/$APP_NAME-$APP_VERSION.jar \
        src/main/resources/application.properties \
        monitoring/ \
        nginx/ \
        .dockerignore
    
    # Copy to server
    scp deploy-package.tar.gz $SSH_USER@$SERVER_HOST:/tmp/
    
    # Execute deployment commands
    ssh $SSH_USER@$SERVER_HOST << EOF
        set -e
        
        # Create application directory
        sudo mkdir -p /opt/$APP_NAME
        sudo chown $SSH_USER:$SSH_USER /opt/$APP_NAME
        cd /opt/$APP_NAME
        
        # Extract deployment package
        tar -xzf /tmp/deploy-package.tar.gz
        
        # Create environment file
        cat > .env << ENVEOF
DB_PASSWORD=$DB_PASSWORD
JWT_SECRET=$JWT_SECRET
GRAFANA_PASSWORD=admin123
ENVEOF
        
        # Start services
        if [[ "$ENABLE_MONITORING" == "true" ]]; then
            docker-compose -f docker-compose.prod.yml up -d
        else
            docker-compose -f docker-compose.prod.yml up -d app postgres
        fi
        
        # Wait for services to be ready
        sleep 30
        
        # Check health
        if curl -f http://localhost:$APP_PORT/actuator/health; then
            echo "Application deployed successfully!"
        else
            echo "Application health check failed"
            exit 1
        fi
EOF
    
    print_success "Docker deployment completed"
}

# Function to deploy with JAR
deploy_jar() {
    print_status "Deploying JAR to $SERVER_HOST..."
    
    # Copy JAR to server
    scp target/$APP_NAME-$APP_VERSION.jar $SSH_USER@$SERVER_HOST:/tmp/
    
    # Execute deployment commands
    ssh $SSH_USER@$SERVER_HOST << EOF
        set -e
        
        # Create application directory
        sudo mkdir -p /opt/$APP_NAME
        sudo chown $SSH_USER:$SSH_USER /opt/$APP_NAME
        cd /opt/$APP_NAME
        
        # Move JAR file
        mv /tmp/$APP_NAME-$APP_VERSION.jar .
        
        # Create startup script
        cat > start-app.sh << 'SCRIPTEOF'
#!/bin/bash
export JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC"
export SPRING_PROFILES_ACTIVE=$ENVIRONMENT
export DB_PASSWORD=$DB_PASSWORD
export JWT_SECRET=$JWT_SECRET

java \$JAVA_OPTS -jar $APP_NAME-$APP_VERSION.jar \\
  --spring.profiles.active=$ENVIRONMENT \\
  --server.port=$APP_PORT \\
  --spring.datasource.url=jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME \\
  --spring.datasource.username=$DB_USER \\
  --spring.datasource.password=\$DB_PASSWORD \\
  --jwt.secret=\$JWT_SECRET
SCRIPTEOF
        
        chmod +x start-app.sh
        
        # Start application
        nohup ./start-app.sh > app.log 2>&1 &
        
        # Wait for startup
        sleep 30
        
        # Check health
        if curl -f http://localhost:$APP_PORT/actuator/health; then
            echo "Application deployed successfully!"
        else
            echo "Application health check failed"
            exit 1
        fi
EOF
    
    print_success "JAR deployment completed"
}

# Function to deploy with systemd
deploy_systemd() {
    print_status "Deploying with systemd to $SERVER_HOST..."
    
    # Copy JAR to server
    scp target/$APP_NAME-$APP_VERSION.jar $SSH_USER@$SERVER_HOST:/tmp/
    
    # Execute deployment commands
    ssh $SSH_USER@$SERVER_HOST << EOF
        set -e
        
        # Create application directory and user
        sudo mkdir -p /opt/$APP_NAME
        sudo useradd -r -s /bin/false $APP_NAME || true
        sudo chown $APP_NAME:$APP_NAME /opt/$APP_NAME
        cd /opt/$APP_NAME
        
        # Move JAR file
        sudo mv /tmp/$APP_NAME-$APP_VERSION.jar .
        sudo chown $APP_NAME:$APP_NAME $APP_NAME-$APP_VERSION.jar
        
        # Create systemd service
        sudo tee /etc/systemd/system/$APP_NAME.service > /dev/null << SERVICEEOF
[Unit]
Description=Spring Boot Application
After=network.target postgresql.service

[Service]
Type=simple
User=$APP_NAME
Group=$APP_NAME
WorkingDirectory=/opt/$APP_NAME
Environment=SPRING_PROFILES_ACTIVE=$ENVIRONMENT
Environment=DB_PASSWORD=$DB_PASSWORD
Environment=JWT_SECRET=$JWT_SECRET
ExecStart=/usr/bin/java -Xms512m -Xmx2g -jar $APP_NAME-$APP_VERSION.jar \\
  --spring.profiles.active=$ENVIRONMENT \\
  --server.port=$APP_PORT \\
  --spring.datasource.url=jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME \\
  --spring.datasource.username=$DB_USER \\
  --spring.datasource.password=\$DB_PASSWORD \\
  --jwt.secret=\$JWT_SECRET
ExecReload=/bin/kill -HUP \$MAINPID
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
SERVICEEOF
        
        # Reload systemd and start service
        sudo systemctl daemon-reload
        sudo systemctl enable $APP_NAME
        sudo systemctl start $APP_NAME
        
        # Wait for startup
        sleep 30
        
        # Check health
        if curl -f http://localhost:$APP_PORT/actuator/health; then
            echo "Application deployed successfully!"
        else
            echo "Application health check failed"
            sudo systemctl status $APP_NAME
            exit 1
        fi
EOF
    
    print_success "Systemd deployment completed"
}

# Function to setup SSL
setup_ssl() {
    if [[ "$ENABLE_SSL" == "true" && -n "$DOMAIN" ]]; then
        print_status "Setting up SSL for domain: $DOMAIN"
        
        ssh $SSH_USER@$SERVER_HOST << EOF
            set -e
            
            # Install certbot if not installed
            if ! command -v certbot &> /dev/null; then
                sudo apt update
                sudo apt install -y certbot python3-certbot-nginx
            fi
            
            # Get SSL certificate
            sudo certbot --nginx -d $DOMAIN --non-interactive --agree-tos --email admin@$DOMAIN
            
            # Setup auto-renewal
            (crontab -l 2>/dev/null; echo "0 12 * * * /usr/bin/certbot renew --quiet") | crontab -
EOF
        
        print_success "SSL setup completed"
    fi
}

# Function to setup monitoring
setup_monitoring() {
    if [[ "$ENABLE_MONITORING" == "true" ]]; then
        print_status "Setting up monitoring..."
        
        ssh $SSH_USER@$SERVER_HOST << EOF
            set -e
            
            # Create monitoring directory
            sudo mkdir -p /opt/monitoring
            cd /opt/monitoring
            
            # Create Prometheus configuration
            cat > prometheus.yml << PROMEOF
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'spring-boot-app'
    static_configs:
      - targets: ['localhost:$APP_PORT']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
PROMEOF
            
            # Start Prometheus
            docker run -d \\
              --name prometheus \\
              -p 9090:9090 \\
              -v \$(pwd)/prometheus.yml:/etc/prometheus/prometheus.yml \\
              prom/prometheus
            
            # Start Grafana
            docker run -d \\
              --name grafana \\
              -p 3000:3000 \\
              -e GF_SECURITY_ADMIN_PASSWORD=admin \\
              grafana/grafana
            
            echo "Monitoring setup completed:"
            echo "  - Prometheus: http://$SERVER_HOST:9090"
            echo "  - Grafana: http://$SERVER_HOST:3000 (admin/admin)"
EOF
        
        print_success "Monitoring setup completed"
    fi
}

# Function to show deployment info
show_deployment_info() {
    print_success "Deployment completed successfully!"
    echo ""
    echo "📊 Deployment Information:"
    echo "  Server: $SERVER_HOST"
    echo "  Method: $DEPLOY_METHOD"
    echo "  Environment: $ENVIRONMENT"
    echo "  Application URL: http://$SERVER_HOST:$APP_PORT"
    echo "  Health Check: http://$SERVER_HOST:$APP_PORT/actuator/health"
    
    if [[ "$ENABLE_SSL" == "true" && -n "$DOMAIN" ]]; then
        echo "  HTTPS URL: https://$DOMAIN"
    fi
    
    if [[ "$ENABLE_MONITORING" == "true" ]]; then
        echo ""
        echo "📈 Monitoring:"
        echo "  Prometheus: http://$SERVER_HOST:9090"
        echo "  Grafana: http://$SERVER_HOST:3000 (admin/admin)"
    fi
    
    echo ""
    echo "🔧 Useful Commands:"
    echo "  Check status: ssh $SSH_USER@$SERVER_HOST 'systemctl status $APP_NAME'"
    echo "  View logs: ssh $SSH_USER@$SERVER_HOST 'journalctl -u $APP_NAME -f'"
    echo "  Restart: ssh $SSH_USER@$SERVER_HOST 'sudo systemctl restart $APP_NAME'"
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_usage
            exit 0
            ;;
        -m|--method)
            DEPLOY_METHOD="$2"
            shift 2
            ;;
        -s|--server)
            SERVER_HOST="$2"
            shift 2
            ;;
        -u|--user)
            SSH_USER="$2"
            shift 2
            ;;
        -p|--port)
            APP_PORT="$2"
            shift 2
            ;;
        -d|--db-host)
            DB_HOST="$2"
            shift 2
            ;;
        -b|--db-port)
            DB_PORT="$2"
            shift 2
            ;;
        -n|--db-name)
            DB_NAME="$2"
            shift 2
            ;;
        -w|--db-user)
            DB_USER="$2"
            shift 2
            ;;
        -k|--db-pass)
            DB_PASSWORD="$2"
            shift 2
            ;;
        -j|--jwt-secret)
            JWT_SECRET="$2"
            shift 2
            ;;
        -e|--env)
            ENVIRONMENT="$2"
            shift 2
            ;;
        --ssl)
            ENABLE_SSL="true"
            shift
            ;;
        --domain)
            DOMAIN="$2"
            shift 2
            ;;
        --monitoring)
            ENABLE_MONITORING="true"
            shift
            ;;
        *)
            print_error "Unknown option: $1"
            show_usage
            exit 1
            ;;
    esac
done

# Set defaults
APP_PORT=${APP_PORT:-$DEFAULT_PORT}
DB_HOST=${DB_HOST:-"localhost"}
DB_PORT=${DB_PORT:-$DEFAULT_DB_PORT}
DB_NAME=${DB_NAME:-"spring_boot_app"}
DB_USER=${DB_USER:-"postgres"}
DB_PASSWORD=${DB_PASSWORD:-"password"}
JWT_SECRET=${JWT_SECRET:-"your-secret-key-here"}
ENVIRONMENT=${ENVIRONMENT:-"prod"}
ENABLE_SSL=${ENABLE_SSL:-"false"}
ENABLE_MONITORING=${ENABLE_MONITORING:-"false"}

# Validate parameters
validate_params

# Main deployment process
print_status "Starting deployment process..."

# Build application
build_app

# Deploy based on method
case $DEPLOY_METHOD in
    docker)
        deploy_docker
        ;;
    jar)
        deploy_jar
        ;;
    systemd)
        deploy_systemd
        ;;
    *)
        print_error "Invalid deployment method: $DEPLOY_METHOD"
        exit 1
        ;;
esac

# Setup SSL if requested
setup_ssl

# Setup monitoring if requested
setup_monitoring

# Show deployment information
show_deployment_info

print_success "Deployment completed! 🎉" 