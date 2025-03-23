package com.bookstore.inventory.service;

import com.bookstore.inventory.model.Book;
import com.bookstore.inventory.model.PagedResponse;
import com.bookstore.inventory.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setPrice(updatedBook.getPrice());
                    book.setAuthor(updatedBook.getAuthor());
                    book.setGenres(updatedBook.getGenres());
                    return bookRepository.save(book);
                }).orElseThrow(() -> new RuntimeException("Book not found."));
    }

    public void deleteBook(Long id) {
//        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
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

}
