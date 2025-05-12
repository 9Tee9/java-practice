package org.polina.practice.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository <Book> {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    Book save (Book book);
    Book update (Long id, Book book);
    void delete(Long id);
}
