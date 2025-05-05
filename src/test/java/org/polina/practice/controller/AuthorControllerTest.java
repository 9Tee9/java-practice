package org.polina.practice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.polina.practice.entity.Author;
import org.polina.practice.repository.AuthorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorControllerTest {
    @Mock
    private AuthorRepository authorRepository;

    private AuthorController authorController;

    private Author author1;
    private Author author2;

    @BeforeEach
    void setUp() {
        authorController = new AuthorController(authorRepository);
        author1 = new Author(1L, "Author 1", "Bio", Collections.emptyList());
        author2 = new Author(2L, "Author 2", "Bio2", Collections.emptyList());
    }

    @Test
    void whenGetAllAuthors_thenReturnListOfAuthors() throws Exception {
        List<Author> expectedAuthors = List.of(author1, author2);
        when(authorRepository.findAll()).thenReturn(expectedAuthors);

        ResponseEntity<List<Author>> response = authorController.getAllAuthors();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Author> actualAuthors = response.getBody();
        assertNotNull(actualAuthors);
        assertEquals(expectedAuthors.size(), actualAuthors.size());

        Author returnedAuthor1 = actualAuthors.getFirst();
        assertEquals(expectedAuthors.getFirst().getId(), returnedAuthor1.getId());
        assertEquals(expectedAuthors.getFirst().getName(), returnedAuthor1.getName());
        assertEquals(expectedAuthors.getFirst().getBio(), returnedAuthor1.getBio());
        assertEquals(expectedAuthors.get(0).getBooks(), returnedAuthor1.getBooks());

        Author returnedAuthor2 = actualAuthors.get(1);
        assertEquals(expectedAuthors.get(1).getId(), returnedAuthor2.getId());
        assertEquals(expectedAuthors.get(1).getName(), returnedAuthor2.getName());
        assertEquals(expectedAuthors.get(1).getBio(), returnedAuthor2.getBio());
        assertEquals(expectedAuthors.get(1).getBooks(), returnedAuthor2.getBooks());

        verify(authorRepository, times(1)).findAll();
        verifyNoMoreInteractions(authorRepository);
    }
}
