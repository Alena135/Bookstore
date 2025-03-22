package com.bookstore.inventory.service;

import com.bookstore.inventory.model.Author;
import com.bookstore.inventory.model.Book;
import com.bookstore.inventory.repository.AuthorRepository;
import com.bookstore.inventory.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Book> searchBooksByTitle(String title) {
        if (title != null) {
            return bookRepository.findByTitleContaining(title);
        }
        return bookRepository.findAll();
    }

    public List<Book> searchBooksByAuthor(Long id) {
        if (id != null) {
            return bookRepository.findByAuthorId(id);
        }
        return bookRepository.findAll();
    }

    public List<Book> searchBooksByGenre(String genre) {
        if (genre != null) {
            return bookRepository.findByGenres(genre);
        }
        return bookRepository.findAll();
    }
}
