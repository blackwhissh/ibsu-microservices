# ---- Stage 1: Build ----
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy entire multi-module project
COPY . .

RUN mvn -pl profile-service -am clean package -DskipTests

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jre

# Copy the built jar from the previous stage
COPY --from=builder /app/profile-service/target/*.jar app.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]
