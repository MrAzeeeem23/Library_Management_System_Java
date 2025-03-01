package com.library.library_management.scraper;

import com.library.library_management.model.Book;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ScraperTask implements Runnable {
    private final String url;
    private final ConcurrentHashMap<String, Book> bookStore;

    public ScraperTask(String url, ConcurrentHashMap<String, Book> bookStore) {
        this.url = url;
        this.bookStore = bookStore;
    }

    @Override
    public void run() {
        try {
            System.out.println("Scraping URL: " + url + " on thread " + Thread.currentThread().getName());
            Book scrapedBook = scrapeBookData(url);

            if (scrapedBook != null) {
                System.out.println("Book created - Title: " + scrapedBook.getTitle() + ", Author: " + scrapedBook.getAuthor());
                bookStore.put(url, scrapedBook);
                System.out.println("Successfully added book: " + scrapedBook.getTitle() + " by " + scrapedBook.getAuthor() + " from " + url);
            } else {
                System.out.println("Skipping URL " + url + ": No valid book data found (title or author missing)");
            }
        } catch (Exception e) {
            System.err.println("Error scraping " + url + ": " + e.getMessage());
        }
    }

    private Book scrapeBookData(String url) throws IOException {
        Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(10000).get();

        // Extract title
        String title = null;
        Element titleElement = doc.select("h1").first();
        if (titleElement != null && !titleElement.text().trim().isEmpty()) {
            title = titleElement.text().trim();
        } else {
            Element tableTitle = doc.select("tr:has(th:contains(Title)) td").first();
            if (tableTitle != null && !tableTitle.text().trim().isEmpty()) {
                title = tableTitle.text().trim();
            }
        }
        if (title == null || title.isEmpty()) {
            System.out.println("No valid title found at " + url);
            return null;
        }

        // Extract author
        String author = "Unknown Author";
        Element authorElement = doc.select("tr:has(th:contains(Author)) td a").first();
        if (authorElement != null && !authorElement.text().trim().isEmpty()) {
            author = authorElement.text().trim();
        }

        // Debugging before creating Book
        System.out.println("Scraped - Title: " + title + ", Author: " + author + " from " + url);

        // Create Book object
        Book book = new Book(title, author, 5);
        return book;
    }
}