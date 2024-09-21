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

    public int saveComment(Comment comment) {
        final String sql = "INSERT INTO COMMENTS(lecture_id, user_id, comment_content, upvotes, downvotes, reply_of) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
            comment.getLecture().getId(),
            comment.getUser().getId(),
            comment.getCommentContent(),
            comment.getUpvotes(),
            comment.getDownvotes(),
            comment.getReplyOf() != null ? comment.getReplyOf().getCommentId() : null
        );
    }
    public List<Comment> findByLectureId(Long lectureId) {
        final String sql = "SELECT * FROM COMMENTS WHERE lecture_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class), lectureId);
    }
}
