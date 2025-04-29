package org.polina.practice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.polina.practice.entity.Author;
import org.polina.practice.repository.AuthorRepository;
import org.polina.practice.views.Views;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/author")
public class AuthorController {
    private final AuthorRepository authorRepository;

    @GetMapping("/all")
    @JsonView(Views.AuthorSummary.class)
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok().body(authorRepository.findAll());
    }
}
