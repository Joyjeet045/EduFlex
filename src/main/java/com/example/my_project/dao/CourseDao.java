package com.example.my_project.dao;

import com.example.my_project.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CourseDao {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public CourseDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public int addCourse(Course course) {
    final String sql = "INSERT INTO COURSES(title, description, language, instructor_id, start_date, category, price, thumbnail) " +
                       "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    return jdbcTemplate.update(sql,
      course.getTitle(),
      course.getDescription(),
      course.getLanguage().name(),
      course.getInstructor().getId(),
      course.getStartDate(),
      course.getCategory().name(),
      course.getPrice(),
      course.getThumbnail()
    );
  }

  public List<Course> findAll() {
    String sql = "SELECT * FROM COURSES";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Course.class));
  }

  public Optional<Course> findById(Long id) {
    String sql = "SELECT * FROM COURSES WHERE id = ?";
    try {
      Course course = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Course.class), id);
      return Optional.of(course);
    } catch (Exception e) {
      System.out.println(e);
      return Optional.empty();
    }
  }
}
