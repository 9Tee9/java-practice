package org.polina.practice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.polina.practice.dto.AddBookRequest;
import org.polina.practice.dto.BookListResponse;
import org.polina.practice.dto.BookResponse;
import org.polina.practice.dto.UpdateBookRequest;
import org.polina.practice.entity.Book;
import org.polina.practice.mapper.BookMapper;
import org.polina.practice.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/book")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MODERATOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Page<Book>> getAllBooks(@PageableDefault(size = 5, sort = "title") Pageable pageable) {
        return ResponseEntity.ok().body(bookService.getAllBooks(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MODERATOR', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok().body(bookMapper.bookToBookResponse(bookService.getBookById(id)));
    }
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<BookResponse> addBook(@RequestBody @Valid AddBookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.bookToBookResponse(bookService.addBook(request)));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @RequestBody @Valid UpdateBookRequest request) {
        return ResponseEntity.ok(bookMapper.bookToBookResponse(bookService.updateBook(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}
