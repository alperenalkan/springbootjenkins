pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'petstore:latest'
        DOCKER_CONTAINER = 'petstore-app'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Getting code from repository...'
                checkout scm
            }
        }
        
        stage('Deploy with Docker Compose') {
            steps {
                echo 'Deploying with Docker Compose...'
                script {
                    sh '''
                        cd /workspace
                        docker-compose -f docker-compose.app.yml down || true
                        docker-compose -f docker-compose.app.yml up -d --build
                    '''
                }
            }
        }
        
        stage('Health Check') {
            steps {
                echo 'Checking application health...'
                script {
                    sh '''
                        sleep 40
                        curl -f http://localhost:8080/actuator/health || exit 1
                    '''
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
        always {
            echo 'Build completed!'
        }
    }
}

