# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .

COPY common-library/ common-library/
RUN mvn -f common-library/pom.xml install -DskipTests

COPY inventory-service/pom.xml inventory-service/
RUN mvn -f inventory-service/pom.xml dependency:go-offline

COPY inventory-service/src inventory-service/src
RUN mvn -f inventory-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/inventory-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]