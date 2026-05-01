# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .

COPY common-library/ common-library/
RUN mvn -f common-library/pom.xml install -DskipTests

COPY talent-service/pom.xml talent-service/
RUN mvn -f talent-service/pom.xml dependency:go-offline

COPY talent-service/src talent-service/src
RUN mvn -f talent-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/talent-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]