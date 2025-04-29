package org.polina.practice.service;

import org.polina.practice.dto.AddBookRequest;
import org.polina.practice.dto.UpdateBookRequest;
import org.polina.practice.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<Book> getAllBooks(Pageable pageable);
    Book getBookById(Long id);
    Book addBook(AddBookRequest request);
    Book updateBook(Long id, UpdateBookRequest request);
    void deleteBookById(Long id);
}
