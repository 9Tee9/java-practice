package org.polina.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.polina.practice.entity.Book;
import org.polina.practice.service.BookService;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private MockMvc mockMvc;
    @Mock
    private BookService bookService;
    @InjectMocks
    private BookController bookController;
    private ObjectMapper objectMapper;

    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        book1 = new Book(1L, "Book One", "Author One", 2021);
        book2 = new Book(2L, "Book Two", "Author Two", 2022);
    }

    @Test
    public void whenGetAllBooks_thenReturnListOfBooks() throws Exception {
        List<Book> books = Arrays.asList(book1, book2);
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/book/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Book One"))
                .andExpect(jsonPath("$[0].author").value("Author One"))
                .andExpect(jsonPath("$[0].publicationYear").value(2021))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Book Two"))
                .andExpect(jsonPath("$[1].author").value("Author Two"))
                .andExpect(jsonPath("$[1].publicationYear").value(2022));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    public void whenGetBookById_thenReturnBook() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(book1);

        mockMvc.perform(get("/api/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Book One"))
                .andExpect(jsonPath("$.author").value("Author One"))
                .andExpect(jsonPath("$.publicationYear").value(2021));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    public void whenAddBook_thenReturnAddedBook() throws Exception {
        when(bookService.addBook(any(Book.class))).thenReturn(book1);

        mockMvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title" : "Book One",
                                "author" : "Author One",
                                "publicationYear" : 2021
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Book One"))
                .andExpect(jsonPath("$.author").value("Author One"))
                .andExpect(jsonPath("$.publicationYear").value(2021));

        verify(bookService, times(1)).addBook(any(Book.class));
    }

    @Test
    public void whenUpdateBook_thenReturnUpdatedBook() throws Exception {
        Book updatedBook = new Book(1L, "Updated Book One", "Updated Author", 2010);
        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/book/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title" : "Updated Book One",
                                "author" : "Updated Author",
                                "publicationYear" : 2010
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Book One"))
                .andExpect(jsonPath("$.author").value("Updated Author"))
                .andExpect(jsonPath("$.publicationYear").value(2010));

        verify(bookService, times(1)).updateBook(eq(1L), any(Book.class));
    }

    @Test
    public void whenDeleteBookById_thenReturnNoContent() throws Exception {
        doNothing().when(bookService).deleteBookById(1L);

        mockMvc.perform(delete("/api/book/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBookById(1L);
    }
}
