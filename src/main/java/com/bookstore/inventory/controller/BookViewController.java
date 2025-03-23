//package com.bookstore.inventory.controller;
//
//import com.bookstore.inventory.model.Book;
//import com.bookstore.inventory.model.PagedResponse;
//import com.bookstore.inventory.service.BookService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//@RequestMapping("/books")
//public class BookViewController {
//
//    private final BookService bookService;
//
//    public BookViewController(BookService bookService) {
//        this.bookService = bookService;
//    }
//
//    @GetMapping
//    public String listBooks(Model model) {
//        model.addAttribute("books", bookService.getAllBooks());
//        return "list";  // Returns the list.html Thymeleaf template
//    }
//
//    @GetMapping("/create")
//    public String createBookForm(Model model) {
//        model.addAttribute("book", new Book());
//        return "create";  // Returns the create.html Thymeleaf template
//    }
//
////    @GetMapping("/{id}")
////    public String viewBook(@PathVariable Long id, Model model) {
////        model.addAttribute("book", bookService.getBookById(id));
////        return "view";  // You can create a view.html template to show book details
////    }
//
//    @GetMapping("/{id}/edit")
//    public String editBookForm(@PathVariable Long id, Model model) {
//        model.addAttribute("book", bookService.getBookById(id));
//        return "edit";  // Returns the edit.html Thymeleaf template
//    }
//
//    @GetMapping("/searchBooks")
//    public String listBooks(Model model,
//                            @RequestParam(defaultValue = "") String query,
//                            @RequestParam(defaultValue = "title") String searchBy,
//                            @RequestParam(defaultValue = "0") int page,
//                            @RequestParam(defaultValue = "10") int size,
//                            @RequestParam(defaultValue = "title") String sortBy,
//                            @RequestParam(defaultValue = "ASC") String direction) {
//
//        // Call the searchBooks method in BookService
//        PagedResponse<Book> pagedResponse = bookService.searchBooks(query, searchBy, page, size, sortBy, direction);
//
//        // Add the search results and pagination info to the model
//        model.addAttribute("books", pagedResponse.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", (int) Math.ceil((double) pagedResponse.getTotalElements() / size));
//        model.addAttribute("totalBooks", pagedResponse.getTotalElements());
//        model.addAttribute("query", query);
//        model.addAttribute("searchBy", searchBy);
//        model.addAttribute("sortBy", sortBy);
//        model.addAttribute("direction", direction);
//
//        return "search"; // The Thymeleaf template to render
//    }
//}
