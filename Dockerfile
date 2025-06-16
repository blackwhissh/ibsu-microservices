# Stage 1: Build stage (builds entire multi-module Maven project)
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copy Maven wrapper and config
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# Copy all module directories
COPY api-gateway ./api-gateway
COPY auth-service ./auth-service
COPY order-service ./order-service
COPY profile-service ./profile-service
COPY item-service ./item-service
COPY cart-service ./cart-service
COPY common ./common

# Download dependencies offline (optional)
RUN ./mvnw dependency:go-offline -B

# Build all modules and skip tests
RUN ./mvnw clean package -DskipTests

# Stage 2: Run stage (copies only one module’s jar to keep image small)
FROM eclipse-temurin:17-jre

ARG JAR_FILE=auth-service/target/*.jar
COPY --from=builder /app/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
