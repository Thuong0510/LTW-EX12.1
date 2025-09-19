# ---------- Stage 1: build WAR ----------
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -e -DskipTests package
# WAR sẽ nằm ở target/<artifactId>-<version>.war

# ---------- Stage 2: run with Tomcat 11 (Servlet 6 / Jakarta EE 10) ----------
FROM tomcat:11.0.0-jdk17-temurin
# Xóa apps mặc định
RUN rm -rf /usr/local/tomcat/webapps/*
# Copy WAR thành ROOT.war để app chạy ở "/"
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# (tùy chọn) set timezone/locale
ENV TZ=Asia/Ho_Chi_Minh

# (tùy chọn) truyền DB config qua env
ENV DB_URL="jdbc:mysql://host.docker.internal:3306/newdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8"
ENV DB_USER="root"
ENV DB_PASS="Thuong@123"

EXPOSE 8080
CMD ["catalina.sh", "run"]
