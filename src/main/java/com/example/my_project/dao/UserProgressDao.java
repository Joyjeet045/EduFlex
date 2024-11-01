package com.example.my_project.dao;

import com.example.my_project.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserProgressDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserProgressDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveOrUpdateProgress(Long userId, Long lectureId, boolean completed) {
        String sql = "INSERT INTO user_lecture_progress (user_id, lecture_id, completed, completed_at) " +
                     "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE completed = ?, completed_at = ?";
        Timestamp completedAt = completed ? Timestamp.valueOf(LocalDateTime.now()) : null;
        jdbcTemplate.update(sql, userId, lectureId, completed, completedAt, completed, completedAt);
    }

    public boolean isLectureCompletedByUser(Long userId, Long lectureId) {
      String sql = "SELECT completed FROM user_lecture_progress WHERE user_id = ? AND lecture_id = ?";
      List<Boolean> results = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getBoolean("completed"), userId, lectureId);
      
      return !results.isEmpty() && Boolean.TRUE.equals(results.get(0));
  }


    public List<UserProgress> findAllCompletedLecturesByUserId(Long userId) {
        String sql = "SELECT * FROM user_lecture_progress WHERE user_id = ? AND completed = true";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserProgress.class), userId);
    }
}
