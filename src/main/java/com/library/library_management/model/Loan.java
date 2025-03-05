package com.library.library_management.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int loanId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate dueDate;

    private double fine;

    public Loan() {}
    public Loan(Book book, User user) {
        this.book = book;
        this.user = user;
        this.dueDate = LocalDate.now().plusDays(user.getLoanPeriod());
        this.fine = 0.0;
    }

    // Getters and setters
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
}