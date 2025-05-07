@echo off
echo Building and deploying ASC Microservices...

echo.
echo Step 1: Building all services with Gradle...
call gradlew clean bootJar --parallel

if %ERRORLEVEL% neq 0 (
    echo Error: Failed to build services.
    exit /b %ERRORLEVEL%
)

echo.
echo Step 2: Deploying services with Docker Compose...
docker-compose down
docker-compose up -d --build

if %ERRORLEVEL% neq 0 (
    echo Error: Failed to deploy services.
    exit /b %ERRORLEVEL%
)

echo.
echo Deployment completed successfully!
echo.
echo Services:
echo - Eureka Server: http://localhost:8761
echo - API Gateway: http://localhost:8080
echo - Auth Service: via API Gateway
echo - User Service: via API Gateway
echo - Team Service: via API Gateway
echo - Main Service: via API Gateway
echo - Scrim Service: via API Gateway
echo.
echo To stop the services, run: docker-compose down