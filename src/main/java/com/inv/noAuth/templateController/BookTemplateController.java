package com.inv.noAuth.templateController;

import com.inv.noAuth.exception.BookNotFoundException;
import com.inv.noAuth.model.Book;
import com.inv.noAuth.model.PagedResponse;
import com.inv.noAuth.repository.BookRepository;
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
//        model.addAttribute("book", new Book());
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
        return "book_list"; // Refers to src/main/resources/templates/book_list.html
    }

    @GetMapping("/{id}/edit")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        model.addAttribute("book", book);
        return "book_form"; // Use the same form for creating and editing
    }

    // Delete a book by ID
    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/books/view-all"; // Redirect back to the list of books
    }

    @GetMapping("/search")
    public String searchPage(Model model) {
        // Add initial attributes to the model (optional, depending on whether you want to pre-fill data)
        model.addAttribute("searchBy", "title");  // Default searchBy
        model.addAttribute("query", "");  // Default query is empty
        return "book_search";  // Thymeleaf template for the search page
    }
}
