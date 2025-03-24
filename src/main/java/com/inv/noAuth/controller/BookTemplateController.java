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

@Controller
@RequestMapping("/books-web")
public class BookTemplateController {
    private final BookRepository bookRepository;

    public BookTemplateController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/create")
    public String showCreateBookForm(Model model) {
        return "book_form";
    }

    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        model.addAttribute("book", book);
        return "book_display";
    }

    @GetMapping("/view-all")
    public String viewAllBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);

        return "book_list";
    }

    @GetMapping("/{id}/edit")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        model.addAttribute("book", book);
        return "book_form";
    }

    // Delete a book by ID
    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/books/view-all";
    }

    @GetMapping("/search")
    public String searchPage(Model model) {
        model.addAttribute("searchBy", "title");
        model.addAttribute("query", "");
        return "book_list";
    }
}
