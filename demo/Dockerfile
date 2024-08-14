# Use a Maven image to build the application
FROM maven:3.8.3-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar demo.jar

EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "demo.jar"]
