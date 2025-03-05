# Library Management System

A Java-based **Library Management System** built with **Spring Boot**, featuring RESTful APIs, a multi-threaded web scraper, and design patterns for managing books, users, and loans. The system uses MySQL for persistence and incorporates OOP principles, concurrency, and exception handling.

## Features

- **Book Management**: Add, update, delete, and scrape books from URLs.
- **User Management**: Create, update, and delete users (Patrons and Staff).
- **Loan Management**: Borrow and return books with thread-safe operations.
- **Multi-Threaded Web Scraper**: Fetches book data from multiple URLs concurrently using Jsoup.
- **Design Patterns**: Singleton, Factory, and Observer for scalability and flexibility.
- **Concurrency**: Uses `ReentrantLock` for thread-safe book borrowing/returning.
- **Exception Handling**: Robust error responses via Spring’s `@ControllerAdvice`.

## Tech Stack

- **Backend**: Java, Spring Boot, Spring Data JPA
- **Database**: MySQL (configurable in `application.properties`)
- **Web Scraping**: Jsoup
- **Build Tool**: Maven
- **Patterns**: Singleton, Factory, Observer
- **Concurrency**: `ExecutorService`, `ReentrantLock`, `ConcurrentHashMap`

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
    - The app runs on `http://localhost:6677`.

4. **Test the API**:
    - Use Postman or `curl` to interact with endpoints (see below).

## API Endpoints

### Base URL
```
http://localhost:8080/api
```

### Books
- **GET /books**: List all books.
- **GET /books/{id}**: Get a book by ID.
- **POST /books**: Add a book (`{"title": "Book", "author": "Author", "availableCopies": 3}`).
- **PUT /books/{id}**: Update a book.
- **DELETE /books/{id}**: Delete a book.
- **POST /books/scrape**: Scrape books from URLs (`["https://www.gutenberg.org/ebooks/84"]`).

### Users
- **GET /users**: List all users.
- **GET /users/{id}**: Get a user by ID.
- **POST /users**: Add a user (`{"name": "Charlie", "email": "charlie@example.com", "userType": "PATRON"}`).
- **PUT /users/{id}**: Update a user.
- **DELETE /users/{id}**: Delete a user.

### Loans
- **GET /loans**: List all loans.
- **GET /loans/{id}**: Get a loan by ID.
- **POST /loans?bookId={bookId}&userId={userId}**: Borrow a book.
- **DELETE /loans/{id}**: Return a book.

## Example Usage

1. **Add a Book**:
   ```bash
   curl -X POST http://localhost:6677/api/books -H "Content-Type: application/json" -d '{"title": "Moby Dick", "author": "Herman Melville", "availableCopies": 3}'
   ```

2. **Add a User**:
   ```bash
   curl -X POST http://localhost:6677/api/users -H "Content-Type: application/json" -d '{"name": "Charlie", "email": "charlie@example.com", "userType": "PATRON"}'
   ```

3. **Borrow a Book**:
   ```bash
   curl -X POST "http://localhost:6677/api/loans?bookId=1&userId=1"
   ```

4. **Scrape Books**:
   ```bash
   curl -X POST http://localhost:6677/api/books/scrape -H "Content-Type: application/json" -d '["https://www.gutenberg.org/ebooks/84"]'
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

## Future Enhancements*

- Add a frontend (e.g., React) for a UI.


