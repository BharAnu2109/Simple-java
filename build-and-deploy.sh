#!/bin/bash

# Hotel Management System Build and Deployment Script

set -e

echo "🏨 Hotel Management System - Build & Deploy Script"
echo "================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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

# Check prerequisites
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    # Check Java
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed. Please install Java 17 or higher."
        exit 1
    fi
    
    java_version=$(java -version 2>&1 | head -n1 | awk -F '"' '{print $2}' | awk -F '.' '{print $1}')
    if [ "$java_version" -lt 17 ]; then
        print_error "Java 17 or higher is required. Current version: $java_version"
        exit 1
    fi
    
    # Check Maven
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed. Please install Maven 3.6 or higher."
        exit 1
    fi
    
    # Check Docker
    if ! command -v docker &> /dev/null; then
        print_warning "Docker is not installed. Docker features will be skipped."
        DOCKER_AVAILABLE=false
    else
        DOCKER_AVAILABLE=true
    fi
    
    # Check Kubernetes
    if ! command -v kubectl &> /dev/null; then
        print_warning "kubectl is not installed. Kubernetes deployment will be skipped."
        KUBECTL_AVAILABLE=false
    else
        KUBECTL_AVAILABLE=true
    fi
    
    print_success "Prerequisites check completed"
}

# Build all services
build_services() {
    print_status "Building all services..."
    
    # Clean and install all modules
    mvn clean install -DskipTests
    
    if [ $? -eq 0 ]; then
        print_success "All services built successfully"
    else
        print_error "Build failed"
        exit 1
    fi
}

# Run tests
run_tests() {
    print_status "Running tests..."
    
    mvn test
    
    if [ $? -eq 0 ]; then
        print_success "All tests passed"
    else
        print_warning "Some tests failed. Check the output above."
    fi
}

# Build Docker images
build_docker_images() {
    if [ "$DOCKER_AVAILABLE" = false ]; then
        print_warning "Docker not available. Skipping Docker image build."
        return
    fi
    
    print_status "Building Docker images..."
    
    # Build images for each service
    services=("api-gateway" "user-service" "room-service" "booking-service" "config-server")
    
    for service in "${services[@]}"; do
        if [ -f "$service/Dockerfile" ]; then
            print_status "Building $service image..."
            docker build -t "hotel-management/$service:latest" "$service/"
            if [ $? -eq 0 ]; then
                print_success "$service image built successfully"
            else
                print_error "Failed to build $service image"
            fi
        else
            print_warning "Dockerfile not found for $service"
        fi
    done
}

# Start infrastructure services
start_infrastructure() {
    if [ "$DOCKER_AVAILABLE" = false ]; then
        print_warning "Docker not available. Cannot start infrastructure services."
        return
    fi
    
    print_status "Starting infrastructure services..."
    
    cd docker
    docker-compose up -d postgres kafka zookeeper redis
    
    if [ $? -eq 0 ]; then
        print_success "Infrastructure services started"
        print_status "Waiting for services to be ready..."
        sleep 30
    else
        print_error "Failed to start infrastructure services"
        exit 1
    fi
    
    cd ..
}

# Deploy to Docker Compose
deploy_docker() {
    if [ "$DOCKER_AVAILABLE" = false ]; then
        print_warning "Docker not available. Skipping Docker deployment."
        return
    fi
    
    print_status "Deploying all services with Docker Compose..."
    
    cd docker
    docker-compose up -d
    
    if [ $? -eq 0 ]; then
        print_success "All services deployed successfully"
        print_status "Services are starting up. This may take a few minutes..."
        
        # Wait for services to be ready
        sleep 60
        
        print_status "Checking service health..."
        check_service_health
    else
        print_error "Deployment failed"
        exit 1
    fi
    
    cd ..
}

# Deploy to Kubernetes
deploy_kubernetes() {
    if [ "$KUBECTL_AVAILABLE" = false ]; then
        print_warning "kubectl not available. Skipping Kubernetes deployment."
        return
    fi
    
    print_status "Deploying to Kubernetes..."
    
    # Apply all Kubernetes manifests
    kubectl apply -f kubernetes/
    
    if [ $? -eq 0 ]; then
        print_success "Kubernetes deployment initiated"
        print_status "Waiting for pods to be ready..."
        
        # Wait for pods to be ready
        kubectl wait --for=condition=ready pod -l app=api-gateway -n hotel-management --timeout=300s
        
        print_success "Kubernetes deployment completed"
    else
        print_error "Kubernetes deployment failed"
        exit 1
    fi
}

# Check service health
check_service_health() {
    print_status "Checking service health..."
    
    services=(
        "API Gateway:http://localhost:8080/actuator/health"
        "User Service:http://localhost:8081/actuator/health"
        "Room Service:http://localhost:8082/actuator/health"
    )
    
    for service_info in "${services[@]}"; do
        service_name=$(echo $service_info | cut -d: -f1)
        service_url=$(echo $service_info | cut -d: -f2,3)
        
        print_status "Checking $service_name..."
        
        # Try to reach the health endpoint
        response=$(curl -s -o /dev/null -w "%{http_code}" "$service_url" 2>/dev/null || echo "000")
        
        if [ "$response" = "200" ]; then
            print_success "$service_name is healthy"
        else
            print_warning "$service_name is not responding (HTTP $response)"
        fi
    done
}

# Show service URLs
show_service_urls() {
    print_status "Service URLs:"
    echo "============="
    echo "🌐 API Gateway: http://localhost:8080"
    echo "👥 User Service: http://localhost:8081"
    echo "🏠 Room Service: http://localhost:8082"
    echo "📅 Booking Service: http://localhost:8083"
    echo "💳 Payment Service: http://localhost:8084"
    echo "📧 Notification Service: http://localhost:8085"
    echo "📦 Inventory Service: http://localhost:8086"
    echo "👷 Staff Service: http://localhost:8087"
    echo "🧹 Housekeeping Service: http://localhost:8088"
    echo "💰 Billing Service: http://localhost:8089"
    echo "📊 Reporting Service: http://localhost:8090"
    echo ""
    echo "📋 Eureka Dashboard: http://localhost:8761"
    echo "⚙️  Config Server: http://localhost:8888"
    echo ""
    echo "📖 API Documentation:"
    echo "   Users: http://localhost:8080/api/users"
    echo "   Rooms: http://localhost:8080/api/rooms"
    echo "   Bookings: http://localhost:8080/api/bookings"
}

# Clean up
cleanup() {
    print_status "Cleaning up..."
    
    if [ "$DOCKER_AVAILABLE" = true ]; then
        cd docker
        docker-compose down
        cd ..
    fi
    
    if [ "$KUBECTL_AVAILABLE" = true ]; then
        kubectl delete -f kubernetes/ --ignore-not-found=true
    fi
    
    print_success "Cleanup completed"
}

# Show help
show_help() {
    echo "Hotel Management System Build Script"
    echo ""
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  build         Build all services"
    echo "  test          Run tests"
    echo "  docker-build  Build Docker images"
    echo "  docker-deploy Deploy with Docker Compose"
    echo "  k8s-deploy    Deploy to Kubernetes"
    echo "  health        Check service health"
    echo "  urls          Show service URLs"
    echo "  cleanup       Clean up deployments"
    echo "  full          Full build and deployment"
    echo "  help          Show this help"
    echo ""
    echo "Examples:"
    echo "  $0 full               # Complete build and deployment"
    echo "  $0 build              # Just build the services"
    echo "  $0 docker-deploy      # Deploy using Docker Compose"
    echo "  $0 k8s-deploy         # Deploy to Kubernetes"
}

# Main execution
main() {
    case "${1:-full}" in
        "build")
            check_prerequisites
            build_services
            ;;
        "test")
            check_prerequisites
            run_tests
            ;;
        "docker-build")
            check_prerequisites
            build_services
            build_docker_images
            ;;
        "docker-deploy")
            check_prerequisites
            build_services
            build_docker_images
            deploy_docker
            show_service_urls
            ;;
        "k8s-deploy")
            check_prerequisites
            build_services
            build_docker_images
            deploy_kubernetes
            show_service_urls
            ;;
        "health")
            check_service_health
            ;;
        "urls")
            show_service_urls
            ;;
        "cleanup")
            cleanup
            ;;
        "full")
            check_prerequisites
            build_services
            run_tests
            build_docker_images
            start_infrastructure
            deploy_docker
            show_service_urls
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        *)
            print_error "Unknown command: $1"
            echo ""
            show_help
            exit 1
            ;;
    esac
}

# Run main function with all arguments
main "$@"