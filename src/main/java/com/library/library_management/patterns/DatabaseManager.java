package com.library.library_management.patterns;

import com.library.library_management.model.Book;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    // Singleton instance
    private static final DatabaseManager instance = new DatabaseManager();
    private final Map<Integer, Book> books;  // Simulated database

    // Private constructor to prevent instantiation
    private DatabaseManager() {
        books = new HashMap<>();
    }

    // Public access to singleton instance
    public static DatabaseManager getInstance() {
        return instance;
    }

    // Add a book to the database
    public void addBook(Book book) {
        books.put(book.getId(), book);
    }

    // Get a book by ID
    public Book getBook(int id) {
        return books.get(id);
    }

    // Get all books
    public Map<Integer, Book> getAllBooks() {
        return books;
    }
}
