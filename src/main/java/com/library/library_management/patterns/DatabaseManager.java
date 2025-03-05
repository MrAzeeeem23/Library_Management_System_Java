package com.library.library_management.patterns;

import com.library.library_management.model.Book;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    // Singleton instance pattern
    private static final DatabaseManager instance = new DatabaseManager();
    private final Map<Integer, Book> books;  // Simulated database

    // private constructor to prevent instantiation
    private DatabaseManager() {
        books = new HashMap<>();
    }

    // public access to singleton instance
    public static DatabaseManager getInstance() {
        return instance;
    }

    // add a book to the database
    public void addBook(Book book) {
        books.put(book.getId(), book);
    }

    // get a book by ID
    public Book getBook(int id) {
        return books.get(id);
    }

    // get all books
    public Map<Integer, Book> getAllBooks() {
        return books;
    }
}

// not in use
