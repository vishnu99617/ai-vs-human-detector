# AI vs Human Text Detector Microservices

This project has been migrated to a Spring Boot Microservices architecture. It functions exactly like the original Python project but utilizes an enterprise-grade backend pattern.

## Architecture Structure

1. **service-registry (Port 8761):** Spring Cloud Eureka Server. Registers and discovers all microservices.
2. **api-gateway (Port 8080):** Spring Cloud Gateway. The central entry point that routes requests to the correct service.
3. **user-service (Port 8081):** Spring Boot + MySQL + Data JPA. Manages user registration, authentication, and contact messages.
4. **python-nlp-service (Port 5001):** Flask REST API that executes the original GPT-2 NLP logic.
5. **detection-service (Port 8082):** Spring Boot service that integrates the Python NLP logic into the Java ecosystem.
6. **frontend-service (Port 8083):** Spring Boot application containing all original static assets. Acts as a Backend-For-Frontend, rendering Thymeleaf views and proxying authentication requests.

---

## 🛠 Prerequisites
- Java 17+
- Maven
- MySQL Server (running on localhost:3306)
- Python 3.8+ (with `pip`)

## ⚙️ Database Configuration
Before running, ensure you have a MySQL server running and ready.
1. Open your MySQL client and run:
   ```sql
   CREATE DATABASE projectdb;
   ```
   *(The user credentials `root` and password `YourMyqlPaswword` are already configured in `user-service/application.yml` exactly as requested. Modify them if your local database uses different credentials.)*

## 🚀 How to Run the Application

### Method 1: Automated Startup (Recommended for Windows)
Simply double-click the `start_all.bat` file located in the root folder. 
This will automatically launch all 6 services in separate windows in the correct order!

---

### Method 2: Manual Startup
You must start the microservices in the following order:

#### 1. Start the Python NLP Service
1. Open a terminal and navigate to `Spring-Microservices/python-nlp-service`.
2. Install dependencies: `pip install -r requirements.txt`
3. Run the service: `python app.py`

### 2. Start Service Registry (Eureka)
1. Navigate to `Spring-Microservices/service-registry`.
2. Run `mvn spring-boot:run`.
3. *(Wait until it successfully starts on port 8761)*

### 3. Start API Gateway
1. Navigate to `Spring-Microservices/api-gateway`.
2. Run `mvn spring-boot:run`.

### 4. Start User Service
1. Navigate to `Spring-Microservices/user-service`.
2. Run `mvn spring-boot:run`.
*(This will automatically generate the `newuser` and `newcontact` tables in your database).*

### 5. Start Detection Service
1. Navigate to `Spring-Microservices/detection-service`.
2. Run `mvn spring-boot:run`.

### 6. Start Frontend Service
1. Navigate to `Spring-Microservices/frontend-service`.
2. Run `mvn spring-boot:run`.

## 🌐 Accessing the Application
Once all services have successfully started and registered with Eureka (you can verify them by visiting `http://localhost:8761`), you can interact with the app.

- **Main Application UI (Same as original):** `http://localhost:8080/`
- **AI Text Detector UI:** `http://localhost:8080/detector`
- **Verify API Gateway Routing:** The API Gateway listens on `8080` and routes all pages (e.g. `/`, `/detector`) to the frontend service, and API requests to their respective backend services (e.g. `/api/detection/detect` to the detection service). Accessing the application on port `8080` is required for the text detector to function.
