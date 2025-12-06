FROM maven:3.9.9-ibm-semeru-21-noble@sha256:d03a0137ae539579778a6eab9f4ba082976531d0d54df3edeaa3b4f50d960a57 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine-3.22@sha256:d52fed95b86b9374fcd6f289f022b7d5ef4de6fdbe1e49a59cf7551496027090

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]