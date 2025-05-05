package org.polina.practice.controller;

import lombok.RequiredArgsConstructor;
import org.polina.practice.entity.Author;
import org.polina.practice.repository.AuthorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/author")
public class AuthorController {
    private final AuthorRepository authorRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok().body(authorRepository.findAll());
    }
}
