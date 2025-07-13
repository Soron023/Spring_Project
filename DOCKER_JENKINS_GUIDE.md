# 🐳 Docker & Jenkins Guide - Spring Boot Application

## 📋 Table of Contents
1. [Docker Setup](#docker-setup)
2. [Jenkins Pipeline](#jenkins-pipeline)
3. [Environment Management](#environment-management)
4. [Monitoring & Observability](#monitoring--observability)
5. [Deployment Scripts](#deployment-scripts)
6. [Troubleshooting](#troubleshooting)

## 🐳 Docker Setup

### Prerequisites
- Docker Desktop installed and running
- Docker Compose installed
- At least 4GB RAM available for containers

### Quick Start with Docker

#### 1. Build and Run (Development)
```bash
# Build the Docker image
docker build -t spring-boot-app .

# Run with Docker Compose (includes PostgreSQL and Redis)
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f app
```

#### 2. Using the Deployment Script
```bash
# Make script executable (if not already)
chmod +x docker-deploy.sh

# Build image
./docker-deploy.sh build

# Start development environment
./docker-deploy.sh start dev

# Start staging environment
./docker-deploy.sh start staging

# Start production environment
./docker-deploy.sh start prod
```

### Docker Compose Environments

#### Development Environment (`docker-compose.yml`)
- **Application**: `http://localhost:8080`
- **PostgreSQL**: `localhost:5432`
- **Redis**: `localhost:6379`
- **Features**: Hot reload, debug mode, development profiles

#### Staging Environment (`docker-compose.staging.yml`)
- **Application**: `http://localhost:8081`
- **PostgreSQL**: `localhost:5433`
- **Redis**: `localhost:6380`
- **Features**: Production-like settings, testing data

#### Production Environment (`docker-compose.prod.yml`)
- **Application**: `http://localhost:8082`
- **PostgreSQL**: `localhost:5434`
- **Redis**: `localhost:6381`
- **Nginx**: `http://localhost:80`, `https://localhost:443`
- **Prometheus**: `http://localhost:9090`
- **Grafana**: `http://localhost:3000`
- **Features**: Full monitoring, SSL, resource limits

### Docker Commands Reference

#### Container Management
```bash
# Start containers
docker-compose up -d

# Stop containers
docker-compose down

# Restart containers
docker-compose restart

# View logs
docker-compose logs -f [service_name]

# Execute commands in container
docker exec -it spring_boot_app /bin/bash

# View container status
docker-compose ps
```

#### Image Management
```bash
# Build image
docker build -t spring-boot-app .

# List images
docker images

# Remove image
docker rmi spring-boot-app

# Push to registry
docker tag spring-boot-app your-registry/spring-boot-app:latest
docker push your-registry/spring-boot-app:latest
```

## 🔄 Jenkins Pipeline

### Prerequisites
- Jenkins server with Docker support
- Required Jenkins plugins:
  - Docker Pipeline
  - SonarQube Scanner
  - Slack Notification
  - HTML Publisher
  - Test Results Analyzer

### Pipeline Features

#### Stages Overview
1. **Checkout** - Source code retrieval
2. **Validate** - Project structure validation
3. **Dependencies** - Maven dependency resolution
4. **Code Quality** - SonarQube analysis & security scanning
5. **Test** - Unit and integration tests
6. **Build** - Application packaging
7. **Docker Build** - Container image creation
8. **Docker Security Scan** - Vulnerability scanning
9. **Push to Registry** - Image distribution
10. **Deploy** - Environment deployment

#### Environment-Specific Deployments
- **Develop Branch** → Staging Environment
- **Main/Master Branch** → Production Environment
- **Feature Branches** → Build and test only

### Jenkins Configuration

#### 1. Credentials Setup
```bash
# In Jenkins, add these credentials:
# 1. Docker Registry Credentials (docker-registry-credentials)
#    - Username: your-registry-username
#    - Password: your-registry-password

# 2. SonarQube Token (sonar-token)
#    - Token: your-sonarqube-token

# 3. Slack Webhook (if using Slack notifications)
#    - Webhook URL: your-slack-webhook-url
```

#### 2. Tools Configuration
```bash
# In Jenkins > Manage Jenkins > Global Tool Configuration:

# Maven
Name: Maven-3.9.5
Installation: Install automatically
Version: 3.9.5

# JDK
Name: OpenJDK-17
Installation: Install automatically
Version: 17
```

#### 3. Pipeline Configuration
```bash
# Update Jenkinsfile environment variables:
DOCKER_REGISTRY = 'your-registry.com'
DOCKER_CREDENTIALS = 'docker-registry-credentials'
SLACK_CHANNEL = '#your-deployments-channel'
```

### Pipeline Execution

#### Manual Trigger
```bash
# In Jenkins:
1. Create new Pipeline job
2. Configure SCM (Git repository)
3. Set Jenkinsfile path
4. Build with parameters (if needed)
```

#### Webhook Trigger
```bash
# Configure webhook in your Git repository:
URL: http://jenkins-url/github-webhook/
Content-Type: application/json
Events: push, pull_request
```

## 🌍 Environment Management

### Environment Variables

#### Development
```bash
SPRING_PROFILES_ACTIVE=docker
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/spring_boot_app
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=123456
JWT_SECRET=dev-secret-key
```

#### Staging
```bash
SPRING_PROFILES_ACTIVE=staging
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/spring_boot_app_staging
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=staging_password
JWT_SECRET=staging-secret-key
```

#### Production
```bash
SPRING_PROFILES_ACTIVE=production
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/spring_boot_app_prod
SPRING_DATASOURCE_USERNAME=${POSTGRES_USER}
SPRING_DATASOURCE_PASSWORD=${POSTGRES_PASSWORD}
JWT_SECRET=${JWT_SECRET}
JAVA_OPTS=-Xms1g -Xmx2g -XX:+UseG1GC
```

### Database Management

#### Backup
```bash
# Development
./docker-deploy.sh backup dev

# Staging
./docker-deploy.sh backup staging

# Production
./docker-deploy.sh backup prod
```

#### Restore
```bash
# Restore from backup
./docker-deploy.sh restore dev backup/dev_backup_20231201_143022.sql
```

## 📊 Monitoring & Observability

### Prometheus Configuration

#### Metrics Endpoints
- **Application Metrics**: `http://localhost:8080/actuator/prometheus`
- **JVM Metrics**: Auto-collected by Spring Boot Actuator
- **Custom Metrics**: Available through Micrometer

#### Key Metrics
- HTTP request rate and response time
- JVM memory usage
- Database connection pool
- Application health status

### Grafana Dashboards

#### Default Dashboard
- **URL**: `http://localhost:3000`
- **Username**: `admin`
- **Password**: `admin` (change on first login)

#### Dashboard Features
- HTTP request rate graphs
- Response time monitoring
- JVM memory usage
- Active connection counts
- Error rate tracking

### Health Checks

#### Application Health
```bash
# Health endpoint
curl http://localhost:8080/actuator/health

# Detailed health
curl http://localhost:8080/actuator/health/detailed

# Custom health checks
curl http://localhost:8080/actuator/health/db
curl http://localhost:8080/actuator/health/redis
```

## 🛠️ Deployment Scripts

### Docker Deployment Script Usage

#### Basic Commands
```bash
# Show help
./docker-deploy.sh

# Build image
./docker-deploy.sh build

# Start environment
./docker-deploy.sh start [dev|staging|prod]

# Stop environment
./docker-deploy.sh stop [dev|staging|prod]

# Restart environment
./docker-deploy.sh restart [dev|staging|prod]
```

#### Advanced Commands
```bash
# View logs
./docker-deploy.sh logs [dev|staging|prod]

# Check status
./docker-deploy.sh status [dev|staging|prod]

# Clean environment
./docker-deploy.sh clean [dev|staging|prod]

# Open shell in container
./docker-deploy.sh shell [dev|staging|prod]

# Database backup
./docker-deploy.sh backup [dev|staging|prod]

# Database restore
./docker-deploy.sh restore [dev|staging|prod] <backup_file>
```

### Environment-Specific Scripts

#### Development
```bash
# Quick development setup
docker-compose up -d

# View logs
docker-compose logs -f app

# Rebuild and restart
docker-compose down
docker-compose up -d --build
```

#### Staging
```bash
# Deploy to staging
docker-compose -f docker-compose.staging.yml up -d

# Run tests against staging
curl http://localhost:8081/actuator/health
```

#### Production
```bash
# Deploy to production
docker-compose -f docker-compose.prod.yml up -d

# Monitor deployment
docker-compose -f docker-compose.prod.yml ps

# Check monitoring
open http://localhost:9090  # Prometheus
open http://localhost:3000  # Grafana
```

## 🔧 Troubleshooting

### Common Docker Issues

#### 1. Port Conflicts
```bash
# Check what's using the port
lsof -i :8080

# Kill process or change port in docker-compose.yml
ports:
  - "8081:8080"  # Use different host port
```

#### 2. Memory Issues
```bash
# Check Docker memory usage
docker stats

# Increase Docker memory limit in Docker Desktop
# Settings > Resources > Memory: 4GB+
```

#### 3. Database Connection Issues
```bash
# Check if database is running
docker-compose ps postgres

# Check database logs
docker-compose logs postgres

# Restart database
docker-compose restart postgres
```

### Common Jenkins Issues

#### 1. Pipeline Fails at Docker Build
```bash
# Check Docker daemon
docker info

# Ensure Jenkins has Docker permissions
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

#### 2. SonarQube Analysis Fails
```bash
# Check SonarQube server
curl http://sonarqube:9000/api/system/status

# Verify credentials in Jenkins
# Manage Jenkins > Credentials > System > Global credentials
```

#### 3. Deployment Fails
```bash
# Check target environment
kubectl get pods -n staging
kubectl get pods -n production

# Check logs
kubectl logs deployment/spring-boot-app -n staging
```

### Performance Optimization

#### Docker Optimization
```bash
# Use multi-stage builds (already implemented)
# Optimize JVM settings for containers
JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseContainerSupport"

# Use .dockerignore to reduce build context
# Already configured in .dockerignore
```

#### Jenkins Optimization
```bash
# Parallel execution for independent stages
# Already implemented in Jenkinsfile

# Cache Maven dependencies
# Use Docker layer caching
# Implement build caching strategies
```

## 📚 Best Practices

### Docker Best Practices
1. **Multi-stage builds** - Reduce image size
2. **Non-root user** - Security enhancement
3. **Health checks** - Container monitoring
4. **Resource limits** - Prevent resource exhaustion
5. **Environment-specific configs** - Separation of concerns

### Jenkins Best Practices
1. **Pipeline as Code** - Version control your CI/CD
2. **Parallel execution** - Faster builds
3. **Security scanning** - Vulnerability detection
4. **Environment promotion** - Controlled deployments
5. **Monitoring integration** - Observability

### Security Best Practices
1. **Secrets management** - Use environment variables
2. **Image scanning** - Regular vulnerability checks
3. **Network isolation** - Docker networks
4. **Access control** - Role-based permissions
5. **Audit logging** - Track all changes

## 🚀 Next Steps

### Advanced Features
1. **Kubernetes Deployment** - Replace Docker Compose
2. **Service Mesh** - Istio for microservices
3. **GitOps** - ArgoCD for declarative deployments
4. **Advanced Monitoring** - ELK stack integration
5. **Security Scanning** - Trivy, Snyk integration

### Scaling Considerations
1. **Horizontal scaling** - Multiple application instances
2. **Load balancing** - Nginx, HAProxy
3. **Database clustering** - PostgreSQL replication
4. **Caching strategy** - Redis cluster
5. **CDN integration** - Static asset delivery

---

**Happy Containerizing! 🐳** 