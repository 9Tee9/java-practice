package org.polina.practice.mapper;

import org.mapstruct.Mapper;
import org.polina.practice.dto.BookListResponse;
import org.polina.practice.dto.BookResponse;

import org.polina.practice.entity.Author;
import org.polina.practice.entity.Book;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookMapper {

        default BookResponse bookToBookResponse(Book book) {
            BookResponse bookResponse = new BookResponse();
            bookResponse.setDescription(book.getDescription());
            bookResponse.setTitle(book.getTitle());
            List<String> authorNames = book.getAuthors().stream()
                    .map(Author::getName)
                    .collect(Collectors.toList());
            bookResponse.setAuthors(authorNames);
            return bookResponse;
        }

        default BookListResponse pageToBookListResponse(Page<Book> bookPage) {
            List<BookResponse> bookDtos = bookPage.getContent().stream()
                    .map(this::bookToBookResponse)
                    .toList();

            return new BookListResponse(
                    bookDtos,
                    bookPage.getNumber(),
                    bookPage.getSize()
            );
        }
    }
