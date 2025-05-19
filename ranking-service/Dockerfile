FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/ranking-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]