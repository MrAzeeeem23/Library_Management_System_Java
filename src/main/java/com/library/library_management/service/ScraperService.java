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

    public void scrapeAndSaveBooks(List<String> urls) {
        List<String> scrapeUrls = (urls != null && !urls.isEmpty())
                ? urls
                : Arrays.asList(
                "https://www.gutenberg.org/ebooks/1342",
                "https://www.gutenberg.org/ebooks/11"
        );

        BookScraper scraper = new BookScraper(2, scrapeUrls);
        scraper.scrapeBooks();

        ConcurrentHashMap<String, Book> scrapedBooks = scraper.getBookStore();
        for (Book scrapedBook : scrapedBooks.values()) {
            System.out.println("Attempting to save - Title: " + scrapedBook.getTitle() + ", Author: " + scrapedBook.getAuthor());
            Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(
                    scrapedBook.getTitle(), scrapedBook.getAuthor()
            );

            if (existingBook.isPresent()) {
                System.out.println("Skipping duplicate book: " + scrapedBook.getTitle() + " by " + scrapedBook.getAuthor());
            } else {
                bookRepository.save(scrapedBook);
                System.out.println("Saved new book: " + scrapedBook.getTitle() + " by " + scrapedBook.getAuthor());
            }
        }
    }
}
