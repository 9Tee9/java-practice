package org.polina.practice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.polina.practice.dto.AddBookRequest;
import org.polina.practice.dto.UpdateBookRequest;
import org.polina.practice.entity.Author;
import org.polina.practice.entity.Book;
import org.polina.practice.exception.AuthorNotFoundException;
import org.polina.practice.exception.BookNotFoundException;
import org.polina.practice.repository.AuthorRepository;
import org.polina.practice.repository.BookRepository;
import org.polina.practice.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(()->
                new BookNotFoundException(MessageFormat
                        .format("Книга с id {0} не найдена!", id)));
    }

    @Override
    @Transactional
    public Book addBook(AddBookRequest request) {
        List<Author> authors = authorRepository.findAllById(request.getAuthorIds());
        if (authors.size() != (request.getAuthorIds().size())) {
            throw new AuthorNotFoundException("Один или несколько авторов не найдены!");
        }
        Book newBook = new Book();
        newBook.setTitle(request.getTitle());
        newBook.setDescription(request.getDescription());
        newBook.setAuthors(authors);
        return bookRepository.save(newBook);
    }

    @Override
    @Transactional
    public Book updateBook(Long id, UpdateBookRequest request) {
        Book updatedBook = bookRepository.findById(id).orElseThrow(()->
                new BookNotFoundException(MessageFormat
                        .format("Книга с id {0} не найдена!", id)));
        updatedBook.setTitle(request.getTitle());
        updatedBook.setDescription(request.getDescription());
        return bookRepository.save(updatedBook);
    }

    @Override
    @Transactional
    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(()->
                new BookNotFoundException(MessageFormat
                        .format("Книга с id {0} не найдена!", id)));
        List<Author> authors = book.getAuthors();
        authors.stream()
                .peek(author -> author.getBooks().remove(book))
                .forEach(authorRepository::save);
        bookRepository.deleteById(id);
    }
}
