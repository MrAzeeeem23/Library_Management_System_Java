package com.library.library_management.patterns;

import com.library.library_management.model.Book;

public interface BookObserver {
    void update(Book book);  // Called when a book becomes available
}
