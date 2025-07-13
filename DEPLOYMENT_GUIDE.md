# 🚀 Server Deployment Guide

Complete guide for deploying your Spring Boot application to various server environments.

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [VPS/Cloud Server Deployment](#vpscloud-server-deployment)
3. [Docker Deployment](#docker-deployment)
4. [Traditional JAR Deployment](#traditional-jar-deployment)
5. [Cloud Platform Deployment](#cloud-platform-deployment)
6. [Production Configuration](#production-configuration)
7. [SSL/HTTPS Setup](#sslhttps-setup)
8. [Monitoring & Maintenance](#monitoring--maintenance)

## Prerequisites

### Server Requirements
- **OS:** Ubuntu 20.04+ / CentOS 8+ / Amazon Linux 2
- **RAM:** Minimum 2GB, Recommended 4GB+
- **Storage:** 10GB+ free space
- **Java:** OpenJDK 17+
- **Docker:** 20.10+ (for containerized deployment)
- **Database:** PostgreSQL 13+ or MySQL 8.0+

### Network Requirements
- **Ports:** 80, 443, 8080, 5432 (database)
- **Domain:** Optional but recommended for production
- **SSL Certificate:** Required for HTTPS

## VPS/Cloud Server Deployment

### 1. Server Setup Script

Create a server setup script:

```bash
#!/bin/bash
# server-setup.sh

# Update system
sudo apt update && sudo apt upgrade -y

# Install Java 17
sudo apt install -y openjdk-17-jdk

# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Install PostgreSQL (if not using Docker)
sudo apt install -y postgresql postgresql-contrib

# Install Nginx
sudo apt install -y nginx

# Install Certbot for SSL
sudo apt install -y certbot python3-certbot-nginx

# Create application directory
sudo mkdir -p /opt/spring-boot-app
sudo chown $USER:$USER /opt/spring-boot-app
```

### 2. Application Deployment

```bash
# Clone your repository
git clone https://github.com/your-username/spring-boot-app.git /opt/spring-boot-app
cd /opt/spring-boot-app

# Build the application
./mvnw clean package -DskipTests

# Create production configuration
cat > application-prod.properties << EOF
# Production Database
spring.datasource.url=jdbc:postgresql://localhost:5432/spring_boot_app_prod
spring.datasource.username=postgres
spring.datasource.password=your_secure_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Configuration
jwt.secret=your_very_long_and_secure_production_secret_key_here
jwt.expiration=86400000

# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Logging
logging.level.root=WARN
logging.level.com.example.springbootapp=INFO
logging.file.name=/var/log/spring-boot-app/application.log

# Flyway
spring.flyway.baseline-on-migrate=true

# Actuator
management.endpoints.web.exposure.include=health,metrics,info
management.endpoint.health.show-details=when-authorized

# Audit Configuration
audit.enabled=true
audit.log-levels=INFO,WARNING,ERROR,SECURITY
EOF
```

### 3. Systemd Service Setup

```bash
# Create systemd service file
sudo tee /etc/systemd/system/spring-boot-app.service > /dev/null << EOF
[Unit]
Description=Spring Boot Application
After=network.target postgresql.service

[Service]
Type=simple
User=spring-app
Group=spring-app
WorkingDirectory=/opt/spring-boot-app
ExecStart=/usr/bin/java -Xms512m -Xmx2g -jar target/spring-boot-app-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
ExecReload=/bin/kill -HUP \$MAINPID
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

# Create user for the application
sudo useradd -r -s /bin/false spring-app
sudo chown -R spring-app:spring-app /opt/spring-boot-app

# Enable and start service
sudo systemctl daemon-reload
sudo systemctl enable spring-boot-app
sudo systemctl start spring-boot-app
```

## Docker Deployment

### 1. Production Docker Compose

```bash
# Use the existing production compose file
docker-compose -f docker-compose.prod.yml up -d

# Or create a custom production setup
cat > docker-compose.production.yml << EOF
version: '3.8'

services:
  app:
    image: your-registry/spring-boot-app:latest
    container_name: spring-boot-app-prod
    environment:
      SPRING_PROFILES_ACTIVE: production
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/spring_boot_app_prod
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: \${DB_PASSWORD}
      JWT_SECRET: \${JWT_SECRET}
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    restart: unless-stopped
    networks:
      - app-network

  postgres:
    image: postgres:15-alpine
    container_name: spring-boot-app-db
    environment:
      POSTGRES_DB: spring_boot_app_prod
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: \${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    restart: unless-stopped
    networks:
      - app-network

  nginx:
    image: nginx:alpine
    container_name: spring-boot-app-nginx
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/ssl:/etc/nginx/ssl:ro
    depends_on:
      - app
    restart: unless-stopped
    networks:
      - app-network

volumes:
  postgres_data:

networks:
  app-network:
    driver: bridge
EOF
```

### 2. Environment Variables

```bash
# Create .env file
cat > .env << EOF
DB_PASSWORD=your_secure_database_password
JWT_SECRET=your_very_long_and_secure_jwt_secret_key
GRAFANA_PASSWORD=your_grafana_password
EOF

# Deploy
docker-compose -f docker-compose.production.yml --env-file .env up -d
```

## Traditional JAR Deployment

### 1. Build and Deploy

```bash
# Build the application
mvn clean package -DskipTests

# Copy to server
scp target/spring-boot-app-0.0.1-SNAPSHOT.jar user@your-server:/opt/spring-boot-app/

# Create startup script
cat > start-app.sh << 'EOF'
#!/bin/bash
export JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC"
export SPRING_PROFILES_ACTIVE=production
export DB_PASSWORD=your_secure_password
export JWT_SECRET=your_secure_jwt_secret

java $JAVA_OPTS -jar spring-boot-app-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=production \
  --spring.datasource.password=$DB_PASSWORD \
  --jwt.secret=$JWT_SECRET
EOF

chmod +x start-app.sh
```

### 2. Process Management

```bash
# Using PM2 (Node.js process manager)
npm install -g pm2

# Create PM2 configuration
cat > ecosystem.config.js << EOF
module.exports = {
  apps: [{
    name: 'spring-boot-app',
    script: 'java',
    args: '-Xms512m -Xmx2g -jar spring-boot-app-0.0.1-SNAPSHOT.jar --spring.profiles.active=production',
    cwd: '/opt/spring-boot-app',
    instances: 1,
    autorestart: true,
    watch: false,
    max_memory_restart: '1G',
    env: {
      NODE_ENV: 'production',
      SPRING_PROFILES_ACTIVE: 'production'
    }
  }]
}
EOF

# Start with PM2
pm2 start ecosystem.config.js
pm2 save
pm2 startup
```

## Cloud Platform Deployment

### AWS EC2 Deployment

```bash
# Install AWS CLI
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

# Configure AWS credentials
aws configure

# Create deployment script
cat > deploy-aws.sh << 'EOF'
#!/bin/bash

# Build application
mvn clean package -DskipTests

# Create deployment package
tar -czf spring-boot-app.tar.gz target/spring-boot-app-0.0.1-SNAPSHOT.jar application-prod.properties

# Upload to S3
aws s3 cp spring-boot-app.tar.gz s3://your-bucket/deployments/

# Deploy to EC2 instances
aws ssm send-command \
  --instance-ids "i-1234567890abcdef0" \
  --document-name "AWS-RunShellScript" \
  --parameters 'commands=["cd /opt/spring-boot-app", "aws s3 cp s3://your-bucket/deployments/spring-boot-app.tar.gz .", "tar -xzf spring-boot-app.tar.gz", "sudo systemctl restart spring-boot-app"]'
EOF

chmod +x deploy-aws.sh
```

### Google Cloud Platform

```bash
# Install Google Cloud SDK
curl https://sdk.cloud.google.com | bash
exec -l $SHELL
gcloud init

# Deploy to Google Cloud Run
gcloud run deploy spring-boot-app \
  --source . \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars SPRING_PROFILES_ACTIVE=production

# Or deploy to Google Compute Engine
gcloud compute instances create-with-container spring-boot-app \
  --container-image gcr.io/your-project/spring-boot-app:latest \
  --machine-type e2-medium \
  --zone us-central1-a
```

### Azure App Service

```bash
# Install Azure CLI
curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash

# Login to Azure
az login

# Create App Service
az group create --name spring-boot-app-rg --location eastus
az appservice plan create --name spring-boot-app-plan --resource-group spring-boot-app-rg --sku B1
az webapp create --name spring-boot-app --resource-group spring-boot-app-rg --plan spring-boot-app-plan --runtime "JAVA:17-java17"

# Deploy application
az webapp deployment source config-local-git --name spring-boot-app --resource-group spring-boot-app-rg
git remote add azure <git-url-from-previous-command>
git push azure main
```

## Production Configuration

### 1. Security Configuration

```properties
# application-prod.properties
# Security
spring.security.require-ssl=true
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=your_keystore_password
server.ssl.key-store-type=PKCS12

# Rate Limiting
spring.security.oauth2.resourceserver.jwt.issuer-uri=https://your-domain.com

# CORS
spring.web.cors.allowed-origins=https://your-frontend-domain.com
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*

# Database Connection Pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

### 2. Logging Configuration

```xml
<!-- logback-spring.xml -->
<configuration>
  <springProfile name="production">
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
      <file>/var/log/spring-boot-app/application.log</file>
      <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <fileNamePattern>/var/log/spring-boot-app/application.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
        <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
          <maxFileSize>100MB</maxFileSize>
        </timeBasedFileNamingAndTriggeringPolicy>
        <maxHistory>30</maxHistory>
      </rollingPolicy>
      <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
      </encoder>
    </appender>
    
    <root level="WARN">
      <appender-ref ref="FILE" />
    </root>
  </springProfile>
</configuration>
```

## SSL/HTTPS Setup

### 1. Using Let's Encrypt (Free SSL)

```bash
# Install Certbot
sudo apt install certbot python3-certbot-nginx

# Get SSL certificate
sudo certbot --nginx -d your-domain.com -d www.your-domain.com

# Auto-renewal
sudo crontab -e
# Add: 0 12 * * * /usr/bin/certbot renew --quiet
```

### 2. Nginx Configuration

```nginx
# /etc/nginx/sites-available/spring-boot-app
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name your-domain.com;

    ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;

    # Security headers
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
    add_header X-Frame-Options DENY always;
    add_header X-Content-Type-Options nosniff always;
    add_header X-XSS-Protection "1; mode=block" always;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Health check endpoint
    location /actuator/health {
        proxy_pass http://localhost:8080/actuator/health;
        access_log off;
    }
}
```

## Monitoring & Maintenance

### 1. Health Checks

```bash
# Create health check script
cat > health-check.sh << 'EOF'
#!/bin/bash

HEALTH_URL="http://localhost:8080/actuator/health"
ALERT_EMAIL="admin@your-domain.com"

response=$(curl -s -o /dev/null -w "%{http_code}" $HEALTH_URL)

if [ $response -ne 200 ]; then
    echo "Application health check failed with status: $response" | mail -s "Spring Boot App Health Alert" $ALERT_EMAIL
    # Restart service
    sudo systemctl restart spring-boot-app
fi
EOF

# Add to crontab
chmod +x health-check.sh
(crontab -l 2>/dev/null; echo "*/5 * * * * /opt/spring-boot-app/health-check.sh") | crontab -
```

### 2. Backup Script

```bash
# Database backup script
cat > backup.sh << 'EOF'
#!/bin/bash

BACKUP_DIR="/opt/backups"
DATE=$(date +%Y%m%d_%H%M%S)
DB_NAME="spring_boot_app_prod"

# Create backup directory
mkdir -p $BACKUP_DIR

# Backup database
pg_dump -h localhost -U postgres $DB_NAME > $BACKUP_DIR/db_backup_$DATE.sql

# Backup application logs
tar -czf $BACKUP_DIR/logs_backup_$DATE.tar.gz /var/log/spring-boot-app/

# Keep only last 7 days of backups
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
find $BACKUP_DIR -name "*.tar.gz" -mtime +7 -delete

echo "Backup completed: $DATE"
EOF

chmod +x backup.sh
# Add to crontab for daily backup
(crontab -l 2>/dev/null; echo "0 2 * * * /opt/spring-boot-app/backup.sh") | crontab -
```

### 3. Performance Monitoring

```bash
# Install monitoring tools
sudo apt install -y htop iotop nethogs

# Create monitoring dashboard
cat > monitor.sh << 'EOF'
#!/bin/bash

echo "=== System Resources ==="
free -h
echo ""
echo "=== Disk Usage ==="
df -h
echo ""
echo "=== Application Status ==="
systemctl status spring-boot-app --no-pager
echo ""
echo "=== Recent Logs ==="
tail -20 /var/log/spring-boot-app/application.log
EOF

chmod +x monitor.sh
```

## Deployment Checklist

- [ ] **Security**
  - [ ] Change default passwords
  - [ ] Configure firewall (UFW)
  - [ ] Enable SSL/HTTPS
  - [ ] Set up security headers

- [ ] **Database**
  - [ ] Create production database
  - [ ] Configure connection pooling
  - [ ] Set up backups
  - [ ] Test migrations

- [ ] **Application**
  - [ ] Build with production profile
  - [ ] Configure logging
  - [ ] Set up monitoring
  - [ ] Test all endpoints

- [ ] **Infrastructure**
  - [ ] Configure load balancer (if needed)
  - [ ] Set up auto-scaling
  - [ ] Configure CDN
  - [ ] Set up alerts

- [ ] **Documentation**
  - [ ] Update deployment docs
  - [ ] Create runbooks
  - [ ] Document rollback procedures

## Quick Deployment Commands

```bash
# 1. Build and package
mvn clean package -DskipTests

# 2. Deploy with Docker
docker-compose -f docker-compose.prod.yml up -d

# 3. Deploy with systemd
sudo systemctl restart spring-boot-app

# 4. Check status
sudo systemctl status spring-boot-app
docker ps

# 5. View logs
sudo journalctl -u spring-boot-app -f
docker logs spring-boot-app-prod
```

## Troubleshooting

### Common Issues

1. **Port already in use**
   ```bash
   sudo netstat -tulpn | grep :8080
   sudo kill -9 <PID>
   ```

2. **Database connection failed**
   ```bash
   sudo systemctl status postgresql
   sudo -u postgres psql -c "\l"
   ```

3. **Memory issues**
   ```bash
   # Increase heap size
   java -Xms1g -Xmx2g -jar app.jar
   ```

4. **SSL certificate issues**
   ```bash
   sudo certbot renew --dry-run
   sudo nginx -t
   ```

---

For more detailed information, refer to the [Complete User Guide](USER_GUIDE.md) and [Docker & Jenkins Guide](DOCKER_JENKINS_GUIDE.md). 