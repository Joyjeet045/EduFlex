package com.example.my_project.dao;

import com.example.my_project.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRequestDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookRequestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BookRequest save(BookRequest bookRequest) {
        String sql = "INSERT INTO book_request (user_book_id, requested_copies, request_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
            bookRequest.getUserBookId(),
            bookRequest.getRequestedCopies(),
            bookRequest.getRequestDate()
        );
        return bookRequest; 
    }
    public List<BookRequest> findByUserId(Long userId) {
        String sql = "SELECT * FROM book_request WHERE user_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BookRequest.class), userId);
    }

    public void updateStatus(Long requestId, RequestStatus status) {
        String sql = "UPDATE book_request SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status.name(), requestId);
    }

    public List<BookRequest> findAll() {
        String sql = "SELECT * FROM book_request";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BookRequest.class));
    }

    public BookRequest findById(Long id) {
        String sql = "SELECT * FROM book_request WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(BookRequest.class), id);
    }

}
