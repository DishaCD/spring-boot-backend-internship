# Spring Boot Backend Developer Internship Tasks

This repository contains the backend systems developed as part of the **Backend Developer Internship**. Each task is designed to showcase real-world development practices using **Java, Spring Boot, Spring Data JPA, and Relational Databases (PostgreSQL & H2)**.

---

## 📁 Repository Structure

*   `task 1/`: **Employee Management System**
    *   Exposes endpoints to perform full CRUD operations on employees.
    *   Features validation for input requests (e.g., unique email constraint validation).
*   `task 2/`: **Library Management System**
    *   Advanced relational database setup with PostgreSQL.
    *   Automatic schema management and data seeding using versioned **Flyway Migrations** (`V1` to `V6`).
    *   Exposes APIs for borrowing and returning books with copy availability constraint checking.
    *   Includes complex JPQL Join & Aggregation projections.
*   `task 3/`: **Food Ordering System**
    *   Features robust validation for menus and order requests.
    *   Implements inventory stock tracking (prevents placing orders exceeding available quantities).
    *   Fully integrated with structured logging via **SLF4J / Logback**.
    *   100% automated test coverage with MockMvc, JUnit, and Mockito.

---

## 🛠️ Technology Stack

*   **Language**: Java 17 / Java 23
*   **Framework**: Spring Boot (v3.x)
*   **Data Access**: Spring Data JPA / Hibernate ORM
*   **Databases**: PostgreSQL (Production) & H2 (In-Memory for Testing)
*   **Migrations**: Flyway Migration Tool
*   **Testing**: JUnit 5, Mockito, MockMvc
*   **Documentation**: Springdoc OpenAPI (Swagger UI)
*   **Logging**: SLF4J / Logback

---

## 🚀 Getting Started

### Prerequisites

Ensure you have the following installed on your local environment:
1.  **JDK 17 or higher** (`java -version`)
2.  **Apache Maven** (`mvn -version`)
3.  **PostgreSQL** (running on `localhost:5432` for Task 2)

### Running a Project Locally

1.  Navigate to the target project directory (e.g., Task 3):
    ```bash
    cd "task 3/task 3"
    ```
2.  Build the project and run tests:
    ```bash
    mvn clean test
    ```
3.  Start the application:
    ```bash
    mvn spring-boot:run
    ```

---

## 🧪 Verification & Test Runs

All projects are fully backed by integration and unit tests verifying:
*   Context startup
*   Flyway schema migrations
*   Custom repository projection queries
*   REST Controller end-to-end flows
*   Validation triggers (such as duplicate entries and invalid counts)

All tests execute successfully with **0 failures and 0 errors**.
