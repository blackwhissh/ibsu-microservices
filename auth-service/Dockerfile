# ---- Stage 1: Build ----
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copy entire multi-module project
COPY . .

# Install Maven (Debian-based, suitable for Temurin)
RUN apt-get update && apt-get install -y maven

# Build only auth-service and its dependencies (e.g., common)
RUN mvn -pl auth-service -am clean package -DskipTests

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jre

# Copy the built jar from the previous stage
COPY --from=builder /app/auth-service/target/*.jar app.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]
