package com.inv.noAuth.repository;

import com.inv.noAuth.model.Author;
import com.inv.noAuth.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Book} entities.
 * Provides methods to find books by title, author, genre, and check if a book exists based on title and author.
 * Extends {@link JpaRepository} for standard CRUD operations and custom queries with pagination.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Finds books with a title containing the specified string, paginated.
     * @param title the substring to search for in the title
     * @param pageable the pagination information
     * @return a {@link Page} of books with titles containing the specified string
     */
    Page<Book> findByTitleContaining(String title, Pageable pageable);

    /**
     * Finds books by the full name of the author, paginated.
     * This method assumes that the author's full name is a concatenation of their first and last names.
     * @param fullName the full name of the author (e.g., "John Doe")
     * @param pageable the pagination information
     * @return a {@link Page} of books by the specified author
     */
    Page<Book> findByAuthorFullName(String fullName, Pageable pageable);

    /**
     * Finds books by genre name, paginated.
     * This method uses a custom query to join the {@link Book} entity with its associated {@link Genre} entities
     * and search for books that match the given genre name.
     * @param genreName the name of the genre
     * @param pageable the pagination information
     * @return a {@link Page} of books that belong to the specified genre
     */
    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.name = :genreName")
    Page<Book> findByGenres(String genreName, Pageable pageable);

    /**
     * Checks if a book with the specified title and author already exists in the repository.
     * @param title the title of the book
     * @param author the author of the book
     * @return {@code true} if a book with the specified title and author exists, {@code false} otherwise
     */
    boolean existsByTitleAndAuthor(String title, Author author);
}