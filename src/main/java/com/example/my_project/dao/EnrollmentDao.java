package com.example.my_project.dao;

import com.example.my_project.models.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnrollmentDao {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public EnrollmentDao(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
  }
  public int save(Enrollment enrollment) {
    final String insertSql = "INSERT INTO enrollments (course_id, learner_id, enrollment_date, progress) VALUES (?, ?, ?, ?)";
    try {
        return jdbcTemplate.update(insertSql,
            enrollment.getCourse().getId(),
            enrollment.getLearner().getId(),
            enrollment.getEnrollmentDate(),
            enrollment.getProgress()
        );
    } catch (DataAccessException e) {
        System.out.println("Error saving enrollment: " + e.getMessage());
        throw new RuntimeException("Error saving enrollment to the database", e);
    }
  }
  public Enrollment findByUserAndCourse(Long userId, Long courseId) {
    String sql = "SELECT * FROM enrollments WHERE learner_id = ? AND course_id = ?";
    try {
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Enrollment.class), userId, courseId);
    } catch (DataAccessException e) {
        System.out.println("Error fetching enrollment by user and course: " + e.getMessage());
        return null; 
    }
  }
  public List<Enrollment> findByUserId(Long userId) {
    String sql = "SELECT * FROM enrollments WHERE learner_id = ?";
    try {
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Enrollment.class), userId);
    } catch (DataAccessException e) {
        System.out.println("Error fetching enrollments by user ID: " + e.getMessage());
        throw new RuntimeException("Error fetching enrollments by user ID", e);
    }
  }
  public List<Enrollment> findByCourseId(Long courseId) {
    String sql = "SELECT * FROM enrollments WHERE course_id = ?";
    try {
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Enrollment.class), courseId);
    } catch (DataAccessException e) {
        System.out.println("Error fetching enrollments by course ID: " + e.getMessage());
        throw new RuntimeException("Error fetching enrollments by course ID", e);
    }
  }
}