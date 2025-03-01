package com.library.library_management.service;

import com.library.library_management.model.Book;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.scraper.BookScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ScraperService {
    @Autowired
    private BookRepository bookRepository;

    public void scrapeAndSaveBooks() {
        List<String> urls = Arrays.asList(
                "https://www.gutenberg.org/ebooks/1342",  // Pride and Prejudice
                "https://www.gutenberg.org/ebooks/84",
                "https://www.gutenberg.org/ebooks/81",
                "https://www.gutenberg.org/ebooks/61",
                "https://www.gutenberg.org/ebooks/1324"
        );

        BookScraper scraper = new BookScraper(2);  // 2 threads
        scraper.scrapeBooks(urls);

        ConcurrentHashMap<Integer, Book> scrapedBooks = scraper.getBookStore();
        for (Book scrapedBook : scrapedBooks.values()) {
            // Check if book already exists by title and author
            Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(
                    scrapedBook.getTitle(), scrapedBook.getAuthor()
            );

            if (existingBook.isPresent()) {
                System.out.println("Skipping duplicate book: " + scrapedBook.getTitle());
            } else {
                bookRepository.save(scrapedBook);
                System.out.println("Saved new book: " + scrapedBook.getTitle());
            }
        }
    }
}
