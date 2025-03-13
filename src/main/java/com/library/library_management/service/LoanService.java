package com.library.library_management.service;

import com.library.library_management.exception.ResourceNotFoundException;
import com.library.library_management.model.Book;
import com.library.library_management.model.Loan;
import com.library.library_management.model.Patron;
import com.library.library_management.model.User;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.LoanRepository;
import com.library.library_management.repository.UserRepository;
import jakarta.transaction.Transactional;
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

//    @Transactional
//    public synchronized Loan createLoan(int bookId, int userId) {
//        // Validate bookId
//        Book book = bookRepository.findById(bookId)
//                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
//
//        // Validate userId
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
//
//        // Proceed with loan creation
//        if (book.borrowBook()) {
//            Loan loan = new Loan(book, user);
//            return loanRepository.save(loan);
//        }
//        throw new IllegalStateException("Book is not available for borrowing");
//    }

    @Transactional
    public synchronized Loan createLoan(int bookId, int userId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (!bookOpt.isPresent()) throw new ResourceNotFoundException("Book not found with id: " + bookId);
        Book book = bookOpt.get();

        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) throw new ResourceNotFoundException("User not found with id: " + userId);
        User user = userOpt.get();

        if (user instanceof Patron) {
            book.registerObserver((Patron) user);
            bookRepository.save(book); // Persist observer relationship
        }

        if (book.getAvailableCopies() == 0) {
            // Book already unavailable, notify but don’t create a new loan
            book.notifyObservers();
            throw new IllegalStateException("Book is already unavailable; observers notified.");
        }

        if (book.borrowBook()) {
            Loan loan = new Loan(book, user);
            Loan savedLoan = loanRepository.save(loan);
            bookRepository.save(book); // Update availableCopies
            return savedLoan;
        }
        throw new IllegalStateException("Book is not available for borrowing");
    }

    public void deleteLoan(int id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
        loan.getBook().returnBook();
        loanRepository.deleteById(id);
    }

    public void clearAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        for (Loan loan : loans) {
            loan.getBook().returnBook();
            loanRepository.delete(loan);
        }
    }

//    new Methods
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getLoanById(int id) {
        return loanRepository.findById(id);
    }

}
