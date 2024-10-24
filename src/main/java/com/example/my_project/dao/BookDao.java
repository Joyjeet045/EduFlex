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

    public int saveBook(Book book) {
        String sql = "INSERT INTO book (title, author, category, copies, available_copies, thumbnail) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
            book.getTitle(),
            book.getAuthor(),
            book.getCategory(),
            book.getCopies(),
            book.getAvailableCopies(),
            book.getThumbnail()
        );
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
    public int updateAvailableCopies(Long id, int quantityChange) {
        String sql = "UPDATE book SET available_copies = available_copies + ? WHERE id = ?";
        return jdbcTemplate.update(sql, quantityChange, id);
    }

    public int getAvailableCopies(Long id) {
        String sql = "SELECT available_copies FROM book WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id);
    }
}
