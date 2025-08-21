FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Maven files for dependency resolution
COPY pom.xml ./
COPY ../common/pom.xml ./common/
COPY ../restaurant-service/pom.xml ./restaurant-service/

# Copy source code
COPY ../common/src ./common/src
COPY src ./restaurant-service/src

# Build the application
COPY ../pom.xml ../
RUN ./mvnw clean package -DskipTests

# Run the application
EXPOSE 8081

CMD ["java", "-jar", "restaurant-service/target/restaurant-service-1.0.0-SNAPSHOT.jar"]