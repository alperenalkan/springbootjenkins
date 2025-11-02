pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9'
        jdk 'JDK 17'
    }
    
    environment {
        DOCKER_IMAGE = 'petstore:latest'
        DOCKER_CONTAINER = 'petstore-app'
        REGISTRY = 'your-registry.com' // Docker registry URL'inizi buraya ekleyin
        DOCKER_CREDENTIALS = 'docker-credentials' // Jenkins credentials ID
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Getting code from repository...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building the application...'
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Packaging the application...'
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                script {
                    dockerImage = docker.build("${DOCKER_IMAGE}")
                }
            }
        }
        
        stage('Docker Push') {
            when {
                branch 'main'
            }
            steps {
                echo 'Pushing Docker image to registry...'
                script {
                    docker.withRegistry("https://${REGISTRY}", DOCKER_CREDENTIALS) {
                        dockerImage.push("latest")
                    }
                }
            }
        }
        
        stage('Deploy with Docker Compose') {
            steps {
                echo 'Deploying with Docker Compose...'
                script {
                    sh '''
                        docker-compose down
                        docker-compose up -d --build
                    '''
                }
            }
        }
        
        stage('Health Check') {
            steps {
                echo 'Checking application health...'
                script {
                    sh '''
                        sleep 30
                        curl -f http://localhost:8080/actuator/health || exit 1
                    '''
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline succeeded!'
            mail to: 'team@example.com',
                 subject: "Pipeline SUCCESS: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                 body: "The build was successful. View: ${env.BUILD_URL}"
        }
        failure {
            echo 'Pipeline failed!'
            mail to: 'team@example.com',
                 subject: "Pipeline FAILURE: ${env.JOB_NAME} - ${env.BUILD_NUMBER}",
                 body: "The build failed. View: ${env.BUILD_URL}"
        }
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
    }
}

