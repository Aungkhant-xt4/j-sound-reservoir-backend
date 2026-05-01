# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .

COPY common-library/ common-library/
RUN mvn -f common-library/pom.xml install -DskipTests

COPY event-service/pom.xml event-service/
RUN mvn -f event-service/pom.xml dependency:go-offline

COPY event-service/src event-service/src
RUN mvn -f event-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/event-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]