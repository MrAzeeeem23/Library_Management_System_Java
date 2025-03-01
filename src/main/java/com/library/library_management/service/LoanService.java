package com.library.library_management.service;

import com.library.library_management.exception.ResourceNotFoundException;
import com.library.library_management.model.Book;
import com.library.library_management.model.Loan;
import com.library.library_management.model.User;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.LoanRepository;
import com.library.library_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getLoanById(int id) {
        return loanRepository.findById(id);
    }

    public synchronized Loan createLoan(int bookId, int userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (book.borrowBook()) {
            Loan loan = new Loan(book, user);
            return loanRepository.save(loan);
        }
        throw new IllegalStateException("Book is not available for borrowing");
    }

    public void deleteLoan(int id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
        loan.getBook().returnBook();
        loanRepository.deleteById(id);
    }
}