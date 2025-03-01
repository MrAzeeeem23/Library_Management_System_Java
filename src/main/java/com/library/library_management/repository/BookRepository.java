package com.library.library_management.repository;

import com.library.library_management.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT b FROM Book b WHERE b.title = :title AND b.author = :author")
    Optional<Book> findByTitleAndAuthor(String title, String author);
}
