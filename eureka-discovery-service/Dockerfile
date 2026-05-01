# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .

COPY eureka-discovery-service/pom.xml eureka-discovery-service/
RUN mvn -f eureka-discovery-service/pom.xml dependency:go-offline

COPY eureka-discovery-service/src eureka-discovery-service/src
RUN mvn -f eureka-discovery-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/eureka-discovery-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]