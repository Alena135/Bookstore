package com.inv.noAuth.controller;

import com.inv.noAuth.exception.BookNotFoundException;
import com.inv.noAuth.model.Book;
import com.inv.noAuth.model.PagedResponse;
import com.inv.noAuth.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for handling HTTP requests related to book management in the web application.
 * Provides endpoints for creating, viewing, editing, deleting, and searching books.
 */
@Controller
@RequestMapping("/books-web")
public class BookTemplateController {
    private final BookRepository bookRepository;

    /**
     * Constructor that initializes the BookTemplateController with the necessary repository.
     * @param bookRepository the repository for handling book-related data
     */
    public BookTemplateController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Displays the form for creating a new book.
     * @param model the model object to pass attributes to the view
     * @return the view name for the book creation form (book_form.html)
     */
    @GetMapping("/create")
    public String showCreateBookForm(Model model) {
        return "book_form";
    }

    /**
     * Displays the details of a book by its ID.
     * @param id the ID of the book to view
     * @param model the model object to pass attributes to the view
     * @return the view name for displaying the book details (book_display.html)
     * @throws BookNotFoundException if the book with the specified ID is not found
     */
    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        model.addAttribute("book", book);
        return "book_display";
    }

    /**
     * Displays a list of all books.
     * @param model the model object to pass attributes to the view
     * @return the view name for displaying the list of books (book_list.html)
     */
    @GetMapping("/view-all")
    public String viewAllBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);

        return "book_list";
    }

    /**
     * Displays the form for editing an existing book.
     * @param id the ID of the book to edit
     * @param model the model object to pass attributes to the view
     * @return the view name for the book editing form (book_form.html)
     * @throws BookNotFoundException if the book with the specified ID is not found
     */
    @GetMapping("/{id}/edit")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        model.addAttribute("book", book);
        return "book_form";
    }

    /**
     * Deletes a book by its ID.
     * @param id the ID of the book to delete
     * @return a redirect to the view-all page after deletion
     */
    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/books/view-all";
    }

    /**
     * Displays the search page for books.
     * @param model the model object to pass attributes to the view
     * @return the view name for the search page (book_list.html)
     */
    @GetMapping("/search")
    public String searchPage(Model model) {
        model.addAttribute("searchBy", "title");
        model.addAttribute("query", "");
        return "book_list";
    }
}
