package com.example.my_project.dao;

import com.example.my_project.models.RecommendedBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecommendedDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RecommendedDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RecommendedBook> findByCourseId(Long courseId) {
        String sql = "SELECT * FROM recommended_books WHERE course_id = ?";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RecommendedBook.class), courseId);
        } catch (DataAccessException e) {
            System.out.println("Error fetching recommended books by course ID: " + e.getMessage());
            throw new RuntimeException("Error fetching recommended books by course ID", e);
        }
    }

    public int addRecommendedBook(RecommendedBook recommendedBook) {
        final String insertSql = "INSERT INTO recommended_books (course_id, book_id, notes) VALUES (?, ?, ?)";
        try {
            return jdbcTemplate.update(insertSql,
                recommendedBook.getCourseId(),
                recommendedBook.getBookId(),
                recommendedBook.getNotes()
            );
        } catch (DataAccessException e) {
            System.out.println("Error adding recommended book: " + e.getMessage());
            throw new RuntimeException("Error adding recommended book to the database", e);
        }
    }
}
