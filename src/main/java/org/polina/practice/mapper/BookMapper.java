package org.polina.practice.mapper;

import org.mapstruct.Mapper;
import org.polina.practice.dto.BookListResponse;
import org.polina.practice.dto.BookResponse;

import org.polina.practice.entity.Book;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

        BookResponse bookToBookResponse(Book book);

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
