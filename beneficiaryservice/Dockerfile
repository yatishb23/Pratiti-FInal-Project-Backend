# Build stage
FROM maven:3.9.9-eclipse-temurin-22 AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:22-jdk-slim
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8761