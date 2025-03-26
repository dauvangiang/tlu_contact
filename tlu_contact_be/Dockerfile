# Stage 1: Build ứng dụng với Gradle
FROM maven:3.9.9-eclipse-temurin-23 as build
WORKDIR /app

COPY /tlu_contact_be /app

RUN chmod +x ./mvnw || true

RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng với OpenJDK
FROM openjdk:23-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
