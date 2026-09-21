FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY orders-service/pom.xml orders-service/pom.xml
COPY orders-service/src orders-service/src
RUN mvn -q -f orders-service/pom.xml package -DskipTests
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/orders-service/target/orders-service-1.0.0.jar app.jar
USER 1001
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
