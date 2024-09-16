package com.example.my_project.dao;

import com.example.my_project.models.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EnrollmentDao {
  private final JdbcTemplate jdbcTemplate;
  @Autowired
  public EnrollmentDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  public int enrollLearner(Enrollment enrollment) {
    String sql = "INSERT INTO ENROLLMENTS(course_id, learner_id, enrollment_date, progress) VALUES(?, ?, ?, ?)";
    return jdbcTemplate.update(sql,
      enrollment.getCourse().getId(),
      enrollment.getLearner().getId(),
      enrollment.getEnrollmentDate(),
      enrollment.getProgress());
  }
  public Optional<Enrollment> findEnrollmentByCourseAndLearner(Long courseId, Long learnerId) {
    String sql="SELECT * FROM ENROLLMENTS WHERE course_id = ? AND learner_id = ?";
    try{
      Enrollment enrollment = jdbcTemplate.queryForObject(sql, new Object[]{courseId, learnerId}, new BeanPropertyRowMapper<>(Enrollment.class));
      return Optional.of(enrollment);
    } 
    catch (Exception e) {
      return Optional.empty();
    }
  }
}