package com.library.library_management.model;

import com.library.library_management.patterns.BookObserver;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("PATRON")
public class Patron extends User implements BookObserver {
    @ManyToMany(mappedBy = "observers")
    @JsonBackReference
    private List<Book> observedBooks = new ArrayList<>();

    public Patron() {}
    public Patron(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public int getLoanPeriod() {
        return 14;
    }

    @Override
    public void update(Book book) {
        if (book.getAvailableCopies() == 0) {
            System.out.println(getName() + " notified: Book '" + book.getTitle() + "' is now unavailable!");
        } else {
            System.out.println(getName() + " notified: Book '" + book.getTitle() + "' is now available!");
        }
    }

    // Getter for observedBooks
    public List<Book> getObservedBooks() {
        return observedBooks;
    }
}
