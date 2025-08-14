# Hotel Management System - Microservices Architecture

A comprehensive Spring Boot hotel management system with microservices architecture, Docker containerization, Kubernetes orchestration, and Kafka messaging.

## Architecture Overview

This system implements a complete hotel management solution using:
- **Spring Boot 3.2.0** with Java 17
- **Spring Cloud 2023.0.0** for microservices
- **Apache Kafka** for event-driven messaging
- **PostgreSQL** for persistent data storage
- **Redis** for caching
- **Docker** for containerization
- **Kubernetes** for orchestration
- **Eureka** for service discovery
- **Spring Cloud Gateway** for API routing

## System Services (100+ Features)

### Core Services
1. **API Gateway** (Port 8080)
   - Centralized routing and load balancing
   - Authentication and authorization
   - Rate limiting and circuit breaker
   - Request/response logging

2. **User Service** (Port 8081)
   - User registration and authentication
   - Profile management
   - Role-based access control (Guest, Staff, Manager, Admin)
   - User search and filtering

3. **Room Service** (Port 8082)
   - Room inventory management
   - Room types and pricing
   - Availability tracking
   - Room amenities management

4. **Booking Service** (Port 8083)
   - Reservation management
   - Check-in/check-out processing
   - Booking status tracking
   - Conflict resolution

5. **Payment Service** (Port 8084)
   - Payment processing
   - Multiple payment methods
   - Transaction history
   - Refund management

6. **Notification Service** (Port 8085)
   - Email notifications
   - SMS notifications
   - Push notifications
   - Notification templates

### Supporting Services
7. **Inventory Service** (Port 8086)
   - Hotel inventory management
   - Supplies tracking
   - Vendor management
   - Purchase orders

8. **Staff Service** (Port 8087)
   - Employee management
   - Shift scheduling
   - Department management
   - Performance tracking

9. **Housekeeping Service** (Port 8088)
   - Room cleaning schedules
   - Maintenance requests
   - Task assignments
   - Quality control

10. **Billing Service** (Port 8089)
    - Invoice generation
    - Bill calculations
    - Tax management
    - Payment reconciliation

11. **Reporting Service** (Port 8090)
    - Occupancy reports
    - Revenue analytics
    - Customer insights
    - Performance metrics

### Infrastructure Services
12. **Config Server** (Port 8888)
    - Centralized configuration management
    - Environment-specific configs
    - Dynamic configuration updates

13. **Eureka Server** (Port 8761)
    - Service discovery and registration
    - Health monitoring
    - Load balancing support

## Features by Service Category

### User Management (20+ Features)
- User registration and login
- Multi-role support (Guest, Staff, Manager, Admin)
- Profile management and updates
- Password reset and recovery
- User search and filtering
- Account activation/deactivation
- Login history tracking
- Security audit logs
- Two-factor authentication support
- Social media integration
- Guest preferences management
- Loyalty program integration
- User activity monitoring
- Account suspension/restoration
- Bulk user operations
- User import/export
- Guest feedback collection
- User analytics and reporting
- Permission management
- Session management

### Room Management (15+ Features)
- Room inventory tracking
- Multiple room types (Single, Double, Suite, etc.)
- Dynamic pricing management
- Amenities configuration
- Room status management
- Maintenance scheduling
- Room availability calendar
- Floor plans and layouts
- Room photo management
- Seasonal pricing
- Group booking support
- Room upgrade management
- Accessibility features
- Room assignment automation
- Occupancy optimization

### Booking & Reservations (25+ Features)
- Online booking system
- Real-time availability checking
- Booking confirmation and cancellation
- Check-in/check-out automation
- Guest history tracking
- Special requests handling
- Group reservations
- Corporate booking management
- Waitlist management
- Booking modifications
- No-show handling
- Early check-in/late check-out
- Room assignment optimization
- Overbooking management
- Booking analytics
- Seasonal promotions
- Package deals
- Loyalty discounts
- Booking reminders
- Guest communication
- Booking reports
- Revenue optimization
- Cancellation policies
- Refund processing
- Guest preferences tracking

### Payment & Billing (15+ Features)
- Multiple payment methods
- Secure payment processing
- Invoice generation
- Automatic billing
- Split billing support
- Corporate billing
- Tax calculations
- Discount management
- Refund processing
- Payment reconciliation
- Chargeback handling
- Financial reporting
- Revenue tracking
- Payment analytics
- Fraud detection

### Communication & Notifications (10+ Features)
- Email notifications
- SMS messaging
- Push notifications
- Booking confirmations
- Check-in reminders
- Payment alerts
- Maintenance notifications
- Staff communications
- Guest surveys
- Emergency alerts

### Inventory & Supplies (12+ Features)
- Supply inventory tracking
- Automated reordering
- Vendor management
- Purchase order processing
- Stock level monitoring
- Expiry date tracking
- Cost optimization
- Supplier analytics
- Quality control
- Asset management
- Maintenance supplies
- Guest amenities tracking

### Staff Management (15+ Features)
- Employee profiles
- Shift scheduling
- Time tracking
- Performance management
- Training records
- Department organization
- Role assignments
- Payroll integration
- Leave management
- Staff communication
- Performance reviews
- Skill tracking
- Certification management
- Staff analytics
- Compliance tracking

### Housekeeping Operations (8+ Features)
- Room cleaning schedules
- Task assignments
- Quality inspections
- Maintenance requests
- Inventory usage tracking
- Staff productivity monitoring
- Guest satisfaction feedback
- Cleaning protocols

## Technology Stack

### Backend Technologies
- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Application framework
- **Spring Cloud 2023.0.0** - Microservices framework
- **Spring Data JPA** - Data persistence
- **Spring Security** - Authentication and authorization
- **Spring Cloud Gateway** - API gateway
- **Spring Kafka** - Message streaming
- **PostgreSQL** - Primary database
- **H2** - In-memory database for development
- **Redis** - Caching and session storage
- **Maven** - Build and dependency management

### Messaging & Communication
- **Apache Kafka** - Event streaming platform
- **Zookeeper** - Kafka cluster coordination

### Containerization & Orchestration
- **Docker** - Application containerization
- **Docker Compose** - Multi-container orchestration
- **Kubernetes** - Container orchestration platform

### Monitoring & Observability
- **Spring Boot Actuator** - Application monitoring
- **Prometheus** - Metrics collection
- **Grafana** - Metrics visualization (can be added)
- **ELK Stack** - Logging (can be integrated)

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Docker and Docker Compose
- Kubernetes cluster (optional)

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Simple-java
   ```

2. **Build all services**
   ```bash
   mvn clean install
   ```

3. **Start infrastructure services**
   ```bash
   cd docker
   docker-compose up -d postgres kafka zookeeper redis
   ```

4. **Start discovery and configuration services**
   ```bash
   # Terminal 1 - Eureka Server
   cd eureka-server
   mvn spring-boot:run

   # Terminal 2 - Config Server
   cd config-server
   mvn spring-boot:run
   ```

5. **Start core services**
   ```bash
   # Terminal 3 - API Gateway
   cd api-gateway
   mvn spring-boot:run

   # Terminal 4 - User Service
   cd user-service
   mvn spring-boot:run

   # Terminal 5 - Room Service
   cd room-service
   mvn spring-boot:run

   # And so on for other services...
   ```

### Docker Deployment

1. **Build Docker images**
   ```bash
   mvn clean package
   docker-compose -f docker/docker-compose.yml build
   ```

2. **Start all services**
   ```bash
   docker-compose -f docker/docker-compose.yml up -d
   ```

### Kubernetes Deployment

1. **Apply Kubernetes manifests**
   ```bash
   kubectl apply -f kubernetes/
   ```

2. **Check deployment status**
   ```bash
   kubectl get pods -n hotel-management
   ```

## API Documentation

### API Gateway Routes
- **Users API**: `http://localhost:8080/api/users/**`
- **Rooms API**: `http://localhost:8080/api/rooms/**`
- **Bookings API**: `http://localhost:8080/api/bookings/**`
- **Payments API**: `http://localhost:8080/api/payments/**`
- **Notifications API**: `http://localhost:8080/api/notifications/**`
- **Inventory API**: `http://localhost:8080/api/inventory/**`
- **Staff API**: `http://localhost:8080/api/staff/**`
- **Housekeeping API**: `http://localhost:8080/api/housekeeping/**`
- **Billing API**: `http://localhost:8080/api/billing/**`
- **Reports API**: `http://localhost:8080/api/reports/**`

### Example API Calls

#### Create User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+1234567890",
    "role": "GUEST"
  }'
```

#### Get Available Rooms
```bash
curl -X GET "http://localhost:8080/api/rooms?status=AVAILABLE"
```

#### Create Booking
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "guestId": 1,
    "roomId": 1,
    "checkInDate": "2024-01-15",
    "checkOutDate": "2024-01-20",
    "numberOfGuests": 2
  }'
```

## Event-Driven Architecture

### Kafka Topics
- `user-events` - User registration, updates, deletions
- `booking-events` - Booking creation, modifications, cancellations
- `payment-events` - Payment processing, confirmations, failures
- `room-events` - Room status changes, maintenance updates
- `notification-events` - Notification triggers and deliveries
- `inventory-events` - Stock updates, reorder alerts
- `housekeeping-events` - Task assignments, completions

### Event Examples

#### Booking Created Event
```json
{
  "eventId": "uuid",
  "eventType": "BOOKING_CREATED",
  "timestamp": "2024-01-01T10:00:00Z",
  "source": "booking-service",
  "data": {
    "bookingId": 123,
    "guestId": 456,
    "roomId": 789,
    "checkInDate": "2024-01-15",
    "checkOutDate": "2024-01-20"
  }
}
```

## Configuration Management

### Environment-Specific Configurations
- **Development**: H2 database, local Kafka
- **Testing**: In-memory databases, embedded services
- **Production**: PostgreSQL, external Kafka cluster

### Configuration Files
- `application.yml` - Default configuration
- `application-dev.yml` - Development environment
- `application-prod.yml` - Production environment

## Security Features

### Authentication & Authorization
- JWT-based authentication
- Role-based access control (RBAC)
- Method-level security
- API rate limiting

### Security Best Practices
- Password encryption (BCrypt)
- SQL injection prevention
- XSS protection
- CSRF protection
- HTTPS enforcement

## Monitoring & Health Checks

### Health Endpoints
- `http://localhost:8080/actuator/health` - API Gateway health
- `http://localhost:8081/actuator/health` - User Service health
- `http://localhost:8082/actuator/health` - Room Service health
- And so on for all services...

### Metrics
- Application metrics via Actuator
- Prometheus integration
- Custom business metrics
- Performance monitoring

## Scalability Features

### Horizontal Scaling
- Stateless service design
- Load balancing via API Gateway
- Database connection pooling
- Kafka partitioning

### Performance Optimization
- Redis caching
- Database indexing
- Query optimization
- Async processing

## Testing Strategy

### Testing Levels
- **Unit Tests** - Individual component testing
- **Integration Tests** - Service interaction testing
- **Contract Tests** - API contract validation
- **End-to-End Tests** - Complete workflow testing

### Test Technologies
- JUnit 5 for unit testing
- TestContainers for integration testing
- MockWebServer for external service mocking
- Kafka test containers for messaging tests

## Development Guidelines

### Code Quality
- SonarQube integration
- Checkstyle enforcement
- PMD static analysis
- Jacoco code coverage

### Version Control
- GitFlow branching model
- Conventional commits
- Pull request reviews
- Automated CI/CD

## Deployment Strategies

### Blue-Green Deployment
- Zero-downtime deployments
- Rollback capabilities
- A/B testing support

### Canary Releases
- Gradual feature rollouts
- Risk mitigation
- Performance monitoring

## Future Enhancements

### Planned Features
- Mobile application support
- IoT device integration
- Machine learning for pricing optimization
- Blockchain for secure transactions
- Voice-activated room controls
- Augmented reality for virtual tours

### Technology Upgrades
- Migration to Spring Boot 4.x
- Kubernetes operators
- Service mesh integration (Istio)
- Serverless functions

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation wiki

---

**Note**: This is a comprehensive hotel management system designed for production use with enterprise-grade features and scalability considerations.