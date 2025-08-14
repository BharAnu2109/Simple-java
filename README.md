# Retail Business Microservices Architecture

## Overview

This project demonstrates a comprehensive Spring Boot microservices architecture for a retail business, implementing complex scenarios and distributed patterns. The architecture showcases modern enterprise-grade patterns including service discovery, API gateway, circuit breakers, and inter-service communication.

## Architecture Components

### Infrastructure Services
- **Eureka Server** (Port 8761) - Service Discovery and Registration
- **API Gateway** (Port 8080) - Single entry point with routing and circuit breakers

### Business Services
- **User Service** (Port 8081) - Customer and user management
- **Product Service** (Port 8082) - Product catalog and inventory management
- **Order Service** (Port 8083) - Order processing with distributed transactions

## Complex Business Scenarios Implemented

### 1. Service Discovery and Load Balancing
- All services register with Eureka Server
- API Gateway routes requests using service names
- Automatic failover and load balancing

### 2. Circuit Breaker Pattern
- API Gateway implements circuit breakers for all downstream services
- Order Service has circuit breakers for Product and User service calls
- Fallback responses prevent cascading failures

### 3. Inter-Service Communication
- Order Service communicates with User and Product services using OpenFeign
- Robust error handling with fallback mechanisms
- Distributed data consistency through compensating transactions

### 4. Data Management
- Each service has its own H2 database (microservice database pattern)
- JPA entities with proper relationships and validation
- Automated schema creation and lifecycle management

## Technology Stack

- **Spring Boot 2.7.14** - Main framework
- **Spring Cloud 2021.0.8** - Cloud-native patterns
- **Spring Cloud Gateway** - API Gateway with reactive programming
- **Netflix Eureka** - Service discovery
- **OpenFeign** - Declarative REST client
- **Resilience4j** - Circuit breaker implementation
- **Spring Data JPA** - Data access layer
- **H2 Database** - In-memory databases for development
- **Maven** - Multi-module project management

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Running the Application

1. **Start Eureka Server**
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```
   Access at: http://localhost:8761

2. **Start API Gateway**
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```
   Gateway available at: http://localhost:8080

3. **Start User Service**
   ```bash
   cd user-service
   mvn spring-boot:run
   ```
   Direct access: http://localhost:8081

4. **Start Product Service**
   ```bash
   cd product-service
   mvn spring-boot:run
   ```
   Direct access: http://localhost:8082

5. **Start Order Service**
   ```bash
   cd order-service
   mvn spring-boot:run
   ```
   Direct access: http://localhost:8083

### Alternative: Build and Run All Services
```bash
# Build all services
mvn clean package

# Run each service in separate terminals
java -jar eureka-server/target/eureka-server-1.0.0.jar
java -jar api-gateway/target/api-gateway-1.0.0.jar
java -jar user-service/target/user-service-1.0.0.jar
java -jar product-service/target/product-service-1.0.0.jar
java -jar order-service/target/order-service-1.0.0.jar
```

## API Documentation

### User Service APIs
- `GET /api/users` - Get all users
- `POST /api/users` - Create new user
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users/active` - Get active users
- `GET /api/users/search?name={name}` - Search users by name

### Product Service APIs
- `GET /api/products` - Get all products
- `POST /api/products` - Create new product
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/sku/{sku}` - Get product by SKU
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product
- `GET /api/products/category/{category}` - Get products by category
- `GET /api/products/search?q={query}` - Search products
- `PATCH /api/products/{id}/stock` - Update stock quantity
- `GET /api/products/low-stock` - Get low stock products

### Order Service APIs
- `GET /api/orders` - Get all orders
- `POST /api/orders` - Create new order
- `GET /api/orders/{id}` - Get order by ID
- `PUT /api/orders/{id}` - Update order
- `GET /api/orders/user/{userId}` - Get orders by user
- `PATCH /api/orders/{id}/status` - Update order status

### API Gateway Routes
All services are accessible through the API Gateway:
- `http://localhost:8080/api/users/**` → User Service
- `http://localhost:8080/api/products/**` → Product Service
- `http://localhost:8080/api/orders/**` → Order Service

## Testing the System

### 1. Health Checks
```bash
curl http://localhost:8080/api/users/health
curl http://localhost:8080/api/products/health
curl http://localhost:8080/api/orders/health
```

### 2. Create Test Data
```bash
# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "1234567890",
    "address": "123 Main St, City, State"
  }'

# Create a product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "sku": "LAP-001",
    "category": "Electronics",
    "stockQuantity": 10
  }'
```

## Complex Scenarios Demonstrated

### 1. Distributed Transaction Simulation
When creating an order:
1. Order Service validates user exists (calls User Service)
2. Order Service checks product availability (calls Product Service)
3. Order Service reduces product stock (calls Product Service)
4. If any step fails, compensating actions are taken

### 2. Circuit Breaker Testing
1. Stop Product Service
2. Try creating an order
3. Observe fallback responses and circuit breaker activation
4. Check Eureka dashboard for service status

### 3. Service Discovery
1. Services automatically register with Eureka
2. API Gateway discovers services dynamically
3. Load balancing across multiple instances (if running)

## Database Access

Each service provides H2 console access:
- User Service: http://localhost:8081/h2-console
- Product Service: http://localhost:8082/h2-console
- Order Service: http://localhost:8083/h2-console

JDBC URL format: `jdbc:h2:mem:{servicename}db` (e.g., `jdbc:h2:mem:userdb`)
Username: `sa`, Password: (empty)

## Monitoring and Management

All services expose actuator endpoints:
- Health: `/actuator/health`
- Info: `/actuator/info`
- Metrics: `/actuator/metrics`

Eureka Dashboard: http://localhost:8761

## Key Design Patterns

1. **Microservice Architecture** - Independent, deployable services
2. **Service Registry & Discovery** - Eureka-based service location
3. **API Gateway Pattern** - Single entry point with routing
4. **Circuit Breaker Pattern** - Fault tolerance and resilience
5. **Database per Service** - Data independence and autonomy
6. **Compensating Transaction** - Distributed transaction handling
7. **Fallback Pattern** - Graceful degradation during failures

## Future Enhancements

- Event-driven architecture with message queues
- Distributed configuration with Spring Cloud Config
- Payment service with transaction processing
- Inventory service with real-time stock management
- Notification service with email/SMS capabilities
- API documentation with Swagger/OpenAPI
- Containerization with Docker
- Orchestration with Kubernetes
- Centralized logging and monitoring

## Original Java Exercises

The original Java programming exercises have been preserved in the `original-exercises/` directory, maintaining the educational value of the repository while adding enterprise-grade microservices architecture.