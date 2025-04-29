package org.polina.practice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.polina.practice.dto.AddBookRequest;
import org.polina.practice.dto.BookFilter;
import org.polina.practice.dto.BookListResponse;
import org.polina.practice.dto.UpdateBookRequest;
import org.polina.practice.entity.Book;
import org.polina.practice.mapper.BookMapper;
import org.polina.practice.service.BookService;
import org.polina.practice.views.Views;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/book")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping("/all")
    public ResponseEntity<BookListResponse> getAllBooks(@Valid BookFilter filter) {
        Pageable pageable = PageRequest.of(
                filter.getPageNumber() != null ? filter.getPageNumber() : 0,
                filter.getPageSize() != null ? filter.getPageSize() : 10
        );
        Page<Book> bookPage = bookService.getAllBooks(pageable);
        BookListResponse response = bookMapper.pageToBookListResponse(bookPage);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    @JsonView(Views.BookDetails.class)
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok().body(bookService.getBookById(id));
    }
    @PostMapping
    @JsonView(Views.BookSummary.class)
    public ResponseEntity<Book> addBook(@RequestBody @Valid AddBookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(request));
    }
    @PutMapping("/{id}")
    @JsonView(Views.BookSummary.class)
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody @Valid UpdateBookRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}
