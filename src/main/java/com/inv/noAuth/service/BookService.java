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

/**
 * Service class for managing books in the system.
 * This class contains business logic for adding, updating, deleting, and searching books,
 * as well as managing relationships between books, authors, and genres.
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;

    /**
     * Constructor for BookService.
     * Initializes the BookService with the necessary repositories.
     * @param bookRepository Repository for managing Book entities.
     * @param authorRepository Repository for managing Author entities.
     * @param genreRepository Repository for managing Genre entities.
     */
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    /**
     * Adds a new book to the system.
     * If the book's author or genres are not present, they are either created or fetched.
     * @param book The book entity to be added.
     * @return The added Book entity.
     * @throws BookAlreadyExistsException if a book with the same title and author already exists.
     */
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

    /**
     * Updates an existing book with new information.
     * The existing book's properties are updated with the new values.
     * @param id The ID of the book to be updated.
     * @param updatedBook The book entity containing updated information.
     * @return The updated Book entity.
     * @throws BookNotFoundException if no book is found with the provided ID.
     */
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


    /**
     * Deletes a book from the system by its ID.
     * The book's relationships (author and genres) are cleared before deletion.
     * @param id The ID of the book to be deleted.
     * @throws BookNotFoundException if no book is found with the provided ID.
     */
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

    /**
     * Retrieves all books from the system.
     * @return A list of all books in the system.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Retrieves a book by its ID.
     * @param id The ID of the book to retrieve.
     * @return The Book entity with the specified ID.
     * @throws RuntimeException if the book is not found.
     */
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    /**
     * Searches for books based on different criteria such as title, author, or genre.
     * @param searchTerm The term to search for in the selected field.
     * @param searchBy The field to search by (title, author, genre, or all).
     * @param page The page number for pagination.
     * @param size The page size for pagination.
     * @param sortBy The field to sort by (title, price).
     * @param direction The direction of the sort (ASC or DESC).
     * @return A PagedResponse containing the search results.
     */
    public PagedResponse<Book> searchBooks(String searchTerm, String searchBy,
                                           int page, int size, String sortBy, String direction) {
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
                        bookPage = bookRepository.findByAuthorFullName(searchTerm, pageable);
                    } catch (Exception e) {
                        bookPage = Page.empty(pageable);
                    }
                    break;
                case "genre":
                    bookPage = bookRepository.findByGenres(searchTerm, pageable);
                    break;
                case "all":
                default:
                    bookPage = bookRepository.findAll(pageable);
            }
        }
        return createPagedResponse(bookPage);
    }

    // Helper methods
    /**
     * Creates a Sort object for pagination based on the provided sort criteria.
     * @param sortBy The field to sort by.
     * @param direction The direction of the sort (ASC or DESC).
     * @return The Sort object for pagination.
     */
    private Sort createSort(String sortBy, String direction) {
        Sort.Direction dir = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Ensure sortBy is a valid field in the Book entity
        if (!Arrays.asList("title", "price").contains(sortBy.toLowerCase())) {
            sortBy = "title"; // Default sort
        }

        return Sort.by(dir, sortBy);
    }

    /**
     * Creates a PagedResponse object to return the paginated results.
     * @param page The Page object containing paginated data.
     * @return A PagedResponse object containing the paginated data and metadata.
     */
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

    /**
     * Finds an author by its ID or name. If the author does not exist, it is created.
     * @param author The author entity to find or create.
     * @return The found or created Author entity.
     */
    private Author findOrCreateAuthor(Author author) {
        if (author == null) return null;

        return authorRepository.findById(Optional.ofNullable(author.getId()).orElse(0L))
                .or(() -> Optional.ofNullable(authorRepository.findByNameAndSurname(author.getName(), author.getSurname())))
                .orElseGet(() -> authorRepository.save(author));
    }

    /**
     * Finds a genre by its ID or name. If the genre does not exist, it is created.
     * @param genre The genre entity to find or create.
     * @return The found or created Genre entity.
     */
    private Genre findOrCreateGenre(Genre genre) {
        if (genre == null) return null;

        return genreRepository.findById(Optional.ofNullable(genre.getId()).orElse(0L))
                .or(() -> Optional.ofNullable(genreRepository.findByName(genre.getName())))
                .orElseGet(() -> genreRepository.save(genre));
    }

}
