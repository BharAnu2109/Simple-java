# Restaurant Management System

## Modern Microservices Restaurant Application

A comprehensive restaurant management system built with **Spring Boot 3.2**, **Angular 20**, **Apache Kafka 3.6**, **AWS SDK 2.22**, and **Docker**.

### 🏗️ Architecture Overview

This application demonstrates a modern microservices architecture with:

- **Spring Boot 3.2** - Latest Spring framework with Java 17
- **Angular 20** - Modern frontend framework with standalone components
- **Apache Kafka 3.6** - Event-driven architecture for inter-service communication
- **AWS Integration** - S3, DynamoDB, SQS for cloud-native features
- **PostgreSQL** - Relational database for persistent storage
- **Redis** - Caching and session management
- **Docker** - Containerization for deployment

### 🚀 Microservices

#### 1. Common Module
- Shared entities, DTOs, and event classes
- Base entity with audit fields
- Kafka event definitions

#### 2. Restaurant Service (Port: 8081)
- Restaurant and menu management
- Menu item catalog with categories
- Restaurant search and filtering
- Location-based delivery radius calculations

#### 3. Order Service (Port: 8082)
- Order creation and processing
- Order status tracking with real-time updates
- Integration with restaurant service for menu validation
- Kafka event publishing for order lifecycle

#### 4. Frontend (Angular 20)
- Modern reactive UI with standalone components
- Restaurant discovery and menu browsing
- Real-time order tracking
- Mobile-responsive design with SCSS

### 🛠️ Technology Stack

#### Backend
- **Spring Boot 3.2.1** - Enterprise Java framework
- **Spring Cloud 2023.0.0** - Microservices patterns
- **Spring Kafka 3.1.0** - Event streaming
- **Spring Data JPA** - Database abstraction
- **H2 Database** - Development database
- **PostgreSQL** - Production database
- **AWS SDK 2.22.7** - Cloud services integration
- **TestContainers** - Integration testing

#### Frontend
- **Angular 20.2.0** - Progressive web app framework
- **TypeScript 5.9** - Type-safe JavaScript
- **SCSS** - Enhanced CSS with variables and mixins
- **RxJS 7.8** - Reactive programming
- **Angular HTTP Client** - RESTful API communication

#### Infrastructure
- **Apache Kafka 3.6** - Event streaming platform
- **Docker Compose** - Local development environment
- **LocalStack** - AWS services simulation
- **Redis** - Caching layer
- **Eureka Server** - Service discovery

### 🎯 Complex Restaurant Scenarios

#### Real-time Order Tracking
- Order status updates published via Kafka
- Live notifications to customers and restaurant staff
- Estimated delivery time calculations

#### Location-based Services
- Delivery radius calculations using geospatial queries
- Restaurant discovery based on customer location
- Dynamic delivery fee computation

#### Inventory Management
- Menu item availability tracking
- Popular items analytics
- Category-based organization

#### Multi-tenant Architecture
- Support for multiple restaurant locations
- Restaurant-specific menus and pricing
- Centralized order management

#### Event-driven Communication
- Order created events trigger payment processing
- Status updates notify multiple services
- Audit trail for all operations

### 🔧 Setup Instructions

#### Prerequisites
- Java 17+
- Node.js 20+
- Maven 3.6+
- Docker & Docker Compose

#### Local Development

1. **Start Infrastructure Services**
   ```bash
   docker-compose up -d
   ```

2. **Build and Run Backend Services**
   ```bash
   # Build all services
   mvn clean install
   
   # Run Restaurant Service
   cd restaurant-service
   mvn spring-boot:run
   
   # Run Order Service (in another terminal)
   cd order-service
   mvn spring-boot:run
   ```

3. **Run Frontend**
   ```bash
   cd frontend
   npm install
   npm start
   ```

#### Access Points
- **Frontend**: http://localhost:4200
- **Restaurant Service**: http://localhost:8081
- **Order Service**: http://localhost:8082
- **Kafka UI**: http://localhost:8080
- **Eureka Dashboard**: http://localhost:8761

### 📊 API Documentation

#### Restaurant Service APIs
- `GET /api/restaurants` - List all restaurants
- `GET /api/restaurants/{id}` - Get restaurant details
- `GET /api/restaurants/search` - Search restaurants
- `GET /api/restaurants/{id}/menu` - Get restaurant menu
- `POST /api/restaurants` - Create restaurant
- `PUT /api/restaurants/{id}` - Update restaurant

#### Order Service APIs
- `POST /api/orders` - Create new order
- `GET /api/orders/{id}` - Get order details
- `GET /api/orders/customer/{customerId}` - Get customer orders
- `PUT /api/orders/{id}/status` - Update order status
- `POST /api/orders/customer/{customerId}/order/{orderId}/cancel` - Cancel order

### 🧪 Testing

#### Backend Testing
```bash
mvn test  # Unit tests
mvn verify  # Integration tests with TestContainers
```

#### Frontend Testing
```bash
cd frontend
npm test  # Unit tests with Jest
npm run e2e  # End-to-end tests
```

### 🚢 Production Deployment

#### Docker Containerization
Each service includes Dockerfile for containerization:
```bash
# Build service images
docker build -t restaurant-service ./restaurant-service
docker build -t order-service ./order-service
docker build -t frontend ./frontend
```

#### AWS Deployment
- **ECS/EKS** - Container orchestration
- **Application Load Balancer** - Traffic distribution
- **RDS PostgreSQL** - Managed database
- **MSK** - Managed Kafka service
- **ElastiCache** - Redis caching
- **S3** - Static asset storage

### 🔍 Monitoring & Observability

#### Health Checks
- Spring Boot Actuator endpoints
- Custom health indicators
- Database connectivity checks

#### Metrics & Monitoring
- Prometheus metrics exposure
- Grafana dashboards
- Custom business metrics

#### Logging
- Structured JSON logging
- Centralized log aggregation
- Request correlation IDs

### 🔐 Security Features

#### Authentication & Authorization
- JWT token-based authentication
- Role-based access control
- API rate limiting

#### Data Protection
- Input validation and sanitization
- SQL injection prevention
- CORS configuration

### 🎨 Frontend Features

#### Modern UI/UX
- Responsive design for all devices
- Loading states and error handling
- Progressive web app capabilities

#### Real-time Features
- WebSocket integration for live updates
- Push notifications
- Offline capability

### 📈 Performance Optimizations

#### Backend
- Connection pooling
- Database query optimization
- Caching strategies
- Async processing

#### Frontend
- Lazy loading of routes
- Image optimization
- Bundle size optimization
- Service worker caching

### 🤝 Contributing

1. Fork the repository
2. Create feature branch
3. Write tests for new features
4. Ensure all tests pass
5. Submit pull request

### 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Built with ❤️ using Spring Boot 3.2, Angular 20, Kafka 3.6, and modern DevOps practices**