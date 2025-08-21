FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Maven files for dependency resolution
COPY pom.xml ./
COPY ../common/pom.xml ./common/
COPY ../order-service/pom.xml ./order-service/

# Copy source code
COPY ../common/src ./common/src
COPY src ./order-service/src

# Build the application
COPY ../pom.xml ../
RUN ./mvnw clean package -DskipTests

# Run the application
EXPOSE 8082

CMD ["java", "-jar", "order-service/target/order-service-1.0.0-SNAPSHOT.jar"]