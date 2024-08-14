package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookControllerTest {
    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void testCreateBook() throws Exception {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setYearPublished(2024);

        when(bookService.saveBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Book\",\"author\":\"Test Author\",\"yearPublished\":2024}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.yearPublished").value(2024));

        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    @Test
    public void testGetAllBooks() throws Exception {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setYearPublished(2024);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setYearPublished(2023);

        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[1].title").value("Book 2"));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setYearPublished(2024);

        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.yearPublished").value(2024));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    public void testGetBookByIdNotFound() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book existingBook = new Book();
        existingBook.setTitle("Old Title");
        existingBook.setAuthor("Old Author");
        existingBook.setYearPublished(2020);

        Book updatedBook = new Book();
        updatedBook.setTitle("New Title");
        updatedBook.setAuthor("New Author");
        updatedBook.setYearPublished(2024);

        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Title\",\"author\":\"New Author\",\"yearPublished\":2024}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.author").value("New Author"))
                .andExpect(jsonPath("$.yearPublished").value(2024));

        verify(bookService, times(1)).updateBook(eq(1L), any(Book.class));
    }

    @Test
    public void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(1L);
    }

}
