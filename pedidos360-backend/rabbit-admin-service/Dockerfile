FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY rabbit-admin-service/pom.xml rabbit-admin-service/pom.xml
COPY rabbit-admin-service/src rabbit-admin-service/src
RUN mvn -q -f rabbit-admin-service/pom.xml package -DskipTests
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/rabbit-admin-service/target/rabbit-admin-service-1.0.0.jar app.jar
USER 1001
EXPOSE 8083
ENTRYPOINT ["java","-jar","app.jar"]
