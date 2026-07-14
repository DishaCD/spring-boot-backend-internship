# Library Management System Backend with PostgreSQL, Spring Data JPA, and Flyway Migrations

A production-style, relational database-backed **Library Management System** built with **Spring Boot 3.3.1**, **Java 17**, **PostgreSQL**, **Spring Data JPA**, and **Flyway Database Migrations**. 

The application is engineered so that on startup, it connects to PostgreSQL, automatically executes versioned Flyway migrations in strict order, creates all database tables with real-world relational constraints, seeds initial sample data (authors, categories, and books), validates schema integrity (`ddl-auto=validate`), and exposes interactive REST APIs via Swagger/OpenAPI UI—all zero-touch without requiring manual SQL script execution.

---

## 🌟 Project Highlights & Features

- **Automated Database Versioning with Flyway**: Strict schema ownership (`spring.jpa.hibernate.ddl-auto=validate`). Tables and seed data are created incrementally via `V1` through `V6` SQL migrations.
- **Relational Integrity & Validation**:
  - **Foreign Key Relationships**: `Author (1) -------- (N) Book` and `Category (1) -------- (N) Book` with `ON DELETE RESTRICT` protection.
  - **Copy Validation Constraint**: Database-level check `CONSTRAINT chk_books_copy_validation CHECK (available_copies <= total_copies AND available_copies >= 0 AND total_copies >= 0)`.
  - **Unique Constraints**: Unique `isbn` on books and unique `name` on categories.
- **Rich Custom Repository Queries**:
  - Title & Author case-insensitive search (`findByTitleContainingIgnoreCase`, `findByAuthorNameContainingIgnoreCase`)
  - Publication year filtering (`findByPublicationYear`)
  - Category and availability checks (`findByCategoryName`, `findByAvailableCopiesGreaterThan`)
  - JPQL Join & Aggregation Projections (`findBooksWithCategoryName`, `findBooksWithAuthorName`, `countBooksPerCategory`)
- **Full REST API & Interactive Swagger UI**: Complete CRUD operations, borrow/return workflows, and dedicated endpoints for all required SQL queries.
- **Comprehensive Automated Test Suite**: Built-in integration tests verifying context startup, Flyway history execution, JPA validation, custom repository queries, and REST endpoints.

---

## 🛠️ Technology Stack

| Component | Technology | Version / Description |
| :--- | :--- | :--- |
| **Language** | Java | 17 |
| **Framework** | Spring Boot | 3.3.1 |
| **ORM / Data Access** | Spring Data JPA / Hibernate ORM | 6.5.x |
| **Database** | PostgreSQL | 16+ / 17 |
| **Migration Tool** | Flyway | Core & PostgreSQL Module (`10.x`) |
| **Build Tool** | Apache Maven | 3.9+ |
| **Utilities** | Lombok | Boilerplate reduction (@Data, @Builder) |
| **API Documentation** | Springdoc OpenAPI UI | `2.5.0` (Swagger UI at `/swagger-ui.html`) |
| **Testing** | JUnit 5 / Spring Boot Test / MockMvc | Comprehensive integration testing suite |

---

## 📁 Project Structure

```
src/main/java/com/example/librarymanagement
├── LibraryManagementApplication.java          # Main Spring Boot entry point
├── config
│   ├── DatabaseConfig.java                    # JPA, EntityScan & Transaction management
│   └── OpenApiConfig.java                     # Swagger/OpenAPI configuration
├── controller
│   ├── AuthorController.java                  # REST endpoints for Authors CRUD
│   ├── BookController.java                    # REST endpoints for Books CRUD, borrowing, & search
│   ├── CategoryController.java                # REST endpoints for Categories CRUD
│   └── QueryController.java                   # Endpoints demonstrating required PRD join/aggregate queries
├── dto
│   ├── AuthorDTO.java / AuthorSummaryDTO.java # Author data transfer objects
│   ├── BookCreateDTO.java / BookDTO.java      # Book input/output models
│   ├── CategoryDTO.java                       # Category data transfer objects
│   └── QueryResponses.java                    # Projections for join and aggregate queries
├── entity
│   ├── Author.java                            # Author entity (@Table authors)
│   ├── Book.java                              # Book entity (@Table books with check constraint)
│   └── Category.java                          # Category entity (@Table categories)
├── exception
│   ├── ErrorResponse.java                     # Standardized JSON error structure
│   ├── GlobalExceptionHandler.java            # RestControllerAdvice handling errors & validation
│   └── [ResourceNotFound, Duplicate, InvalidCopyOperation]Exception.java
├── repository
│   ├── AuthorRepository.java                  # JPA repository for authors
│   ├── BookRepository.java                    # JPA repository with custom queries and projections
│   └── CategoryRepository.java                # JPA repository for categories
└── service
    ├── AuthorService.java                     # Business logic for authors
    ├── BookService.java                       # Business logic for books (copy validation, search)
    └── CategoryService.java                   # Business logic for categories

src/main/resources
├── application.properties                     # Database, JPA, Flyway, and Swagger configuration
└── db/migration
    ├── V1__create_authors_table.sql           # Schema creation: Authors table
    ├── V2__create_categories_table.sql        # Schema creation: Categories table (unique name)
    ├── V3__create_books_table.sql             # Schema creation: Books table (FKs, check constraints)
    ├── V4__insert_sample_authors.sql          # Seed data: 8 renowned authors
    ├── V5__insert_sample_categories.sql       # Seed data: 7 core library categories
    └── V6__insert_sample_books.sql            # Seed data: 12 sample books with copies

src/test/java/com/example/librarymanagement
├── LibraryManagementApplicationTests.java     # Context verification & Flyway migration checks
├── RepositoryIntegrationTests.java            # Custom JPA repository method tests
└── ControllerIntegrationTests.java            # REST API integration tests via MockMvc
```

---

## 🗄️ Database Schema & Entity Relationships

### Relational Schema Diagram

```
+-----------------------------------+       +-----------------------------------+
|              AUTHORS              |       |            CATEGORIES             |
+-----------------------------------+       +-----------------------------------+
| id: BIGSERIAL (PK)                |       | id: BIGSERIAL (PK)                |
| name: VARCHAR(150) NOT NULL       |       | name: VARCHAR(100) NOT NULL (UQ)  |
| nationality: VARCHAR(100)         |       | description: TEXT                 |
| birth_year: INTEGER               |       +-----------------+-----------------+
+-----------------+-----------------+                         |
                  |                                           |
                  | @OneToMany(mappedBy="author")             | @OneToMany(mappedBy="category")
                  |                                           |
                  |        +----------------------------------+
                  |        |
                  v        v
+-----------------+-----------------------------------------------------------------+
|                                       BOOKS                                       |
+-----------------------------------------------------------------------------------+
| id: BIGSERIAL (PK)                                                                |
| title: VARCHAR(255) NOT NULL                                                      |
| isbn: VARCHAR(20) NOT NULL (UNIQUE)                                               |
| publication_year: INTEGER NOT NULL                                                |
| available_copies: INTEGER NOT NULL DEFAULT 0                                      |
| total_copies: INTEGER NOT NULL                                                    |
| author_id: BIGINT NOT NULL (FK -> authors.id ON DELETE RESTRICT)                  |
| category_id: BIGINT NOT NULL (FK -> categories.id ON DELETE RESTRICT)             |
| created_at: TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP                          |
| updated_at: TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP                          |
+-----------------------------------------------------------------------------------+
| CONSTRAINT chk_books_copy_validation CHECK (available_copies <= total_copies      |
|                                             AND available_copies >= 0             |
|                                             AND total_copies >= 0)                |
+-----------------------------------------------------------------------------------+
```

---

## 🚀 Setup & Installation Guide

### Prerequisites
- **Java 17 JDK** (`java -version`)
- **Maven 3.9+** (`mvn -version`)
- **PostgreSQL Server running on localhost:5432**

### Step 1: Create Database in PostgreSQL
Connect to your PostgreSQL server (`psql -U postgres`) and run:
```sql
CREATE DATABASE library_db;
```

### Step 2: Configure Application Properties (if needed)
By default, the application connects using credentials specified in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/library_db
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

### Step 3: Run the Application
In the project root directory, execute:
```bash
mvn spring-boot:run
```

During startup, look for the Flyway logs indicating automatic schema and seed data deployment:
```text
INFO --- org.flywaydb.core.FlywayExecutor : Database: jdbc:postgresql://localhost:5432/library_db (PostgreSQL 17.10)
INFO --- o.f.core.internal.command.DbMigrate : Migrating schema "public" to version "1 - create authors table"
INFO --- o.f.core.internal.command.DbMigrate : Migrating schema "public" to version "2 - create categories table"
INFO --- o.f.core.internal.command.DbMigrate : Migrating schema "public" to version "3 - create books table"
INFO --- o.f.core.internal.command.DbMigrate : Migrating schema "public" to version "4 - insert sample authors"
INFO --- o.f.core.internal.command.DbMigrate : Migrating schema "public" to version "5 - insert sample categories"
INFO --- o.f.core.internal.command.DbMigrate : Migrating schema "public" to version "6 - insert sample books"
INFO --- o.f.core.internal.command.DbMigrate : Successfully applied 6 migrations to schema "public", now at version v6
INFO --- org.hibernate.Version : Hibernate ORM core version 6.5.2.Final
INFO --- c.e.l.LibraryManagementApplication : Started LibraryManagementApplication in 5.6 seconds
```

### Step 4: Verify Migrations via PostgreSQL
Run the following query to inspect Flyway schema history:
```sql
SELECT installed_rank, version, description, type, script, success 
FROM flyway_schema_history;
```

**Expected Result:**
| installed_rank | version | description | type | script | success |
| :---: | :---: | :--- | :--- | :--- | :---: |
| 1 | 1 | create authors table | SQL | V1__create_authors_table.sql | `t` |
| 2 | 2 | create categories table | SQL | V2__create_categories_table.sql | `t` |
| 3 | 3 | create books table | SQL | V3__create_books_table.sql | `t` |
| 4 | 4 | insert sample authors | SQL | V4__insert_sample_authors.sql | `t` |
| 5 | 5 | insert sample categories | SQL | V5__insert_sample_categories.sql | `t` |
| 6 | 6 | insert sample books | SQL | V6__insert_sample_books.sql | `t` |

---

## 📊 SQL Queries for Submission & Verification

The following SQL queries demonstrate join relationships, aggregations, and filtering. You can execute them directly in PostgreSQL (`psql -d library_db`) or via the interactive REST API `/api/queries/...`.

### 1. Fetch All Books
```sql
SELECT id, title, isbn, publication_year, available_copies, total_copies 
FROM books;
```
**Sample Output:**
| id | title | isbn | publication_year | available_copies | total_copies |
| -: | :--- | :--- | :---: | :---: | :---: |
| 1 | Harry Potter and the Philosopher's Stone | 9780747532743 | 1997 | 10 | 12 |
| 2 | The Alchemist | 9780062315007 | 1988 | 8 | 10 |
| 3 | Atomic Habits | 9780735211292 | 2018 | 15 | 20 |
| 4 | Rich Dad Poor Dad | 9781612680194 | 1997 | 5 | 8 |
| 5 | 1984 | 9780451524935 | 1949 | 4 | 10 |
| 6 | Sapiens: A Brief History of Humankind | 9780062316097 | 2011 | 7 | 9 |
| 7 | Angels and Demons | 9780671027360 | 2000 | 6 | 6 |
| 8 | Animal Farm | 9780451526342 | 1945 | 3 | 5 |
| 9 | Harry Potter and the Chamber of Secrets | 9780747538493 | 1998 | 9 | 11 |
| 10 | The Da Vinci Code | 9780385504205 | 2003 | 12 | 15 |
| 11 | Homo Deus: A Brief History of Tomorrow | 9780062464316 | 2015 | 6 | 8 |
| 12 | A Brief History of Time | 9780553380163 | 1988 | 5 | 7 |

### 2. Fetch All Authors
```sql
SELECT id, name, nationality, birth_year 
FROM authors;
```
**Sample Output:**
| id | name | nationality | birth_year |
| -: | :--- | :--- | :---: |
| 1 | J.K. Rowling | British | 1965 |
| 2 | Paulo Coelho | Brazilian | 1947 |
| 3 | George Orwell | British | 1903 |
| 4 | James Clear | American | 1986 |
| 5 | Robert Kiyosaki | American | 1947 |
| 6 | Dan Brown | American | 1964 |
| 7 | Yuval Noah Harari | Israeli | 1976 |
| 8 | Stephen Hawking | British | 1942 |

### 3. Books by Category (Join Query)
```sql
SELECT b.title, c.name AS category_name
FROM books b
JOIN categories c ON b.category_id = c.id;
```
**Sample Output:**
| title | category_name |
| :--- | :--- |
| Animal Farm | Fiction |
| 1984 | Fiction |
| The Alchemist | Fiction |
| Harry Potter and the Chamber of Secrets | Fantasy |
| Harry Potter and the Philosopher's Stone | Fantasy |
| Atomic Habits | Self Help |
| Rich Dad Poor Dad | Finance |
| Homo Deus: A Brief History of Tomorrow | History |
| Sapiens: A Brief History of Humankind | History |
| The Da Vinci Code | Mystery |
| Angels and Demons | Mystery |
| A Brief History of Time | Science |

### 4. Books by Author (Join Query)
```sql
SELECT b.title, a.name AS author_name
FROM books b
JOIN authors a ON b.author_id = a.id;
```
**Sample Output:**
| title | author_name |
| :--- | :--- |
| Harry Potter and the Philosopher's Stone | J.K. Rowling |
| The Alchemist | Paulo Coelho |
| Atomic Habits | James Clear |
| Rich Dad Poor Dad | Robert Kiyosaki |
| 1984 | George Orwell |
| Sapiens: A Brief History of Humankind | Yuval Noah Harari |
| Angels and Demons | Dan Brown |
| Animal Farm | George Orwell |
| Harry Potter and the Chamber of Secrets | J.K. Rowling |
| The Da Vinci Code | Dan Brown |
| Homo Deus: A Brief History of Tomorrow | Yuval Noah Harari |
| A Brief History of Time | Stephen Hawking |

### 5. Number of Books per Category (Aggregate Query)
```sql
SELECT c.name AS category_name, COUNT(*) AS book_count
FROM books b
JOIN categories c ON b.category_id = c.id
GROUP BY c.name;
```
**Sample Output:**
| category_name | book_count |
| :--- | :---: |
| Finance | 1 |
| History | 2 |
| Fantasy | 2 |
| Mystery | 2 |
| Self Help | 1 |
| Science | 1 |
| Fiction | 3 |

### 6. Available Books Only (Filtering Query)
```sql
SELECT id, title, available_copies, total_copies
FROM books
WHERE available_copies > 0;
```
**Sample Output:**
Returns all 12 sample books that have `available_copies > 0`. If a book's available copies are decremented to `0` using the `/api/books/{id}/borrow` API, it is automatically filtered out from this query.

---

## 🌐 API Endpoints & Swagger UI

Once the application is running (`mvn spring-boot:run`), interactive API documentation is available at:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI v3 JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Core REST API Reference

#### Books Management (`/api/books`)
- `GET /api/books` - Retrieve all books
- `GET /api/books/{id}` - Retrieve a specific book by ID
- `POST /api/books` - Create a new book (`check(available <= total)`)
- `PUT /api/books/{id}` - Update a book's details or copies
- `DELETE /api/books/{id}` - Delete a book
- `GET /api/books/search/title?title=harry` - Search books by title
- `GET /api/books/search/year?year=1997` - Search books by publication year
- `GET /api/books/search/author?author=orwell` - Search books by author name
- `GET /api/books/search/category?category=Mystery` - Search books by category name
- `GET /api/books/available` - Fetch all books with `available_copies > 0`
- `POST /api/books/{id}/borrow` - Borrow a book (`available_copies - 1`)
- `POST /api/books/{id}/return` - Return a borrowed book (`available_copies + 1`)

#### Authors Management (`/api/authors`)
- `GET /api/authors` - Retrieve all authors & books summaries
- `GET /api/authors/{id}` - Retrieve author details
- `POST /api/authors` - Add a new author
- `PUT /api/authors/{id}` - Update author details
- `DELETE /api/authors/{id}` - Delete author (`RESTRICT` if books exist)

#### Categories Management (`/api/categories`)
- `GET /api/categories` - Retrieve all categories
- `GET /api/categories/{id}` - Retrieve specific category
- `POST /api/categories` - Create new category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category (`RESTRICT` if books exist)

#### Sample SQL Queries Demonstration (`/api/queries`)
- `GET /api/queries/books-by-category` - Returns JSON representation of Book + Category join
- `GET /api/queries/books-by-author` - Returns JSON representation of Book + Author join
- `GET /api/queries/books-count-per-category` - Returns aggregated count per category

---

## 🧪 Testing & Verification

Run the full automated test suite using Maven:
```bash
mvn clean test
```

### What the Test Suite Verifies (`RepositoryIntegrationTests`, `ControllerIntegrationTests`, `LibraryManagementApplicationTests`):
1. **Flyway Migration Order & Success**: Confirms all 6 migrations execute without error on a clean schema.
2. **JPA Schema Validation**: Verifies `ddl-auto=validate` confirms the schema perfectly aligns with JPA `@Entity` definitions.
3. **Seed Data Integrity**: Verifies exact author, category, and book counts and attribute values.
4. **Custom Repository Methods**: Exercises `findByTitleContainingIgnoreCase`, `findByPublicationYear`, `findByAuthorNameContainingIgnoreCase`, `findByCategoryName`, and `findByAvailableCopiesGreaterThan`.
5. **JPQL Projections & Counts**: Verifies join query projections (`findBooksWithCategoryName`, `findBooksWithAuthorName`, `countBooksPerCategory`).
6. **REST API Functionality**: Exercises endpoints using `MockMvc` to guarantee HTTP 200/201 and JSON validity.

---

## ✅ Expected Outcome Checklist

When running `mvn spring-boot:run` or `mvn test`, the system accomplishes the following expected outcomes:

- ✅ **Connect to PostgreSQL**: Smooth connection via `jdbc:postgresql://localhost:5432/library_db`
- ✅ **Execute Flyway Migrations Automatically**: Sequences `V1` -> `V2` -> `V3` -> `V4` -> `V5` -> `V6`
- ✅ **Create All Tables**: Creates `authors`, `categories`, and `books` with exact data types, primary keys, foreign keys (`author_id`, `category_id`), unique constraints (`isbn`, `name`), and copy check constraints (`chk_books_copy_validation`)
- ✅ **Insert Sample Data**: Seeds 8 authors, 7 categories, and 12 books automatically on first startup
- ✅ **Create `flyway_schema_history` Table**: Automatically records migration checksums and execution timing
- ✅ **Validate Schema Consistency**: Hibernate startup validates schema consistency (`validate`) without altering tables
- ✅ **Start Without Errors**: Application initializes within ~5 seconds and serves Swagger UI at `http://localhost:8080/swagger-ui.html`

---

## 📜 License
Licensed under the Apache License, Version 2.0.
