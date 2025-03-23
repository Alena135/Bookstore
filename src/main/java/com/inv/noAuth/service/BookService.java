package com.inv.noAuth.service;

import com.inv.noAuth.exception.BookAlreadyExistsException;
import com.inv.noAuth.exception.BookNotFoundException;
import com.inv.noAuth.model.Author;
import com.inv.noAuth.model.Book;
import com.inv.noAuth.model.Genre;
import com.inv.noAuth.model.PagedResponse;
import com.inv.noAuth.repository.AuthorRepository;
import com.inv.noAuth.repository.BookRepository;
import com.inv.noAuth.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;

    public Book addBook(Book book) {
        if (book.getAuthor() != null) {
            book.setAuthor(findOrCreateAuthor(book.getAuthor()));
        }

        if (bookRepository.existsByTitleAndAuthor(book.getTitle(), book.getAuthor())) {
            throw new BookAlreadyExistsException(
                    book.getTitle(),
                    book.getAuthor().getName(),
                    book.getAuthor().getSurname()
            );
        }

        // Ensure genres are managed (saved or fetched)
        if (book.getGenres() != null) {
            book.setGenres(book.getGenres().stream()
                    .map(this::findOrCreateGenre)
                    .collect(Collectors.toSet()));
        }

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        // Update basic properties
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setPrice(updatedBook.getPrice());

        // Update author if provided
        if (updatedBook.getAuthor() != null) {
            existingBook.setAuthor(findOrCreateAuthor(updatedBook.getAuthor()));
        }

        // Update genres if provided
        if (updatedBook.getGenres() != null) {
            Set<Genre> processedGenres = updatedBook.getGenres().stream()
                    .map(this::findOrCreateGenre)
                    .collect(Collectors.toSet());
            existingBook.setGenres(processedGenres);
        } else {
            existingBook.getGenres().clear(); // Clear genres if none are provided
        }

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }

        Book bookToDelete = bookRepository.findById(id).get();
        bookToDelete.getGenres().clear();
        bookToDelete.setAuthor(null);
        bookRepository.save(bookToDelete);

        bookRepository.deleteById(id);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    // Generic search method that can search by different criteria
    public PagedResponse<Book> searchBooks(String searchTerm, String searchBy, int page, int size, String sortBy, String direction) {
        Sort sort = createSort(sortBy, direction);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> bookPage;

        if (searchTerm == null || searchTerm.isEmpty()) {
            // If no search term is provided, return all books
            bookPage = bookRepository.findAll(pageable);
        } else {
            // Search based on the search criteria
            switch (searchBy.toLowerCase()) {
                case "title":
                    bookPage = bookRepository.findByTitleContaining(searchTerm, pageable);
                    break;
                case "author":
                    try {
                        Long authorId = Long.parseLong(searchTerm);
                        bookPage = bookRepository.findByAuthorId(authorId, pageable);
                    } catch (NumberFormatException e) {
                        // Handle case where searchTerm is not a valid author ID
                        bookPage = Page.empty(pageable);
                    }
                    break;
                case "genre":
                    bookPage = bookRepository.findByGenres(searchTerm, pageable);
                    break;
                default:
                    // Default to search by title
                    bookPage = bookRepository.findAll(pageable);
            }
        }
        return createPagedResponse(bookPage);
    }

    // Helper methods
    private Sort createSort(String sortBy, String direction) {
        Sort.Direction dir = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Ensure sortBy is a valid field in the Book entity
        if (!Arrays.asList("title", "price").contains(sortBy.toLowerCase())) {
            sortBy = "title"; // Default sort
        }

        return Sort.by(dir, sortBy);
    }

    private <T> PagedResponse<T> createPagedResponse(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    private Author findOrCreateAuthor(Author author) {
        if (author == null) return null;

        return authorRepository.findById(Optional.ofNullable(author.getId()).orElse(0L))
                .or(() -> Optional.ofNullable(authorRepository.findByNameAndSurname(author.getName(), author.getSurname())))
                .orElseGet(() -> authorRepository.save(author));
    }

    private Genre findOrCreateGenre(Genre genre) {
        if (genre == null) return null;

        return genreRepository.findById(Optional.ofNullable(genre.getId()).orElse(0L))
                .or(() -> Optional.ofNullable(genreRepository.findByName(genre.getName())))
                .orElseGet(() -> genreRepository.save(genre));
    }

}
