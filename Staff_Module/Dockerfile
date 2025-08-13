# === Stage 1: Build ứng dụng với Maven ===
FROM maven:3.9.4-eclipse-temurin-17-alpine AS builder
WORKDIR /build
COPY . .

# Build jar
RUN mvn clean package -DskipTests -DskipITS

FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copy jar từ stage 1
COPY --from=builder /build/target/Staff_Module-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]