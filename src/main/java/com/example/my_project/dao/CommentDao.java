package com.example.my_project.dao;

import com.example.my_project.models.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CommentDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Method to save a comment
    public int saveComment(Comment comment) {
        final String sql = "INSERT INTO comments (lecture_id, user_id, comment_content, upvotes, downvotes, reply_of) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        // Insert the comment into the database
        return jdbcTemplate.update(sql,
                comment.getLecture().getId(),  // Lecture ID
                comment.getUser().getId(),     // User ID (commenter)
                comment.getCommentContent(),   // Comment content
                comment.getUpvotes(),          // Upvotes
                comment.getDownvotes(),        // Downvotes
                comment.getReplyOf() != null ? comment.getReplyOf().getCommentId() : null // Reply reference
        );
    }

    // Method to find comments by lecture ID
    public List<Comment> findByLectureId(Long lectureId) {
        final String sql = "SELECT * FROM comments WHERE lecture_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class), lectureId);
    }
}
