# Library Management System

A Java-based **Library Management System** built with **Spring Boot**, featuring RESTful APIs for managing books, users, and loans. The system includes a multi-threaded web scraper, secure admin authentication, user password encryption with BCrypt, and automated overdue loan charging. It uses MySQL for persistence and incorporates OOP principles, design patterns, concurrency, and exception handling.

## Features

- **Book Management**: Add, update, delete, and scrape books from URLs (admin-only for scraping).
- **User Management**: Create, update, and delete users with encrypted passwords (admin-only for creation/update).
- **Loan Management**: Borrow and return books with thread-safe operations (admin-only for clearing loans).
- **Multi-Threaded Web Scraper**: Fetches book data from multiple URLs concurrently using Jsoup (admin-only).
- **Admin Authentication**: Secure admin-only routes using Spring Security with HTTP Basic Auth.
- **User Password Encryption**: Stores user passwords securely using BCrypt.
- **Overdue Loan Charging**: Automatically charges fines for overdue loans via a scheduled task.
- **Design Patterns**: Singleton, Factory, and Observer for scalability and flexibility.
- **Concurrency**: Uses `ReentrantLock` for thread-safe book borrowing/returning and `ExecutorService` for web scraping.
- **Exception Handling**: Robust error responses via Spring’s `@ControllerAdvice`.

## Tech Stack

- **Backend**: Java, Spring Boot, Spring Data JPA, Spring Security
- **Database**: MySQL (configurable in `application.properties`)
- **Web Scraping**: Jsoup
- **Password Encryption**: BCrypt (via Spring Security)
- **Build Tool**: Maven
- **Patterns**: Singleton, Factory, Observer
- **Concurrency**: `ExecutorService`, `ReentrantLock`, `ConcurrentHashMap`
- **Scheduling**: Spring `@Scheduled` for overdue loan checks

## Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Node.js (optional, for frontend development)

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd library_management
   ```

2. **Configure MySQL**:
   - Create a database:
     ```sql
     CREATE DATABASE librarydb;
     ```
   - Update `src/main/resources/application.properties`:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/librarydb
     spring.datasource.username=root
     spring.datasource.password=your_password
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Build and Run**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   - The app runs on `http://localhost:6677` (adjust port in `application.properties` if needed).

4. **Admin Credentials**:
   - Username: `admin`
   - Password: `admin123`
   - Use these credentials for admin-only endpoints via HTTP Basic Auth.

## API Endpoints

### Base URL
```
http://localhost:6677/api
```

### Authentication
- Admin-only endpoints require HTTP Basic Auth with credentials `admin:admin123`.
- Example: `curl -u admin:admin123 http://localhost:6677/api/users`

### Books
- **GET /books**: List all books (public).
- **GET /books/{id}**: Get a book by ID (public).
- **POST /books**: Add a book (`{"title": "Book", "author": "Author", "availableCopies": 3}`) (public).
- **PUT /books/{id}**: Update a book (public).
- **DELETE /books/{id}**: Delete a book (public).
- **POST /books/scrape**: Scrape books from URLs (`["https://www.gutenberg.org/ebooks/84"]`) (admin-only).

### Users
- **GET /users**: List all users (public).
- **GET /users/{id}**: Get a user by ID (public).
- **POST /users**: Add a user (`{"name": "Charlie", "email": "charlie@example.com", "userType": "PATRON", "password": "mypassword123"}`) (admin-only).
- **PUT /users/{id}**: Update a user (admin-only).
- **DELETE /users/{id}**: Delete a user (public).

### Loans
- **GET /loans**: List all loans (public).
- **GET /loans/{id}**: Get a loan by ID (public).
- **POST /loans?bookId={bookId}&userId={userId}**: Borrow a book (admin-only).
- **DELETE /loans/{id}**: Return a book (admin-only).
- **DELETE /loans/clear**: Clear all loans (admin-only).
- **POST /loans/trigger-overdue**: Manually trigger overdue loan check (admin-only).

## Key Features Explained

### Admin Authentication
- Implemented using Spring Security with HTTP Basic Auth.
- Admin-only routes: `/api/books/scrape`, `/api/users` (POST/PUT), `/api/loans/clear`, `/api/loans/trigger-overdue`, `/api/loans/{id}` (DELETE), `/api/loans?bookId={bookId}&userId={userId}` (POST).
- Credentials: Username `admin`, Password `admin123`.

### User Password Encryption
- User passwords are stored securely using BCrypt hashing.
- Passwords are provided in plaintext via `UserDTO` and hashed before saving to the database.
- Example: `{"password": "mypassword123"}` → Stored as `$2a$10$...`.

### Overdue Loan Charging
- Automatically charges fines for overdue loans via a scheduled task.
- Runs daily at midnight (`@Scheduled(cron = "0 0 0 * * ?")`).
- Fine calculation: $1 per day overdue.
- Fines are stored in the `Loan` entity and exposed in `GET /api/loans`.

### Multi-Threaded Web Scraper
- Fetches book data from multiple URLs concurrently using `ExecutorService` and Jsoup.
- Stores results in a `ConcurrentHashMap` for thread safety.
- Triggered via `POST /api/books/scrape` (admin-only).

## Example Usage

1. **Add a Book** (public):
   ```bash
   curl -X POST http://localhost:6677/api/books \
        -H "Content-Type: application/json" \
        -d '{"title": "Moby Dick", "author": "Herman Melville", "availableCopies": 3}'
   ```

2. **Register a User** (public):
   ```bash
   curl -X POST http://localhost:6677/api/auth/register \
        -u admin:admin123 \
        -H "Content-Type: application/json" \
        -d '{"name": "Charlie", "email": "charlie@example.com", "userType": "PATRON", "password": "mypassword123"}'
   ```

3. **Login a User** (public):
   ```bash
   curl -X POST http://localhost:6677/api/auth/login \
        -u charlie@example.com:mypassword123 \
        -H "Content-Type: application/json"
   ```
4. **Update User Data** (public):
   ```bash
   curl -X PUT http://localhost:6677/api/users/me \
        -u charlie@example.com:mypassword123 \
        -H "Content-Type: application/json" \
        -d '{"name": "Alice", "email": "alice@email.com"}'
   ```

5. **Borrow a Book** (admin-only):
   ```bash
   curl -X POST "http://localhost:6677/api/loans?bookId=1&userId=1" \
        -u admin:admin123 \
        -H "Content-Type: application/json"
   ```

6. **Book not available Observer Notify** (admin-only): 
   ```bash
   curl -X POST "http://localhost:6677/api/loans?bookId=1&userId=1" \
        -u admin:admin123 \
        -H "Content-Type: application/json"
   ```
   **If book is not available then observer will log this 👇**
   ```declarative
   Alice notified: Book 'Test Book' is now unavailable!
   ```

7. **Scrape Books** (admin-only):
   ```bash
   curl -X POST http://localhost:6677/api/books/scrape \
        -u admin:admin123 \
        -H "Content-Type: application/json" \
        -d '["https://www.gutenberg.org/ebooks/84"]'
   ```

8. **Trigger Overdue Loan Check** (admin-only):
   ```bash
   curl -X POST http://localhost:6677/api/loans/trigger-overdue \
        -u admin:admin123 \
        -H "Content-Type: application/json"
   ```

## Design Patterns

- **Singleton**: `DatabaseManager` (placeholder for centralized book storage, not currently used with MySQL).
- **Factory**: `UserFactory` creates `Patron` or `Staff` objects dynamically.
- **Observer**: `Book` notifies `Patron` observers when returned.

## Concurrency

- **Multi-Threaded Scraper**: Uses `ExecutorService` and `ConcurrentHashMap` to scrape URLs concurrently.
- **Locks**: `ReentrantLock` in `Book` ensures thread-safe borrowing/returning.

## OOP Concepts

- **Encapsulation**: Private fields with getters/setters (e.g., `Book.title`, `User.name`).
- **Inheritance**: `Patron` and `Staff` extend `User`.
- **Polymorphism**: `getLoanPeriod()` varies by user type.
- **Abstraction**: Abstract `User` and interfaces (`BookObserver`, `BookSubject`).

## Exception Handling

- Custom exceptions (e.g., `ResourceNotFoundException`) and a global handler (`GlobalExceptionHandler`) manage errors with HTTP status codes (400, 404, 500).
- Handles database constraint violations (e.g., invalid `userId` or `bookId` when creating loans).


