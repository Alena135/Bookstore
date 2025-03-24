package com.inv.withAuth;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.inv.withAuth.model.Author;
import com.inv.withAuth.model.Book;
import com.inv.withAuth.model.Genre;
import com.inv.withAuth.model.PagedResponse;
import com.inv.withAuth.repository.AuthorRepository;
import com.inv.withAuth.repository.BookRepository;
import com.inv.withAuth.repository.GenreRepository;
import com.inv.withAuth.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService; // The class containing the method `searchBooks`

    @Mock
    private BookRepository bookRepository; // Mocking the repository dependency

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private PagedResponse<Book> pagedResponseMock;

    private Pageable pageable;
    private Page<Book> bookPage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pageable = PageRequest.of(0, 10, Sort.by("title").ascending()); // Define pageable
        bookPage = new PageImpl<>(new ArrayList<>()); // Empty page for simplicity
    }

    @Test
    public void testAddBook() {
        // Mocking AuthorRepository
        Author author = new Author("AuthorName", "AuthorSurname");
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));

        // Mocking GenreRepository
        Genre genre = new Genre("GenreName");
        when(genreRepository.findById(anyLong())).thenReturn(Optional.of(genre));

        // Creating the book to be added
        Book book = new Book("Title", 10L, "AuthorName", "AuthorSurname", "GenreName");
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Testing the book add method
        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals("Title", addedBook.getTitle());
        assertEquals(10L, addedBook.getPrice());
        assertEquals("AuthorName", addedBook.getAuthor().getName());
        assertEquals("AuthorSurname", addedBook.getAuthor().getSurname());

        boolean containsGenre;
        if (addedBook.getGenres().stream().anyMatch(g -> genre.getName().equals("GenreName")))
            containsGenre = true;
        else containsGenre = false;
        assertTrue(containsGenre, "The set should contain a genre with name 'GenreName'");

        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testUpdateBook() {
        Long bookId = 1L;

        // Mocking AuthorRepository
        Author existingAuthor = new Author("Old AuthorName", "Old AuthorSurname");
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(existingAuthor));

        // Mocking GenreRepository
        Genre existingGenre = new Genre("Old Genre");
        when(genreRepository.findById(anyLong())).thenReturn(Optional.of(existingGenre));

        // Existing book
        Book existingBook = new Book("Old Title", 10L, "Old AuthorName", "Old AuthorSurname", "Old Genre");

        // Updated book details
        Book updatedBookDetails = new Book("New Title", 100L, "New AuthorName", "New AuthorSurname", "New Genre");

        // Mocking bookRepository
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBookDetails);

        // Performing the update
        Book updatedBook = bookService.updateBook(bookId, updatedBookDetails);

        // Asserting the updated details
        assertNotNull(updatedBook);
        assertEquals("New Title", updatedBook.getTitle());
        assertEquals(100L, updatedBook.getPrice());
        assertEquals("New AuthorName", updatedBook.getAuthor().getName());
        assertEquals("New AuthorSurname", updatedBook.getAuthor().getSurname());

        // Checking if the new genre is added
        boolean containsNewGenre = updatedBook.getGenres().stream()
                .anyMatch(g -> g.getName().equals("New Genre"));
        assertTrue(containsNewGenre, "The set should contain a genre with name 'New Genre'");

        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    public void testDeleteBook() {
        Long bookId = 1L;

        // Creating the book to be deleted
        Book book = new Book(1L, "Title", 10L, "AuthorName", "AuthorSurname", "Genre");

        // Mock existsById to return true
        when(bookRepository.existsById(bookId)).thenReturn(true);

        // Mock findById to return the book
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Perform the delete
        bookService.deleteBook(bookId);

        // Verify the sequence of operations
        verify(bookRepository).existsById(bookId);
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(book);
        verify(bookRepository).deleteById(bookId);
    }

    // TEST SEARCHING
    @Test
    void testSearchBooksWithEmptySearchTerm() {
        // When searchTerm is empty or null, the service should return all books
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        PagedResponse<Book> result = bookService.searchBooks("", "title", 0, 10, "title", "asc");

        assertNotNull(result);
        verify(bookRepository, times(1)).findAll(pageable); // Verify repository call
    }

    @Test
    void testSearchBooksByTitle() {
        String searchTerm = "some title";
        when(bookRepository.findByTitleContaining(searchTerm, pageable)).thenReturn(bookPage);

        PagedResponse<Book> result = bookService.searchBooks(searchTerm, "title", 0, 10, "title", "asc");

        assertNotNull(result);
        verify(bookRepository, times(1)).findByTitleContaining(searchTerm, pageable);
    }

    @Test
    void testSearchBooksByAuthor() {
        String searchTerm = "some author";
        when(bookRepository.findByAuthorFullName(searchTerm, pageable)).thenReturn(bookPage);

        PagedResponse<Book> result = bookService.searchBooks(searchTerm, "author", 0, 10, "author", "asc");

        assertNotNull(result);
        verify(bookRepository, times(1)).findByAuthorFullName(searchTerm, pageable);
    }

    @Test
    void testSearchBooksByGenre() {
        String searchTerm = "some genre";
        when(bookRepository.findByGenres(searchTerm, pageable)).thenReturn(bookPage);

        PagedResponse<Book> result = bookService.searchBooks(searchTerm, "genre", 0, 10, "genre", "asc");

        assertNotNull(result);
        verify(bookRepository, times(1)).findByGenres(searchTerm, pageable);
    }


}
