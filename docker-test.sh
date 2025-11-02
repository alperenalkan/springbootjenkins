#!/bin/bash

# PetStore Docker Test Script
# This script helps test the Docker setup locally

set -e

echo "🐳 PetStore Docker Test Script"
echo "================================"
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

# Check if Docker is installed
check_docker() {
    print_info "Checking Docker installation..."
    if command -v docker &> /dev/null; then
        print_success "Docker is installed"
        docker --version
    else
        print_error "Docker is not installed"
        exit 1
    fi
}

# Check if Docker Compose is installed
check_docker_compose() {
    print_info "Checking Docker Compose installation..."
    if command -v docker-compose &> /dev/null || docker compose version &> /dev/null; then
        print_success "Docker Compose is installed"
        docker-compose --version 2>/dev/null || docker compose version
    else
        print_error "Docker Compose is not installed"
        exit 1
    fi
}

# Build Docker image
build_image() {
    print_info "Building Docker image..."
    if docker build -t petstore:test .; then
        print_success "Docker image built successfully"
    else
        print_error "Failed to build Docker image"
        exit 1
    fi
}

# Start containers
start_containers() {
    print_info "Starting containers with Docker Compose..."
    if docker-compose up -d --build; then
        print_success "Containers started successfully"
    else
        print_error "Failed to start containers"
        exit 1
    fi
}

# Wait for application to be ready
wait_for_app() {
    print_info "Waiting for application to start..."
    max_attempts=30
    attempt=0
    
    while [ $attempt -lt $max_attempts ]; do
        if curl -f http://localhost:8080/actuator/health &> /dev/null; then
            print_success "Application is ready!"
            return 0
        fi
        attempt=$((attempt + 1))
        echo -n "."
        sleep 2
    done
    
    print_error "Application did not start within expected time"
    return 1
}

# Test health endpoint
test_health() {
    print_info "Testing health endpoint..."
    response=$(curl -s http://localhost:8080/actuator/health)
    if [ $? -eq 0 ]; then
        print_success "Health check passed"
        echo "Response: $response"
    else
        print_error "Health check failed"
        return 1
    fi
}

# Show container logs
show_logs() {
    print_info "Container logs:"
    echo "==================="
    docker-compose logs app --tail=20
}

# Stop containers
stop_containers() {
    print_info "Stopping containers..."
    docker-compose down
    print_success "Containers stopped"
}

# Clean up
cleanup() {
    print_info "Cleaning up..."
    docker-compose down -v
    docker rmi petstore:test 2>/dev/null || true
    print_success "Cleanup complete"
}

# Main menu
show_menu() {
    echo ""
    echo "Select an option:"
    echo "1) Full test (build + start + test)"
    echo "2) Build Docker image only"
    echo "3) Start containers only"
    echo "4) Test health endpoint"
    echo "5) Show container logs"
    echo "6) Stop containers"
    echo "7) Clean up everything"
    echo "8) Exit"
    echo ""
    read -p "Enter choice [1-8]: " choice
    echo ""
}

# Main execution
main() {
    check_docker
    check_docker_compose
    echo ""
    
    while true; do
        show_menu
        
        case $choice in
            1)
                build_image
                start_containers
                wait_for_app && test_health
                echo ""
                print_info "Full test completed. Check logs above."
                ;;
            2)
                build_image
                ;;
            3)
                start_containers
                wait_for_app
                ;;
            4)
                test_health
                ;;
            5)
                show_logs
                ;;
            6)
                stop_containers
                ;;
            7)
                stop_containers
                cleanup
                ;;
            8)
                print_info "Exiting..."
                exit 0
                ;;
            *)
                print_error "Invalid option"
                ;;
        esac
    done
}

# Run main function
main

