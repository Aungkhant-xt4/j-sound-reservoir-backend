FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .

COPY common-library/ common-library/
RUN mvn -f common-library/pom.xml install -DskipTests

COPY auth-service/pom.xml auth-service/
RUN mvn -f auth-service/pom.xml dependency:go-offline

COPY auth-service/src auth-service/src
RUN mvn -f auth-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/auth-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]