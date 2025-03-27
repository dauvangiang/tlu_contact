# Stage 1: Build ứng dụng
FROM maven:3.9.9-eclipse-temurin-23 AS builder

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép file pom.xml và tải dependencies trước để tận dụng cache
COPY ./pom.xml .
RUN mvn dependency:go-offline

# Sao chép toàn bộ mã nguồn và build ứng dụng
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Tạo image chạy ứng dụng
FROM eclipse-temurin:23-jre

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép file JAR từ stage build
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Thiết lập biến môi trường
ENV JAVA_OPTS=""

# Chạy ứng dụng
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]