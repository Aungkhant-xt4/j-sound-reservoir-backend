# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .

COPY common-library/ common-library/
RUN mvn -f common-library/pom.xml install -DskipTests

COPY booking-service/pom.xml booking-service/
RUN mvn -f booking-service/pom.xml dependency:go-offline

COPY booking-service/src booking-service/src
RUN mvn -f booking-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/booking-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]