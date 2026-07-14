# Employee Management System REST API

A production-style RESTful **Employee Management System** built with **Spring Boot 3.x**.  
It provides complete CRUD operations for employee records while following REST API best practices: DTOs, Jakarta Validation, proper HTTP status codes, layered architecture, and global exception handling.

This project is designed as an academic submission demonstrating Spring Boot, Spring Web, Spring Data JPA, DTO mapping, validation, and REST API development.

---

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Language |
| Spring Boot 3.2.x | Application framework |
| Spring Web | REST controllers |
| Spring Data JPA | Persistence layer |
| Hibernate ORM | JPA provider |
| H2 Database | In-memory DB (no external install) |
| Lombok | Boilerplate reduction |
| ModelMapper | Entity ↔ DTO mapping |
| Jakarta Validation | Request validation |
| springdoc-openapi | Swagger / OpenAPI docs |
| Maven | Build tool |

---

## Project Structure

```
employee-management-system/
├── pom.xml
├── README.md
├── postman/
│   └── Employee_Management_System.postman_collection.json
└── src/main/java/com/example/employeesystem/
    ├── EmployeeManagementApplication.java
    ├── controller/
    │   └── EmployeeController.java
    ├── service/
    │   ├── EmployeeService.java
    │   └── EmployeeServiceImpl.java
    ├── repository/
    │   └── EmployeeRepository.java
    ├── entity/
    │   └── Employee.java
    ├── dto/
    │   ├── EmployeeRequestDTO.java
    │   └── EmployeeResponseDTO.java
    ├── exception/
    │   ├── ResourceNotFoundException.java
    │   ├── DuplicateEmailException.java
    │   ├── GlobalExceptionHandler.java
    │   └── ErrorResponse.java
    └── config/
        ├── ModelMapperConfig.java
        └── OpenApiConfig.java
```

---

## Prerequisites

- **JDK 17** or higher
- **Maven 3.8+** (or use the Maven Wrapper if added later)

No MySQL/PostgreSQL installation is required — the app uses an **H2 in-memory** database.

---

## Setup & Run

### 1. Clone / open the project

```bash
cd employee-management-system
```

### 2. Build the project

```bash
mvn clean install
```

### 3. Run the application

```bash
mvn spring-boot:run
```

Or run the packaged JAR:

```bash
java -jar target/employee-management-system-1.0.0.jar
```

The API starts at:

```
http://localhost:8080
```

### 4. H2 Console (optional)

- URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:employeesdb`
- Username: `sa`
- Password: *(leave empty)*

### 5. Swagger UI

- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## API Endpoints

Base URL: `/api/employees`

| Method | Endpoint | Description | Success Status |
|--------|----------|-------------|----------------|
| `POST` | `/api/employees` | Create employee | `201 Created` |
| `GET` | `/api/employees` | Get all employees | `200 OK` |
| `GET` | `/api/employees/{id}` | Get employee by ID | `200 OK` |
| `PUT` | `/api/employees/{id}` | Update employee | `200 OK` |
| `DELETE` | `/api/employees/{id}` | Delete employee | `204 No Content` |

### HTTP Status Codes

| Scenario | Status |
|---|---|
| Create success | `201 Created` |
| Fetch / Update success | `200 OK` |
| Delete success | `204 No Content` |
| Validation error | `400 Bad Request` |
| Employee not found | `404 Not Found` |
| Duplicate email | `409 Conflict` |
| Unexpected server error | `500 Internal Server Error` |

---

## Employee Fields

| Field | Type | Constraints |
|---|---|---|
| `id` | Long | Primary key, auto-generated |
| `firstName` | String | Not blank |
| `lastName` | String | Not blank |
| `email` | String | Unique, valid email |
| `department` | String | Not blank |
| `salary` | Double | Must be positive |
| `phoneNumber` | String | Exactly 10 digits |
| `joiningDate` | LocalDate | Not null (`YYYY-MM-DD`) |

---

## Example Requests (cURL)

### Create Employee

```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Harshitha",
    "lastName": "A",
    "email": "harshi@example.com",
    "department": "Engineering",
    "salary": 55000,
    "phoneNumber": "9876543210",
    "joiningDate": "2026-07-13"
  }'
```

**Sample Response (`201 Created`):**

```json
{
  "id": 1,
  "firstName": "Harshitha",
  "lastName": "A",
  "email": "harshi@example.com",
  "department": "Engineering",
  "salary": 55000.0,
  "phoneNumber": "9876543210",
  "joiningDate": "2026-07-13"
}
```

### Get All Employees

```bash
curl http://localhost:8080/api/employees
```

### Get Employee By ID

```bash
curl http://localhost:8080/api/employees/1
```

### Update Employee

```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Harshitha",
    "lastName": "A",
    "email": "harshi_updated@example.com",
    "department": "Computer Science",
    "salary": 60000,
    "phoneNumber": "9876543210",
    "joiningDate": "2026-07-13"
  }'
```

### Delete Employee

```bash
curl -X DELETE http://localhost:8080/api/employees/1
```

---

## Error Response Format

All errors return a consistent JSON body:

```json
{
  "timestamp": "2026-07-13T20:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id 5",
  "path": "/api/employees/5"
}
```

### Validation Error Example (`400`)

```json
{
  "timestamp": "2026-07-13T20:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "email: Email format must be valid; phoneNumber: Phone number must contain exactly 10 digits",
  "path": "/api/employees"
}
```

### Duplicate Email Example (`409`)

```json
{
  "timestamp": "2026-07-13T20:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Employee already exists with email: harshi@example.com",
  "path": "/api/employees"
}
```

---

## Architecture & Design Notes

- **Layered architecture**: Controller → Service → Repository → Entity
- **DTOs**: Entities are never exposed directly; `EmployeeRequestDTO` (validated) and `EmployeeResponseDTO` are used
- **Business rules**:
  - Email must be unique on create
  - Update preserves existing ID; email uniqueness is checked against other employees
  - Missing employees throw `ResourceNotFoundException` → `404`
- **Global exception handling** via `@RestControllerAdvice`
- **OpenAPI** annotations on the controller for Swagger UI

---

## Postman Collection

Import the collection from:

```
postman/Employee_Management_System.postman_collection.json
```

It includes ready-made requests for Create, Get All, Get By ID, Update, and Delete.

---

## Configuration Summary

`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:employeesdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---

## License

This project is provided for academic / educational use.
