FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY target/library-management-system-1.0.0.jar app.jar
RUN mkdir -p /data
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
