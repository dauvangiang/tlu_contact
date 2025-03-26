# Sử dụng hình ảnh JDK 23 chính thức làm base image
FROM eclipse-temurin:23-jdk AS builder

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Sao chép toàn bộ thư mục backend vào container
COPY ./tlu_contact_be /app/

# Build dự án Spring Boot bằng Maven Wrapper
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Giai đoạn chạy ứng dụng
FROM eclipse-temurin:23-jre AS runtime

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file JAR đã build từ giai đoạn trước
COPY --from=builder /app/target/*.jar app.jar

# Mở cổng mà Spring Boot sử dụng (mặc định là 8080)
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]