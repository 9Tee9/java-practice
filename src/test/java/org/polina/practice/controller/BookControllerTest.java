package org.polina.practice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.polina.practice.dto.AddBookRequest;
import org.polina.practice.dto.BookFilter;
import org.polina.practice.dto.BookListResponse;
import org.polina.practice.dto.UpdateBookRequest;
import org.polina.practice.entity.Author;
import org.polina.practice.entity.Book;
import org.polina.practice.mapper.BookMapper;
import org.polina.practice.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void whenGetAllBooks_thenReturnBookListResponse() throws Exception {
        BookFilter filter = new BookFilter();
        filter.setPageNumber(0);
        filter.setPageSize(10);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        BookListResponse response = new BookListResponse();

        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(bookPage);
        when(bookMapper.pageToBookListResponse(bookPage)).thenReturn(response);

        mockMvc.perform(get("/api/v1/book/all")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));

        verify(bookService, times(1)).getAllBooks(any(Pageable.class));
        verify(bookMapper, times(1)).pageToBookListResponse(bookPage);
    }

    @Test
    void whenGetBookById_thenReturnBook() throws Exception {
        Long bookId = 1L;

        Author author = new Author();
        author.setId(1L);
        author.setName("J.K. Rowling");
        author.setBio("Британская писательница, известная своими книгами о Гарри Поттере.");

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Harry Potter and the Philosopher's Stone");
        book.setDescription("Первая книга о приключениях Гарри Поттера.");
        book.setAuthors(Collections.singletonList(author));

        when(bookService.getBookById(bookId)).thenReturn(book);

        mockMvc.perform(get("/api/v1/book/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Harry Potter and the Philosopher's Stone"))
                .andExpect(jsonPath("$.description").value("Первая книга о приключениях Гарри Поттера."))
                .andExpect(jsonPath("$.authors[0].id").value(1L))
                .andExpect(jsonPath("$.authors[0].name").value("J.K. Rowling"))
                .andExpect(jsonPath("$.authors[0].bio").value("Британская писательница, известная своими книгами о Гарри Поттере."));

        verify(bookService, times(1)).getBookById(bookId);
    }

    @Test
    void whenAddBook_thenReturnCreatedBook() throws Exception {
        AddBookRequest request = new AddBookRequest();
        request.setTitle("New Book");
        request.setDescription("Description");
        request.setAuthorIds(Arrays.asList(1L));

        Book createdBook = new Book();
        createdBook.setId(1L);
        createdBook.setTitle(request.getTitle());
        createdBook.setDescription(request.getDescription());

        when(bookService.addBook(request)).thenReturn(createdBook);

        mockMvc.perform(post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "title": "New Book",
                                "description": "Description",
                                "authorIds": [1]
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.description").value("Description"));

        verify(bookService, times(1)).addBook(request);
    }

    @Test
    void whenUpdateBook_thenReturnUpdatedBook() throws Exception {
        Long id = 1L;
        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");

        Book updatedBook = new Book();
        updatedBook.setId(id);
        updatedBook.setTitle(request.getTitle());
        updatedBook.setDescription(request.getDescription());

        when(bookService.updateBook(id, request)).thenReturn(updatedBook);

        mockMvc.perform(put("/api/v1/book/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Updated Title",
                                    "description": "Updated Description"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(bookService, times(1)).updateBook(id, request);
    }

    @Test
    void whenDeleteBookById_thenReturnNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(bookService).deleteBookById(id);
        mockMvc.perform(delete("/api/v1/book/{id}", id))
                .andExpect(status().isNoContent());
        verify(bookService, times(1)).deleteBookById(id);
    }
}
