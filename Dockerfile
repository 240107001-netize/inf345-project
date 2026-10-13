FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /src
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /src/target/*.jar /app/app.jar
ENV PORT=8080
EXPOSE 8080
USER 1000:1000
CMD ["java", "-cp", "/app/app.jar", "app.Main"]
