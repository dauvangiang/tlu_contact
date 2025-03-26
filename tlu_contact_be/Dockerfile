# Stage 1: Build ứng dụng với Maven
FROM maven:3.9.9-eclipse-temurin-23 as build
WORKDIR /app/tlu_contact_be

# Copy chỉ thư mục backend vào container
COPY ./tlu_contact_be /app/tlu_contact_be

# Thêm quyền thực thi cho Maven Wrapper nếu có
RUN chmod +x mvnw || true

# Build ứng dụng, bỏ qua test
RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng với OpenJDK 23
FROM openjdk:23-jdk
WORKDIR /app

# Copy file JAR đã build từ stage 1 vào container
COPY --from=build /app/tlu_contact_be/target/*.jar /app/app.jar

# Mở port 8080
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
