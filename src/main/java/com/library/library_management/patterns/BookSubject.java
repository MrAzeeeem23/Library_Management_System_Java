package com.library.library_management.patterns;

import com.library.library_management.model.Book;

public interface BookSubject {
    void registerObserver(BookObserver observer);

    void notifyObservers(Book book);
}
