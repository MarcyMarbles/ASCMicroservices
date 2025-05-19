FROM openjdk:17-jdk-slim


RUN apt-get update \
 && apt-get install -y --no-install-recommends curl ca-certificates \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY build/libs/eureka-server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761

HEALTHCHECK --interval=30s --start-period=60s --timeout=10s --retries=10 \
  CMD curl -f http://localhost:8761/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]