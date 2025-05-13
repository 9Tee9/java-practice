package org.polina.practice.repository.impl;

import lombok.RequiredArgsConstructor;
import org.polina.practice.entity.Book;
import org.polina.practice.exception.BookNotFoundException;
import org.polina.practice.mapper.BookRowMapper;
import org.polina.practice.repository.BookRepository;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository<Book> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        return jdbcTemplate.query(sql, new BookRowMapper());
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM books WHERE id=?";
        Book book = DataAccessUtils.singleResult(
                jdbcTemplate.query(sql,
                        new ArgumentPreparedStatementSetter(new Object[] {id}),
                        new RowMapperResultSetExtractor<>(new BookRowMapper(), 1))
        );
        return Optional.of(book);
    }

    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO books (title, author, publication_year) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getPublicationYear());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        book.setId(key.longValue());
        return book;
    }

    @Override
    public Book update(Long id, Book book) {
        Book existedBook = findById(id).orElse(null);
        if (existedBook != null) {
            String sql = "UPDATE books SET title=?, author=?, publication_year=? WHERE id=?";
            jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getPublicationYear(), existedBook.getId());
            book.setId(existedBook.getId());
            return book;
        } else {
            throw new BookNotFoundException(MessageFormat
                    .format("Книга с ID {0} не найдена!", id));
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM books WHERE id=?";
        jdbcTemplate.update(sql, id);
    }
}
