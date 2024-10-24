package com.example.my_project.dao;

import com.example.my_project.models.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class IssueDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public IssueDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Issue issue) {
        String sql = "INSERT INTO issues (book_id, user_id, issue_date, return_date) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, 
                issue.getBookId(), 
                issue.getUserId(), 
                issue.getIssueDate(),
                issue.getReturnDate()); 
    }
    public int updateIssue(Issue issue) {
        String sql = "UPDATE issues SET return_date = ? WHERE id = ?";
        return jdbcTemplate.update(sql, 
                new Date(System.currentTimeMillis()), 
                issue.getId());
    }

    public List<Issue> findBooksIssuedToUser(Long userId) {
        String sql = "SELECT * FROM issues WHERE user_id = ?"; 
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Issue.class), userId);
    }

    public Issue findLastIssuedBookForUser(Long userId, Long bookId) {
        String sql = "SELECT * FROM issues WHERE user_id = ? AND book_id = ? AND return_date IS NULL ORDER BY issue_date DESC LIMIT 1"; 
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Issue.class), userId, bookId);
    }

}
