package com.inv.withAuth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inv.withAuth.controller.BookController;
import com.inv.withAuth.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    /*@Test
    public void testGetAllBooks() throws Exception {
        // Assuming bookService.getAllBooks() returns a list of books
        List<Book> books = List.of(new Book(1L, "Title", 10L, "AuthorName", "AuthorSurname", "Genre"));
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value("AuthorName"));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = new Book(1L, "Title", 10L, "AuthorName", "AuthorSurname", "Genre");
        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("AuthorName"));
    }

    @Test
    public void testCreateBook() throws Exception {
        Book book = new Book(1L, "Title", 10L, "AuthorName", "AuthorSurname", "Genre");
        when(bookService.addBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Book added successfully with ID: 1"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book book = new Book(1L, "Updated Title", 10L, "Updated Author", "Updated AuthorSurname", "Updated Genre");
        when(bookService.updateBook(1L, book)).thenReturn(book);

        mockMvc.perform(put("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/books/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testSearchBooks() throws Exception {
        mockMvc.perform(get("/books/search/title/Spring")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "title")
                        .param("direction", "ASC")
                        .with(user("user").password("password").roles("USER")))
                .andExpect(status().isOk()) // Expected status: 200
                .andExpect(model().attributeExists("books")) // Optional: check model attributes
                .andExpect(view().name("bookList")); // Optional: check the view name
    }*/

}