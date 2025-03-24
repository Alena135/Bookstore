package com.inv.noAuth.repository;

import com.inv.noAuth.model.Author;
import com.inv.noAuth.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByTitleContaining(String title, Pageable pageable);
    Page<Book> findByAuthorFullName(String fullName, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.name = :genreName")
    Page<Book> findByGenres(String genreName, Pageable pageable);

    boolean existsByTitleAndAuthor(String title, Author author);
}