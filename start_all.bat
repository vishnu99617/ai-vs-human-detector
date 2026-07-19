@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17
echo Starting Microservices Architecture...

echo Starting Spring Service Registry...
start cmd /k "cd service-registry && ..\mvnw spring-boot:run"
timeout /t 10 /nobreak > NUL

echo Starting Python NLP Service...
start cmd /k "cd python-nlp-service && python app.py"

echo Starting API Gateway...
start cmd /k "cd api-gateway && ..\mvnw spring-boot:run"
timeout /t 5 /nobreak > NUL

echo Starting User Service...
start cmd /k "cd user-service && ..\mvnw spring-boot:run"
timeout /t 5 /nobreak > NUL

echo Starting Detection Service...
start cmd /k "cd detection-service && ..\mvnw spring-boot:run"
timeout /t 5 /nobreak > NUL

echo Starting Frontend Service...
start cmd /k "cd frontend-service && ..\mvnw spring-boot:run"

echo All services have been launched in separate windows!
echo Once they finish booting, open http://localhost:8080 in your browser.
pause
