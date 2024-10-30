package com.example.my_project.dao;

import com.example.my_project.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Book saveBook(Book book) {
        String sql = "INSERT INTO book (title, author, category, thumbnail) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            book.getTitle(),
            book.getAuthor(),
            book.getCategory(),
            book.getThumbnail()
        );
        Long generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        book.setId(generatedId); 
        return book;
    }
    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM book";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Book.class));
    }

    public Book findBookById(Long id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Book.class), id);
    }

    public int deleteBook(Long id) {
        String sql = "DELETE FROM book WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
