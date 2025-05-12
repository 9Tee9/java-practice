package org.polina.practice.mapper;

import org.polina.practice.entity.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class BookRowMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong(Book.Fields.id));
        book.setTitle(rs.getString(Book.Fields.title));
        book.setAuthor(rs.getString(Book.Fields.author));
        book.setPublicationYear(rs.getInt(Book.Fields.publicationYear));

        return book;
    }
}
