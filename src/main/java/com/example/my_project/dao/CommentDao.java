package com.example.my_project.dao;

import com.example.my_project.models.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.sql.Timestamp;

@Repository
public class CommentDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveComment(Comment comment) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        comment.setCreatedAt(now);  

        String sql = "INSERT INTO comment (lecture_id, user_id, message, created_at, likes, dislikes) VALUES (?, ?, ?, ?, ?, ?)";
        
        return jdbcTemplate.update(sql,
            comment.getLecture().getId(),
            comment.getUser().getId(),
            comment.getMessage(),
            comment.getCreatedAt(),
            comment.getLikes(),
            comment.getDislikes()
        );
    }

    public List<Comment> getCommentsByLecture(Long lectureId) {
        String sql = "SELECT * FROM comment WHERE lecture_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class), lectureId);
    }

    public Comment findCommentById(Long id) {
        String sql = "SELECT * FROM comment WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Comment.class), id);
    }

    public int deleteComment(Long id) {
        String sql = "DELETE FROM comment WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int updateLikes(Long commentId, int likeChange) {
        String sql = "UPDATE comment SET likes = likes + ? WHERE id = ?";
        return jdbcTemplate.update(sql, likeChange, commentId);
    }

    public int updateDislikes(Long commentId, int dislikeChange) {
        String sql = "UPDATE comment SET dislikes = dislikes + ? WHERE id = ?";
        return jdbcTemplate.update(sql, dislikeChange, commentId);
    }

    public int getLikesCount(Long commentId) {
        String sql = "SELECT likes FROM comment WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{commentId}, rs -> {
            return rs.next() ? rs.getInt("likes") : 0; 
        });
    }

    public int getDislikesCount(Long commentId) {
        String sql = "SELECT dislikes FROM comment WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{commentId}, rs -> {
            return rs.next() ? rs.getInt("dislikes") : 0;  
        });
    }
}
