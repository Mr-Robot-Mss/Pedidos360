FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY catalog-service/pom.xml catalog-service/pom.xml
COPY catalog-service/src catalog-service/src
RUN mvn -q -f catalog-service/pom.xml package -DskipTests
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/catalog-service/target/catalog-service-1.0.0.jar app.jar
USER 1001
EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"]
