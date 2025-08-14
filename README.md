# Wrestling Complex Scenarios - Spring Boot Application

A Spring Boot application that demonstrates complex scenarios using Java 8 streams in a wrestling context. This application transforms simple Java stream examples into a comprehensive wrestling analytics API.

## Features

This application provides REST endpoints for wrestling analytics that utilize complex Java 8 stream operations:

- **Tournament Analytics**: Partition wrestlers by performance metrics
- **Rating Analysis**: Find wrestlers with specific rating patterns  
- **Character Analysis**: Analyze duplicate letters in wrestler names
- **Statistical Operations**: Calculate win percentages, rankings, and category distributions
- **Complex Filtering**: Advanced filtering and grouping operations

## Technical Implementation

The application integrates existing Java 8 stream examples:
- `FindDuplicates.java` → Character frequency analysis for wrestler names
- `ElementsStartingWithOne.java` → Pattern matching for wrestler ratings
- `Java8Code.java` → Partitioning wrestlers by performance thresholds
- `SortDescending.java` → Ranking wrestlers by various metrics
- `FirstNonRepeated.java` → Finding unique characters across all wrestler names
- `findCountOfChars.java` → Character frequency analysis

## API Endpoints

### Wrestler Information
- `GET /api/wrestling/wrestlers` - Get all wrestlers
- `GET /api/wrestling/wrestlers/top-by-rating` - Get wrestlers sorted by rating (desc)
- `GET /api/wrestling/wrestlers/top-performer` - Get wrestler with highest win percentage
- `GET /api/wrestling/wrestlers/second-highest-rated` - Get second highest rated wrestler

### Analytics Endpoints
- `GET /api/wrestling/analytics/partition-by-winrate?threshold=70.0` - Partition wrestlers by win rate
- `GET /api/wrestling/analytics/ratings-starting-with/{digit}` - Find ratings starting with specific digit
- `GET /api/wrestling/analytics/category-stats` - Get wrestler count by category
- `GET /api/wrestling/analytics/duplicate-letters/{name}` - Find duplicate letters in name
- `GET /api/wrestling/analytics/first-unique-char` - Get first unique character across all names
- `GET /api/wrestling/analytics/character-frequency` - Get character frequency in all names

### Health Check
- `GET /api/wrestling/health` - Application health status

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build and Run
```bash
# Build the application
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080/wrestling-app`

### Example Usage
```bash
# Check application health
curl http://localhost:8080/wrestling-app/api/wrestling/health

# Get top wrestlers by rating
curl http://localhost:8080/wrestling-app/api/wrestling/wrestlers/top-by-rating

# Partition wrestlers by 70% win rate threshold
curl "http://localhost:8080/wrestling-app/api/wrestling/analytics/partition-by-winrate?threshold=70.0"

# Find duplicate letters in "Stone Cold"
curl "http://localhost:8080/wrestling-app/api/wrestling/analytics/duplicate-letters/Stone%20Cold"
```

## Project Structure
```
src/
├── main/
│   ├── java/com/wrestling/
│   │   ├── WrestlingApplication.java      # Main Spring Boot application
│   │   ├── controller/
│   │   │   └── WrestlingController.java   # REST endpoints
│   │   ├── service/
│   │   │   └── WrestlingAnalyticsService.java # Business logic with stream operations
│   │   └── model/
│   │       └── Wrestler.java              # Wrestler data model
│   └── resources/
│       └── application.properties         # Application configuration
└── Original Java files (preserved as examples)
```

## Wrestling Data

The application includes sample data for 10 famous wrestlers with their ratings, categories, wins, and losses. All analytics operations are performed on this dataset using Java 8 streams.