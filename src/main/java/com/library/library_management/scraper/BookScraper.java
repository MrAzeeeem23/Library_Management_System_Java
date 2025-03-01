package com.library.library_management.scraper;

import com.library.library_management.model.Book;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BookScraper {
    private final ConcurrentHashMap<String, Book> bookStore;
    private final ExecutorService executorService;
    private List<String> urls;

    public BookScraper(int threadPoolSize, List<String> urls) {
        this.bookStore = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.urls = urls;
    }

    public void scrapeBooks() {
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

    public ConcurrentHashMap<String, Book> getBookStore() {
        return bookStore;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
