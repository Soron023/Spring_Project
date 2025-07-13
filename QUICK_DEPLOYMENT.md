# 🚀 Quick Server Deployment Guide

Deploy your Spring Boot application to any server in minutes!

## Prerequisites

- **Server Access:** SSH access to your server
- **Server Requirements:** 
  - Ubuntu 20.04+ / CentOS 8+ / Amazon Linux 2
  - 2GB+ RAM, 10GB+ storage
  - Java 17+ (for JAR deployment)
  - Docker 20.10+ (for Docker deployment)

## 🎯 Quick Deployment Options

### Option 1: Docker Deployment (Recommended)

```bash
# Deploy with Docker to your server
./deploy-server.sh -m docker -s YOUR_SERVER_IP -u YOUR_USERNAME -e prod

# Example:
./deploy-server.sh -m docker -s 192.168.1.100 -u ubuntu -e prod
```

### Option 2: JAR Deployment

```bash
# Deploy JAR file to server
./deploy-server.sh -m jar -s YOUR_SERVER_IP -u YOUR_USERNAME -e prod

# Example:
./deploy-server.sh -m jar -s myserver.com -u admin -e prod
```

### Option 3: Systemd Service Deployment

```bash
# Deploy as systemd service
./deploy-server.sh -m systemd -s YOUR_SERVER_IP -u YOUR_USERNAME -e prod

# Example:
./deploy-server.sh -m systemd -s 10.0.0.50 -u root -e prod
```

## 🔧 Advanced Deployment Options

### With SSL/HTTPS
```bash
./deploy-server.sh -m docker -s YOUR_SERVER_IP -u YOUR_USERNAME -e prod \
  --ssl --domain your-domain.com
```

### With Monitoring
```bash
./deploy-server.sh -m docker -s YOUR_SERVER_IP -u YOUR_USERNAME -e prod \
  --monitoring
```

### Custom Database Configuration
```bash
./deploy-server.sh -m docker -s YOUR_SERVER_IP -u YOUR_USERNAME -e prod \
  -d your-db-host -n your-db-name -w your-db-user -k your-db-password
```

### Complete Production Setup
```bash
./deploy-server.sh -m docker -s YOUR_SERVER_IP -u YOUR_USERNAME -e prod \
  --ssl --domain your-domain.com \
  --monitoring \
  -d your-db-host -n your-db-name -w your-db-user -k your-secure-password \
  -j your-very-long-jwt-secret-key
```

## 📋 Deployment Script Options

| Option | Description | Default |
|--------|-------------|---------|
| `-m, --method` | Deployment method (docker/jar/systemd) | Required |
| `-s, --server` | Server IP or hostname | Required |
| `-u, --user` | SSH username | Required |
| `-p, --port` | Application port | 8080 |
| `-d, --db-host` | Database host | localhost |
| `-b, --db-port` | Database port | 5432 |
| `-n, --db-name` | Database name | spring_boot_app |
| `-w, --db-user` | Database username | postgres |
| `-k, --db-pass` | Database password | password |
| `-j, --jwt-secret` | JWT secret key | your-secret-key-here |
| `-e, --env` | Environment (dev/staging/prod) | prod |
| `--ssl` | Enable SSL/HTTPS | false |
| `--domain` | Domain name for SSL | Required if SSL enabled |
| `--monitoring` | Deploy with monitoring | false |

## 🚀 Step-by-Step Deployment

### 1. Prepare Your Server

```bash
# Connect to your server
ssh YOUR_USERNAME@YOUR_SERVER_IP

# Update system
sudo apt update && sudo apt upgrade -y

# Install Java 17 (for JAR deployment)
sudo apt install -y openjdk-17-jdk

# Install Docker (for Docker deployment)
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

### 2. Deploy Your Application

```bash
# From your local machine, run the deployment script
./deploy-server.sh -m docker -s YOUR_SERVER_IP -u YOUR_USERNAME -e prod
```

### 3. Verify Deployment

```bash
# Check if application is running
curl http://YOUR_SERVER_IP:8080/actuator/health

# Check Docker containers (if using Docker)
ssh YOUR_USERNAME@YOUR_SERVER_IP "docker ps"

# Check systemd service (if using systemd)
ssh YOUR_USERNAME@YOUR_SERVER_IP "systemctl status spring-boot-app"
```

## 🌐 Access Your Application

After successful deployment:

- **Application:** http://YOUR_SERVER_IP:8080
- **Health Check:** http://YOUR_SERVER_IP:8080/actuator/health
- **API Documentation:** http://YOUR_SERVER_IP:8080/api

If you enabled monitoring:
- **Prometheus:** http://YOUR_SERVER_IP:9090
- **Grafana:** http://YOUR_SERVER_IP:3000 (admin/admin)

If you enabled SSL:
- **HTTPS:** https://your-domain.com

## 🔧 Management Commands

### Check Application Status
```bash
# Docker
ssh YOUR_USERNAME@YOUR_SERVER_IP "docker ps | grep spring-boot-app"

# Systemd
ssh YOUR_USERNAME@YOUR_SERVER_IP "systemctl status spring-boot-app"
```

### View Logs
```bash
# Docker
ssh YOUR_USERNAME@YOUR_SERVER_IP "docker logs spring-boot-app-prod"

# Systemd
ssh YOUR_USERNAME@YOUR_SERVER_IP "journalctl -u spring-boot-app -f"
```

### Restart Application
```bash
# Docker
ssh YOUR_USERNAME@YOUR_SERVER_IP "docker-compose -f /opt/spring-boot-app/docker-compose.prod.yml restart app"

# Systemd
ssh YOUR_USERNAME@YOUR_SERVER_IP "sudo systemctl restart spring-boot-app"
```

### Update Application
```bash
# Re-run deployment script with same parameters
./deploy-server.sh -m docker -s YOUR_SERVER_IP -u YOUR_USERNAME -e prod
```

## 🛡️ Security Checklist

- [ ] Change default database password
- [ ] Use strong JWT secret
- [ ] Enable SSL/HTTPS
- [ ] Configure firewall
- [ ] Set up monitoring
- [ ] Regular backups

## 🚨 Troubleshooting

### Common Issues

1. **SSH Connection Failed**
   ```bash
   # Check SSH key setup
   ssh-copy-id YOUR_USERNAME@YOUR_SERVER_IP
   ```

2. **Port Already in Use**
   ```bash
   # Check what's using the port
   ssh YOUR_USERNAME@YOUR_SERVER_IP "sudo netstat -tulpn | grep :8080"
   ```

3. **Database Connection Failed**
   ```bash
   # Check database status
   ssh YOUR_USERNAME@YOUR_SERVER_IP "sudo systemctl status postgresql"
   ```

4. **Application Won't Start**
   ```bash
   # Check logs
   ssh YOUR_USERNAME@YOUR_SERVER_IP "docker logs spring-boot-app-prod"
   ```

### Get Help

```bash
# Show deployment script help
./deploy-server.sh --help

# Check deployment status
./deploy-server.sh -m docker -s YOUR_SERVER_IP -u YOUR_USERNAME -e prod --help
```

## 📚 Next Steps

1. **Set up Domain:** Point your domain to your server IP
2. **Configure SSL:** Use Let's Encrypt for free SSL certificates
3. **Set up Monitoring:** Monitor application performance
4. **Configure Backups:** Set up automated database backups
5. **Set up CI/CD:** Automate deployments with Jenkins

## 🎉 Success!

Your Spring Boot application is now running on your server! 

For detailed deployment information, see the [Complete Deployment Guide](DEPLOYMENT_GUIDE.md). 