# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .

COPY spring-cloud-gateway/pom.xml spring-cloud-gateway/
RUN mvn -f spring-cloud-gateway/pom.xml dependency:go-offline

COPY spring-cloud-gateway/src spring-cloud-gateway/src
RUN mvn -f spring-cloud-gateway/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/spring-cloud-gateway/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]