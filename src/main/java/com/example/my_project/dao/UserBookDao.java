package com.example.my_project.dao;

import com.example.my_project.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class UserBookDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserBookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public UserBook save(UserBook userBook) {
        String sql = "INSERT INTO user_book (user_id, book_id, total_copies, available_copies) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            userBook.getUserId(),
            userBook.getBookId(),
            userBook.getTotalCopies(),
            userBook.getAvailableCopies()
        );
        return userBook; 
    }

    public UserBook findByUserIdAndBookId(Long userId, Long bookId) {
        String sql = "SELECT * FROM user_book WHERE user_id = ? AND book_id = ?";
        List<UserBook> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserBook.class), userId, bookId);
        
        if (results.isEmpty()) {
            return null; 
        }
        
        return results.get(0);
    }

    public int getAvailableCopies(Long id) {
        String sql = "SELECT available_copies FROM user_book WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id);
    }

    public int updateAvailableCopies(Long id, int quantityChange) {
        String sql = "UPDATE user_book SET available_copies = available_copies + ? WHERE id = ?";
        return jdbcTemplate.update(sql, quantityChange, id);
    }
    
    public int updateTotalCopies(Long id, int quantityChange) {
        String sql = "UPDATE user_book SET total_copies = total_copies + ? WHERE id = ?";
        return jdbcTemplate.update(sql, quantityChange, id);
    }

    public boolean hasSufficientCopies(Long userBookId, int requestedCopies) {
        String sql = "SELECT available_copies >= ? FROM user_book WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Boolean.class, requestedCopies, userBookId);
    }

    public List<UserBook> findAllByUserId(Long userId) {
        String sql = "SELECT * FROM user_book WHERE user_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserBook.class), userId);
    }

    public UserBook findById(Long Id) {
        String sql = "SELECT * FROM user_book WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(UserBook.class), Id);
    }

}