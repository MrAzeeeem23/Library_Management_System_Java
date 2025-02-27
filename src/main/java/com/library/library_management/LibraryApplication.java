package com.library.library_management;

import com.library.library_management.model.Book;
import com.library.library_management.model.Patron;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.UserRepository;
import com.library.library_management.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class LibraryApplication implements CommandLineRunner {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoanService loanService;

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Seed initial data
		Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald", 1);  // Only 1 copy
		bookRepository.save(book);

		Patron patron1 = new Patron("Alice", "alice@example.com");
		Patron patron2 = new Patron("Bob", "bob@example.com");
		userRepository.save(patron1);
		userRepository.save(patron2);

		book.registerObserver(patron1);
		book.registerObserver(patron2);

		// Simulate concurrent borrowing
		ExecutorService executor = Executors.newFixedThreadPool(2);

		executor.submit(() -> {
			try {
				loanService.createLoan(book.getId(), patron1.getId());
			} catch (Exception e) {
				System.out.println("Alice: " + e.getMessage());
			}
		});

		executor.submit(() -> {
			try {
				loanService.createLoan(book.getId(), patron2.getId());
			} catch (Exception e) {
				System.out.println("Bob: " + e.getMessage());
			}
		});

		executor.shutdown();
		executor.awaitTermination(5, TimeUnit.SECONDS);

		// Return the book to trigger observers
		loanService.deleteLoan(1);  // Assuming loan ID 1 was created
	}
}
