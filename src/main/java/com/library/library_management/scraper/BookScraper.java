package com.library.library_management.scraper;

import com.library.library_management.model.Book;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BookScraper {
    private final ConcurrentHashMap<Integer, Book> bookStore;
    private final ExecutorService executorService;

    public BookScraper(int threadPoolSize) {
        this.bookStore = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void scrapeBooks(List<String> urls) {
        for (String url : urls) {
            executorService.submit(new ScraperTask(url, bookStore));
        }
        shutdownAndAwaitTermination();
    }

    private void shutdownAndAwaitTermination() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public ConcurrentHashMap<Integer, Book> getBookStore() {
        return bookStore;
    }

    public static void main(String[] args) {
        // Sample URLs (replace with real book catalog URLs)
        List<String> urls = Arrays.asList(
                "https://www.gutenberg.org/ebooks/1342",  // Pride and Prejudice
                "https://www.gutenberg.org/ebooks/84",    // Frankenstein
                "https://www.gutenberg.org/ebooks/11"      // Alice's Adventures in Wonderland
        );

        BookScraper scraper = new BookScraper(2);
        System.out.println("Starting book scraping...");
        scraper.scrapeBooks(urls);

        System.out.println("\nScraped Books:");
        scraper.getBookStore().forEach((id, book) ->
                System.out.println("ID: " + id + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor())
        );
    }
}
