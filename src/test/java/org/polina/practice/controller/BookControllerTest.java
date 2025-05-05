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
    private BookResponse expectedBookResponse;
    private BookListResponse expectedBookListResponse;

    @BeforeEach
    void setUp() {
        bookController = new BookController(bookService, bookMapper);
        testAuthor = new Author(1L, "J.K. Rowling", "Bio", Collections.emptyList());
        testBook = new Book(1L, "Harry Potter", "Description", List.of(testAuthor));

        expectedBookResponse = new BookResponse();
        expectedBookResponse.setTitle("Harry Potter");
        expectedBookResponse.setDescription("Description");
        expectedBookResponse.setAuthors(List.of("J.K. Rowling"));

        expectedBookListResponse = new BookListResponse();
        expectedBookListResponse.setBooks(List.of(expectedBookResponse));
        expectedBookListResponse.setPageNumber(0);
        expectedBookListResponse.setPageSize(5);
    }

    @Test
    void whenGetAllBooks_thenReturnBookListResponse() throws Exception {
        Pageable pageable = Pageable.unpaged();
        Page<Book> bookPage = new PageImpl<>(List.of(testBook));

        when(bookService.getAllBooks(pageable)).thenReturn(bookPage);
        when(bookMapper.pageToBookListResponse(bookPage)).thenReturn(expectedBookListResponse);

        ResponseEntity<BookListResponse> response = bookController.getAllBooks(pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBookListResponse, response.getBody());

        verify(bookService, times(1)).getAllBooks(pageable);
        verify(bookMapper, times(1)).pageToBookListResponse(bookPage);

    }

    @Test
    void whenGetBookById_thenReturnBook() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(testBook);
        when(bookMapper.bookToBookResponse(testBook)).thenReturn(expectedBookResponse);

        ResponseEntity<BookResponse> response = bookController.getBookById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBookResponse, response.getBody());

        verify(bookService, times(1)).getBookById(1L);
        verify(bookMapper, times(1)).bookToBookResponse(testBook);
    }

    @Test
    void whenAddBook_thenReturnCreatedBook() throws Exception {
        AddBookRequest request = new AddBookRequest();
        request.setTitle("New Book");
        request.setDescription("New Description");
        request.setAuthorIds(List.of(1L));

        when(bookService.addBook(request)).thenReturn(testBook);
        when(bookMapper.bookToBookResponse(testBook)).thenReturn(expectedBookResponse);

        ResponseEntity<BookResponse> response = bookController.addBook(request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedBookResponse, response.getBody());

        verify(bookService, times(1)).addBook(request);
        verify(bookMapper, times(1)).bookToBookResponse(testBook);
    }

    @Test
    void whenUpdateBook_thenReturnUpdatedBook() throws Exception {
        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");

        when(bookService.updateBook(1L, request)).thenReturn(testBook);
        when(bookMapper.bookToBookResponse(testBook)).thenReturn(expectedBookResponse);

        ResponseEntity<BookResponse> response = bookController.updateBook(1L, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBookResponse, response.getBody());

        verify(bookService, times(1)).updateBook(1L, request);
        verify(bookMapper, times(1)).bookToBookResponse(testBook);
    }

    @Test
    void whenDeleteBookById_thenReturnNoContent() throws Exception {
        ResponseEntity<Void> response = bookController.deleteBookById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(bookService, times(1)).deleteBookById(1L);
    }
}
