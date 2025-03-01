package com.library.library_management.model;

import com.library.library_management.patterns.BookObserver;
import com.library.library_management.patterns.BookSubject;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"title", "author"}))  // Unique constraint
public class Book implements BookSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 1, max = 100)
    private String title;

    @NotNull
    @Size(min = 1, max = 100)
    private String author;

    private int availableCopies;

    @Transient
    private List<BookObserver> observers = new ArrayList<>();

    @Transient
    private final ReentrantLock lock = new ReentrantLock();  // Lock for thread safety

    public Book() {
    }

    // Constructors
    public Book(int id, String title, String author, int availableCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.availableCopies = availableCopies;
    }

    public Book(String title, String author, int i) {
    }


    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    // Synchronized borrow method
    public boolean borrowBook() {
        lock.lock();  // Acquire the lock
        try {
            if (availableCopies > 0) {
                availableCopies--;
                System.out.println(Thread.currentThread().getName() + " borrowed " + title + ". Copies left: " + availableCopies);
                return true;
            }
            System.out.println(Thread.currentThread().getName() + " failed to borrow " + title + ". No copies left.");
            return false;
        } finally {
            lock.unlock();  // Release the lock
        }
    }

    // Synchronized return method
    public void returnBook() {
        lock.lock();
        try {
            availableCopies++;
            System.out.println(Thread.currentThread().getName() + " returned " + title + ". Copies left: " + availableCopies);
            notifyObservers(this);
        } finally {
            lock.unlock();
        }
    }

    // Observer methods
    @Override
    public void registerObserver(BookObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(BookObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Book book) {
        for (BookObserver observer : observers) {
            observer.update(book);
        }
    }

    public List<BookObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<BookObserver> observers) {
        this.observers = observers;
    }

    public ReentrantLock getLock() {
        return lock;
    }
}

