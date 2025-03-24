package com.inv.noAuth.controller;


import com.inv.noAuth.exception.BookAlreadyExistsException;
import com.inv.noAuth.model.Book;
import com.inv.noAuth.model.PagedResponse;
import com.inv.noAuth.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Get all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    // Create a new book
    @PostMapping
    public ResponseEntity<String> createBook(@RequestBody Book book) {
        try {
            Book createdBook = bookService.addBook(book);
            String responseMessage = "Book added successfully with ID: " + createdBook.getId();
            return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
        } catch (BookAlreadyExistsException e) {
            String errorMessage = "Failed to add the book. Book already exists.";
            return new ResponseEntity<>(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    // Update an existing book
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    // Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Search books with pagination and sorting
    @GetMapping("/search/{searchBy}/{query}")
    public ResponseEntity<PagedResponse<Book>> searchBooks(
            @PathVariable String searchBy,
            @PathVariable String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        PagedResponse<Book> response = bookService.searchBooks(
                query, searchBy, page, size, sortBy, direction);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
