package com.bookstore.inventory.repository;

import com.bookstore.inventory.model.Author;
import com.bookstore.inventory.model.Book;
import com.bookstore.inventory.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthorId(Long id);

    @Query("SELECT b FROM Book b JOIN Genre g WHERE g.name = :genreName")
    List<Book> findByGenres(String genreName);
}