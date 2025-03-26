# Sử dụng hình ảnh JDK 23 chính thức làm base image
FROM eclipse-temurin:23-jdk AS builder

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Sao chép toàn bộ thư mục backend vào container
COPY ./tlu_contact_be /app/

# Cài đặt Maven 4.0.0
RUN apt-get update && apt-get install -y wget \
    && wget https://downloads.apache.org/maven/maven-4/4.0.0/binaries/apache-maven-4.0.0-bin.tar.gz \
    && tar -xzf apache-maven-4.0.0-bin.tar.gz -C /opt/ \
    && ln -s /opt/apache-maven-4.0.0 /opt/maven \
    && rm apache-maven-4.0.0-bin.tar.gz

# Thiết lập biến môi trường cho Maven
ENV MAVEN_HOME=/opt/maven
ENV PATH=${MAVEN_HOME}/bin:${PATH}

# Build dự án Spring Boot
RUN mvn clean package -DskipTests

# Giai đoạn chạy ứng dụng
FROM eclipse-temurin:23-jre AS runtime

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file JAR đã build từ giai đoạn trước
COPY --from=builder /app/target/*.jar app.jar

# Mở cổng mà Spring Boot sử dụng (mặc định là 8080, thay đổi nếu cần)
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]