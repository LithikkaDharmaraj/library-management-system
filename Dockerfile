FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /build/target/library-management-system-1.0.0.jar app.jar
RUN mkdir -p /data
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
