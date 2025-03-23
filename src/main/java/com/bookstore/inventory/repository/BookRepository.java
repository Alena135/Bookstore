package com.bookstore.inventory.repository;

import com.bookstore.inventory.model.Author;
import com.bookstore.inventory.model.Book;
import com.bookstore.inventory.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByTitleContaining(String title, Pageable pageable);

    Page<Book> findByAuthorId(Long id, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN Genre g WHERE g.name = :genreName")
    Page<Book> findByGenres(String genreName, Pageable pageable);
}