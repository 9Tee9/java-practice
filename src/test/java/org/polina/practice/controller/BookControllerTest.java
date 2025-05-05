package org.polina.practice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.polina.practice.dto.AddBookRequest;
import org.polina.practice.dto.BookListResponse;
import org.polina.practice.dto.BookResponse;
import org.polina.practice.dto.UpdateBookRequest;
import org.polina.practice.entity.Author;
import org.polina.practice.entity.Book;
import org.polina.practice.mapper.BookMapper;
import org.polina.practice.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    private BookController bookController;

    private Book testBook;
    private Author testAuthor;

    @BeforeEach
    void setUp() {
        bookController = new BookController(bookService, bookMapper);
        testAuthor = new Author(1L, "J.K. Rowling", "Bio", Collections.emptyList());
        testBook = new Book(1L, "Harry Potter", "Description", List.of(testAuthor));
    }

    @Test
    void whenGetAllBooks_thenReturnPageOfBooks() {
        Pageable pageable = Pageable.unpaged();
        Page<Book> bookPage = new PageImpl<>(List.of(testBook));

        when(bookService.getAllBooks(pageable)).thenReturn(bookPage);

        ResponseEntity<Page<Book>> response = bookController.getAllBooks(pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(bookPage, response.getBody());

        verify(bookService, times(1)).getAllBooks(pageable);
    }

    @Test
    void whenGetBookById_thenReturnBook() {
        when(bookService.getBookById(1L)).thenReturn(testBook);
        when(bookMapper.bookToBookResponse(testBook)).thenReturn(expectedBookResponse());

        ResponseEntity<BookResponse> response = bookController.getBookById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBookResponse(), response.getBody());

        verify(bookService, times(1)).getBookById(1L);
        verify(bookMapper, times(1)).bookToBookResponse(testBook);
    }

    @Test
    void whenAddBook_thenReturnCreatedBook() {
        AddBookRequest request = new AddBookRequest();
        request.setTitle("New Book");
        request.setDescription("New Description");
        request.setAuthorIds(List.of(1L));

        when(bookService.addBook(request)).thenReturn(testBook);
        when(bookMapper.bookToBookResponse(testBook)).thenReturn(expectedBookResponse());

        ResponseEntity<BookResponse> response = bookController.addBook(request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedBookResponse(), response.getBody());

        verify(bookService, times(1)).addBook(request);
        verify(bookMapper, times(1)).bookToBookResponse(testBook);
    }

    @Test
    void whenUpdateBook_thenReturnUpdatedBook() {
        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");

        when(bookService.updateBook(1L, request)).thenReturn(testBook);
        when(bookMapper.bookToBookResponse(testBook)).thenReturn(expectedBookResponse());

        ResponseEntity<BookResponse> response = bookController.updateBook(1L, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBookResponse(), response.getBody());

        verify(bookService, times(1)).updateBook(1L, request);
        verify(bookMapper, times(1)).bookToBookResponse(testBook);
    }

    @Test
    void whenDeleteBookById_thenReturnNoContent() {
        ResponseEntity<Void> response = bookController.deleteBookById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(bookService, times(1)).deleteBookById(1L);
    }

    private BookResponse expectedBookResponse() {
        BookResponse response = new BookResponse();
        response.setTitle("Harry Potter");
        response.setDescription("Description");
        response.setAuthors(List.of("J.K. Rowling"));
        return response;
    }
}