package com.library.library_management.model;

import com.library.library_management.patterns.BookObserver;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PATRON")
public class Patron extends User implements BookObserver {
    public Patron() {}
    public Patron(String name, String email) {
        super(name, email);
    }

    @Override
    public int getLoanPeriod() {
        return 14;
    }

    @Override
    public void update(Book book) {
        System.out.println(getName() + " notified: Book '" + book.getTitle() + "' is now available!");
    }
}
