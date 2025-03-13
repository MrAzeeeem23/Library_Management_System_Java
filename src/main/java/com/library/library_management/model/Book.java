package com.library.library_management.model;

import com.library.library_management.patterns.BookObserver;
import com.library.library_management.patterns.BookSubject;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Entity
public class Book implements BookSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String author;
    private int availableCopies;

    @ManyToMany
    @JoinTable(
            name = "book_patron_observers",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "patron_id")
    )
    @JsonManagedReference
    private List<Patron> observers = new ArrayList<>();

    @Transient
    private final ReentrantLock lock = new ReentrantLock();

    public Book() {}
    public Book(String title, String author, int availableCopies) {
        this.title = title;
        this.author = author;
        this.availableCopies = availableCopies;
    }

    public boolean borrowBook() {
        lock.lock();
        try {
            if (availableCopies > 0) {
                availableCopies--;
                if (availableCopies == 0) {
                    notifyObservers();
                }
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void returnBook() {
        lock.lock();
        try {
            availableCopies++;
            notifyObservers();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void registerObserver(BookObserver observer) {
        if (observer instanceof Patron && !observers.contains(observer)) {
            observers.add((Patron) observer);
        }
    }

    @Override
    public void removeObserver(BookObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (BookObserver observer : observers) {
            observer.update(this); // Pass current Book instance
        }
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
    public List<Patron> getObservers() { return observers; }
}