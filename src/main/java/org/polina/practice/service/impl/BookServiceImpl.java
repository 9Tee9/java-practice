package org.polina.practice.service.impl;

import lombok.RequiredArgsConstructor;
import org.polina.practice.entity.Book;
import org.polina.practice.exception.BookNotFoundException;
import org.polina.practice.repository.BookRepository;
import org.polina.practice.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository<Book> bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(()->
                new BookNotFoundException(MessageFormat
                        .format("Книга с id {0} не найдена!", id)));
    }

    @Override
    @Transactional
    public Book addBook(Book book) {
        Book newBook = new Book();
        newBook.setTitle(book.getTitle());
        newBook.setAuthor(book.getAuthor());
        newBook.setPublicationYear(book.getPublicationYear());
        return bookRepository.save(newBook);
    }

    @Override
    @Transactional
    public Book updateBook(Long id, Book book) {
        return bookRepository.update(id, book);
    }

    @Override
    @Transactional
    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(()->
                new BookNotFoundException(MessageFormat
                        .format("Книга с id {0} не найдена!", id)));
        bookRepository.delete(book.getId());
    }
}
