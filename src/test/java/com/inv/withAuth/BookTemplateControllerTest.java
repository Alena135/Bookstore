package com.inv.withAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class BookTemplateControllerTest {
    @Autowired
    private MockMvc mockMvc;

   /* @Test
    public void testAdminCanModifyBooks() throws Exception {
        // Admin user: should have access to modify (POST)
        mockMvc.perform(post("/books")
                        .with(user("admin").password("adminpass").roles("ADMIN"))
                        .param("title", "New Book")
                        .param("author", "Author Name")
                        .param("publishedDate", "2025-01-01"))
                .andExpect(status().isOk())  // 200 OK on successful modification
                .andExpect(view().name("bookList"));  // Optional: Check the view name
    }

    @Test
    public void testUserCanViewBooks() throws Exception {
        // Regular user: should only have access to view (GET)
        mockMvc.perform(get("/books")
                        .with(user("user").password("userpass").roles("USER")))
                .andExpect(status().isOk())  // 200 OK on successful GET request
                .andExpect(view().name("bookList"));  // Optional: Check the view name
    }

    @Test
    public void testUserCannotModifyBooks() throws Exception {
        // Regular user: should NOT have access to modify (POST)
        mockMvc.perform(post("/books")
                        .with(user("user").password("userpass").roles("USER"))
                        .param("title", "New Book")
                        .param("author", "Author Name")
                        .param("publishedDate", "2025-01-01"))
                .andExpect(status().isForbidden());  // 403 Forbidden
    }

    @Test
    public void testAdminCanModifyBookDetails() throws Exception {
        // Admin user: should be able to modify book details (PUT)
        mockMvc.perform(put("/books/1")
                        .with(user("admin").password("adminpass").roles("ADMIN"))
                        .param("title", "Updated Book")
                        .param("author", "Updated Author")
                        .param("publishedDate", "2025-01-02"))
                .andExpect(status().isOk())  // 200 OK
                .andExpect(view().name("bookDetails"));  // Optional: Check the view name
    }

    @Test
    public void testUnauthorizedUserCannotAccessBookModification() throws Exception {
        // Non-authenticated user: should be redirected to login (302)
        mockMvc.perform(post("/books")
                        .param("title", "New Book")
                        .param("author", "Author Name")
                        .param("publishedDate", "2025-01-01"))
                .andExpect(status().is3xxRedirection())  // 302 Redirect to login
                .andExpect(redirectedUrl("/login"));
    }*/
}
