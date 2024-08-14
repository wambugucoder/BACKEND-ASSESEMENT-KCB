package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;


    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book updateBook(Long id, Book bookDetails) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setYearPublished(bookDetails.getYearPublished());
            return bookRepository.save(book);
        }).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
