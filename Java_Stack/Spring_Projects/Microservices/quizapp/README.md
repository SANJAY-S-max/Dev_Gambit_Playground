# 🎯 QuizApp Architecture Guide: Monolith & Microservices

This document provides a complete guide for the **QuizApp** projects.

---

## 🏗️ Architecture Overview

The QuizApp is implemented in two distinct architectures:

```
Java_Stack/Spring_Projects/
├── quizapp/                                    ← 🏛️ Monolithic Architecture (All-in-one Spring Boot)
└── Microservices/
    └── quizapp/                                ← 🧩 Microservices Ecosystem
        ├── service-registry/                   ← 🌐 Eureka Service Discovery Server (Port 8761)
        ├── api-gateway/                        ← 🚪 Spring Cloud API Gateway (Port 8765)
        ├── question-service/                   ← ❓ Question Management Microservice (Port 8081)
        └── quiz-service/                       ← 📝 Quiz Creation & Submission Microservice (Port 8082)
```

---

## 📊 Comparison: Monolith vs. Microservices

| Feature | Monolithic (`quizapp`) | Microservices (`question-service` + `quiz-service`) |
| :--- | :--- | :--- |
| **Port** | `8080` | `8081` (Question), `8082` (Quiz), `8761` (Eureka), `8765` (Gateway) |
| **Database** | Single database (`quizapp`) | Independent databases (`QuestionDB` & `QuizDB`) |
| **Entities** | `Quiz` has `@ManyToMany List<Question>` | `Quiz` stores `@ElementCollection List<Integer> questionIds` |
| **Inter-Service** | Direct Java method calls | Declarative REST via **Spring Cloud OpenFeign** |
| **Service Discovery**| N/A | **Netflix Eureka Server** (`service-registry`) |
| **Client Routing** | Directly to port `8080` | Centrally routed through **Spring Cloud Gateway** (`8765`) |

---

## 🛠️ Errors & Mistakes Fixed

### 1. Structure Cleanup
* **Double-nested directories eliminated:** Folders were nested like `_Inbox/quizapp/quizapp/src` — un-nested cleanly to `_Inbox/quizapp/` with `pom.xml` and `src/` at the root.
* **Empty directories removed:** Cleaned up empty duplicate directories (`SpringOauth2`, `springSecurity`).

### 2. `api-gateway`
* **Syntax Error:** Removed the invalid dangling word `lower` at the end of `application.properties`.
* **Discovery Locator:** Configured `spring.cloud.gateway.discovery.locator.lower-case-service-id=true`.
* **Eureka Client:** Configured `eureka.client.service-url.defaultZone=http://localhost:8761/eureka/` and `eureka.instance.prefer-ip-address=true`.

### 3. `question-service`
* **Port Conflict:** Added explicit `server.port=8081` (was conflicting on default `8080`).
* **Eureka Client:** Configured `eureka.client.service-url.defaultZone=http://localhost:8761/eureka/`.
* **Missing Method `findBy`:** Fixed `questionDao.findBy(id).get()` to standard `questionDao.findById(id)`.
* **Broken `getScore` Method:** Fixed broken monolithic copy-paste code referencing undefined `Quiz`, `quizDao`, `id`, and returning `QuizResult` instead of `Integer`. Implemented correct question lookup and score calculation against `questionDao`.
* **Routing & Aliases:** Added `@RequestMapping({"question", "questions"})` and accepted both `categoryName` and `catagoryName` query parameters for resilient client communication.

### 4. `quiz-service`
* **Port Conflict:** Added explicit `server.port=8082`.
* **Eureka Client:** Configured `eureka.client.service-url.defaultZone=http://localhost:8761/eureka/`.
* **Feign Mismatch:** Updated `QuizInterface` paths (`question/generate`, `question/getQuestions`, `question/getScore`) to match `question-service`.
* **`QuizDto` Typo:** Corrected field `numQuestons` to `numQuestions` and added backwards-compatible getters/setters.
* **Missing Return in `createQuiz`:** Added proper `HttpStatus.CREATED` response returning the generated quiz ID.
* **Nested `ResponseEntity` in `getQuizQuestions`:** Fixed `getQuizQuestions` to extract the payload body (`questionsResp.getBody()`) and safely handle missing quizzes.
* **Unimplemented `calculateResult`:** Fully implemented result calculation by querying `quiz-service` for question IDs, delegating scoring to `question-service` via OpenFeign, and returning a formatted `QuizResult`.
* **Controller Parameter Binding:** Updated `@PostMapping("create")` in `QuizController` to accept `@RequestBody QuizDto` while maintaining support for URL query parameters.

### 5. `quizapp` (Monolith)
* **Defensive Null Handling:** Added null checks in `calculateResult` before calling `.trim().equalsIgnoreCase()` on submitted answers to prevent `NullPointerException`.

---

## 🚀 How to Run

### Databases (MySQL)
Ensure your MySQL server is running and create the databases:
```sql
CREATE DATABASE IF NOT EXISTS quizapp;
CREATE DATABASE IF NOT EXISTS QuestionDB;
CREATE DATABASE IF NOT EXISTS QuizDB;
```

### Microservices Startup Order
1. **`service-registry`** (Port `8761`)
   ```powershell
   mvn spring-boot:run -f "Java_Stack/Spring_Projects/Microservices/quizapp/service-registry/pom.xml"
   ```
   *Verify:* Open `http://localhost:8761` in your browser to access the Eureka Dashboard.

2. **`question-service`** (Port `8081`)
   ```powershell
   mvn spring-boot:run -f "Java_Stack/Spring_Projects/Microservices/quizapp/question-service/pom.xml"
   ```

3. **`quiz-service`** (Port `8082`)
   ```powershell
   mvn spring-boot:run -f "Java_Stack/Spring_Projects/Microservices/quizapp/quiz-service/pom.xml"
   ```

4. **`api-gateway`** (Port `8765`)
   ```powershell
   mvn spring-boot:run -f "Java_Stack/Spring_Projects/Microservices/quizapp/api-gateway/pom.xml"
   ```

---

## 📡 API Endpoints Reference

### Monolithic (`quizapp` - Port 8080)
* **Add Question:** `POST http://localhost:8080/question/add`
* **All Questions:** `GET http://localhost:8080/question/allQuestions`
* **Create Quiz:** `POST http://localhost:8080/quiz/create?category=Java&numQ=5&title=JavaQuiz`
* **Get Quiz:** `GET http://localhost:8080/quiz/get/{id}`
* **Submit Answers:** `POST http://localhost:8080/quiz/submit/{id}`
  ```json
  [
    {"id": 1, "givenAnswer": "Java"},
    {"id": 2, "givenAnswer": "Inheritance"}
  ]
  ```

### Microservices (Direct or via API Gateway Port 8765)
* **Gateway Base:** `http://localhost:8765/{service-id}/{endpoint}`
  * Example: `http://localhost:8765/question-service/question/allQuestions`
  * Example: `http://localhost:8765/quiz-service/quiz/get/1`
* **Create Quiz:** `POST http://localhost:8082/quiz/create`
  ```json
  {
    "categoryName": "Java",
    "numQuestions": 5,
    "title": "Java Basics Quiz"
  }
  ```
* **Get Quiz Questions:** `GET http://localhost:8082/quiz/get/{id}`
* **Submit Answers & Score:** `POST http://localhost:8082/quiz/submit/{id}`
