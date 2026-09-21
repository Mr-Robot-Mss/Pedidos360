FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY bff-service/pom.xml bff-service/pom.xml
COPY bff-service/src bff-service/src
RUN mvn -q -f bff-service/pom.xml package -DskipTests
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/bff-service/target/bff-service-1.0.0.jar app.jar
USER 1001
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
