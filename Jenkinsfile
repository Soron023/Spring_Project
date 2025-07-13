pipeline {
    agent any
    
    environment {
        // Application configuration
        APP_NAME = 'spring-boot-app'
        APP_VERSION = '0.0.1-SNAPSHOT'
        DOCKER_IMAGE = 'spring-boot-app'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        
        // Registry configuration (update with your registry)
        DOCKER_REGISTRY = 'your-registry.com'
        DOCKER_CREDENTIALS = 'docker-registry-credentials'
        
        // SonarQube configuration
        SONAR_TOKEN = credentials('sonar-token')
        
        // Slack notification (optional)
        SLACK_CHANNEL = '#deployments'
    }
    
    tools {
        maven 'Maven-3.9.5'
        jdk 'OpenJDK-17'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '🔍 Checking out source code...'
                checkout scm
                
                // Clean workspace
                cleanWs()
            }
        }
        
        stage('Validate') {
            steps {
                echo '✅ Validating project structure...'
                script {
                    // Check if essential files exist
                    def requiredFiles = ['pom.xml', 'src/main/java', 'src/main/resources/application.properties']
                    requiredFiles.each { file ->
                        if (!fileExists(file)) {
                            error "Required file/directory not found: ${file}"
                        }
                    }
                }
            }
        }
        
        stage('Dependencies') {
            steps {
                echo '📦 Downloading dependencies...'
                sh 'mvn dependency:resolve dependency:resolve-plugins'
            }
        }
        
        stage('Code Quality') {
            parallel {
                stage('SonarQube Analysis') {
                    when {
                        environment name: 'SONAR_TOKEN', value: ''
                        not { environment name: 'SONAR_TOKEN', value: null }
                    }
                    steps {
                        echo '🔍 Running SonarQube analysis...'
                        withSonarQubeEnv('SonarQube') {
                            sh """
                                mvn clean verify sonar:sonar \
                                    -Dsonar.projectKey=${APP_NAME} \
                                    -Dsonar.projectName='Spring Boot Application' \
                                    -Dsonar.projectVersion=${APP_VERSION} \
                                    -Dsonar.sources=src/main/java \
                                    -Dsonar.tests=src/test/java \
                                    -Dsonar.java.binaries=target/classes \
                                    -Dsonar.java.test.binaries=target/test-classes \
                                    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                            """
                        }
                    }
                }
                
                stage('Security Scan') {
                    steps {
                        echo '🔒 Running security scan...'
                        sh 'mvn org.owasp:dependency-check-maven:check'
                    }
                }
            }
        }
        
        stage('Test') {
            parallel {
                stage('Unit Tests') {
                    steps {
                        echo '🧪 Running unit tests...'
                        sh 'mvn test -Dtest=*Test'
                        
                        // Publish test results
                        publishTestResults testResultsPattern: '**/surefire-reports/*.xml'
                    }
                }
                
                stage('Integration Tests') {
                    steps {
                        echo '🔗 Running integration tests...'
                        sh 'mvn test -Dtest=*IntegrationTest'
                    }
                }
            }
            post {
                always {
                    // Publish test reports
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/site/jacoco',
                        reportFiles: 'index.html',
                        reportName: 'Coverage Report'
                    ])
                }
            }
        }
        
        stage('Build') {
            steps {
                echo '🏗️ Building application...'
                sh 'mvn clean package -DskipTests'
                
                // Archive the JAR file
                archiveArtifacts artifacts: "target/${APP_NAME}-${APP_VERSION}.jar", fingerprint: true
            }
        }
        
        stage('Docker Build') {
            steps {
                echo '🐳 Building Docker image...'
                script {
                    // Build Docker image
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                    
                    // Tag for latest
                    docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").inside {
                        sh 'echo "Docker image built successfully"'
                    }
                }
            }
        }
        
        stage('Docker Security Scan') {
            steps {
                echo '🔒 Scanning Docker image for vulnerabilities...'
                script {
                    // Use Trivy or similar tool for container scanning
                    sh 'docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image ${DOCKER_IMAGE}:${DOCKER_TAG}'
                }
            }
        }
        
        stage('Push to Registry') {
            when {
                anyOf {
                    branch 'main'
                    branch 'master'
                    branch 'develop'
                }
            }
            steps {
                echo '📤 Pushing Docker image to registry...'
                script {
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                            docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}
                            docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest
                            echo \$DOCKER_PASS | docker login ${DOCKER_REGISTRY} -u \$DOCKER_USER --password-stdin
                            docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}
                            docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest
                        """
                    }
                }
            }
        }
        
        stage('Deploy to Staging') {
            when {
                anyOf {
                    branch 'develop'
                    branch 'staging'
                }
            }
            steps {
                echo '🚀 Deploying to staging environment...'
                script {
                    // Deploy to staging environment
                    // This could be Kubernetes, Docker Swarm, or any other orchestration platform
                    sh """
                        # Example: Deploy to Kubernetes
                        # kubectl set image deployment/${APP_NAME} ${APP_NAME}=${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG} -n staging
                        
                        # Example: Deploy with Docker Compose
                        # docker-compose -f docker-compose.staging.yml up -d
                        
                        echo "Deployed ${DOCKER_IMAGE}:${DOCKER_TAG} to staging"
                    """
                }
            }
        }
        
        stage('Deploy to Production') {
            when {
                anyOf {
                    branch 'main'
                    branch 'master'
                }
            }
            steps {
                echo '🚀 Deploying to production environment...'
                script {
                    // Deploy to production environment
                    sh """
                        # Example: Deploy to Kubernetes
                        # kubectl set image deployment/${APP_NAME} ${APP_NAME}=${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG} -n production
                        
                        # Example: Deploy with Docker Compose
                        # docker-compose -f docker-compose.prod.yml up -d
                        
                        echo "Deployed ${DOCKER_IMAGE}:${DOCKER_TAG} to production"
                    """
                }
            }
        }
    }
    
    post {
        always {
            echo '🧹 Cleaning up workspace...'
            cleanWs()
        }
        
        success {
            echo '✅ Pipeline completed successfully!'
            script {
                // Send success notification
                if (env.SLACK_CHANNEL) {
                    slackSend(
                        channel: SLACK_CHANNEL,
                        color: 'good',
                        message: "✅ ${APP_NAME} build #${BUILD_NUMBER} completed successfully!",
                        attachments: [
                            [
                                title: 'Build Details',
                                fields: [
                                    [title: 'Branch', value: env.BRANCH_NAME, short: true],
                                    [title: 'Commit', value: env.GIT_COMMIT.take(8), short: true],
                                    [title: 'Duration', value: currentBuild.durationString, short: true]
                                ]
                            ]
                        ]
                    )
                }
            }
        }
        
        failure {
            echo '❌ Pipeline failed!'
            script {
                // Send failure notification
                if (env.SLACK_CHANNEL) {
                    slackSend(
                        channel: SLACK_CHANNEL,
                        color: 'danger',
                        message: "❌ ${APP_NAME} build #${BUILD_NUMBER} failed!",
                        attachments: [
                            [
                                title: 'Build Details',
                                fields: [
                                    [title: 'Branch', value: env.BRANCH_NAME, short: true],
                                    [title: 'Commit', value: env.GIT_COMMIT.take(8), short: true],
                                    [title: 'Duration', value: currentBuild.durationString, short: true]
                                ]
                            ]
                        ]
                    )
                }
            }
        }
        
        unstable {
            echo '⚠️ Pipeline is unstable!'
        }
    }
} 