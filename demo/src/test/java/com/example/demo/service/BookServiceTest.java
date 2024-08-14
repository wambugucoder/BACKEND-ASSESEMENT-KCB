package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setYearPublished(2024);

        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.saveBook(book);

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testGetAllBooks() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setYearPublished(2024);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setYearPublished(2023);

        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testGetBookById() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setYearPublished(2024);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateBook() {
        Book existingBook = new Book();
        existingBook.setTitle("Old Title");
        existingBook.setAuthor("Old Author");
        existingBook.setYearPublished(2020);

        Book updatedBook = new Book();
        updatedBook.setTitle("New Title");
        updatedBook.setAuthor("New Author");
        updatedBook.setYearPublished(2024);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        Book result = bookService.updateBook(1L, updatedBook);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    public void testUpdateBookNotFound() {
        Book updatedBook = new Book();
        updatedBook.setTitle("New Title");
        updatedBook.setAuthor("New Author");
        updatedBook.setYearPublished(2024);

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(1L, updatedBook));
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteBook() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }
}
