package com.library.library_management.scraper;

import com.library.library_management.model.Book;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ScraperTask implements Runnable {
    private final String url;                          // Real URL to scrape
    private final ConcurrentHashMap<Integer, Book> bookStore;  // Thread-safe storage

    public ScraperTask(String url, ConcurrentHashMap<Integer, Book> bookStore) {
        this.url = url;
        this.bookStore = bookStore;
    }

    @Override
    public void run() {
        try {
            System.out.println("Scraping URL: " + url + " on thread " + Thread.currentThread().getName());
            Book scrapedBook = scrapeBookData(url);

            // Store the book in the shared ConcurrentHashMap
            if (scrapedBook != null) {
                bookStore.put(scrapedBook.getId(), scrapedBook);
                System.out.println("Added book: " + scrapedBook.getTitle() + " from " + url);
            }
        } catch (Exception e) {
            System.err.println("Error scraping " + url + ": " + e.getMessage());
        }
    }

    // Real scraping logic using Jsoup
    private Book scrapeBookData(String url) throws IOException {
        System.out.println("Connecting to " + url);
        // Connect to the URL and fetch the document
        Document doc = Jsoup.connect(url).get();
        System.out.println("Connected to " + url);

        // Extract book details (example assumes a simple HTML structure)
        Element titleElement = doc.select("h1").first();  // Title from <h1> tag
        if (titleElement != null) {
            System.out.println("Found title element: " + titleElement.text());
        } else {
            System.err.println("Title element not found for URL: " + url);
        }
        Element authorElement = doc.select("table tbody tr:contains(Author) td a").first();  // Author from <p class="author">
        if (authorElement != null) {
            System.out.println("Found author element: " + authorElement.text());
        } else {
            System.err.println("Author element not found for URL: " + url);
        }

        if (titleElement != null && authorElement != null) {
            String title = titleElement.text();
            String author = authorElement.text();

            // Generate a simple ID from the URL (in practice, use a unique identifier from the site)
            int id = Math.abs(url.hashCode());

            // Return a new Book object (5 copies by default)
            return new Book(id, title, author, 5);
        } else {
            System.err.println("Failed to find title or author element for URL: " + url);
            return null;
        }
    }
}
