package org.polina.practice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.polina.practice.repository.AuthorRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
public class AuthorControllerTest {
    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorController authorController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
    }

    @Test
    void getAllAuthors_ReturnsListOfAuthors() throws Exception {
        when(authorRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/author/all"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        Mockito.verify(authorRepository, times(1)).findAll();
    }
}
