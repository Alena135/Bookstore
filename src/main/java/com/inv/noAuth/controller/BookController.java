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

/**
 * REST controller responsible for handling HTTP requests related to books.
 * Provides endpoints to perform CRUD operations and search books.
 */
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    /**
     * Constructor that initializes the BookController with the necessary service.
     * @param bookService the service for handling book-related operations
     */
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieves all books.
     * @return a ResponseEntity containing the list of all books and an HTTP status of OK
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Retrieves a book by its ID.
     * @param id the ID of the book to retrieve
     * @return a ResponseEntity containing the book and an HTTP status of OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    /**
     * Creates a new book.
     * @param book the book object to be created
     * @return a ResponseEntity containing a success message and an HTTP status of CREATED
     * @throws BookAlreadyExistsException if the book already exists
     */
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

    /**
     * Updates an existing book.
     * @param id the ID of the book to update
     * @param book the book object containing the updated data
     * @return a ResponseEntity containing the updated book and an HTTP status of OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    /**
     * Deletes a book by its ID.
     * @param id the ID of the book to delete
     * @return a ResponseEntity with HTTP status NO_CONTENT indicating successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Searches for books with pagination and sorting.
     * @param searchBy the field to search by (e.g., title, author)
     * @param query the search query
     * @param page the page number to retrieve (default is 0)
     * @param size the number of books per page (default is 5)
     * @param sortBy the field to sort by (default is title)
     * @param direction the direction of sorting (default is ASC)
     * @return a ResponseEntity containing the paged list of books and an HTTP status of OK
     */
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
