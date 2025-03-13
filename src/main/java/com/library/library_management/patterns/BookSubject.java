package com.library.library_management.patterns;

// updated
public interface BookSubject {
    void registerObserver(BookObserver observer);
    void removeObserver(BookObserver observer);
    void notifyObservers(); // No Book parameter
}
