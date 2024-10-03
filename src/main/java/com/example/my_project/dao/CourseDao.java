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

  // Method to add a new course
  public int addCourse(Course course) {
    final String sql = "INSERT INTO courses (title, description, language, instructor_id, start_date, category, price, thumbnail) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    // Insert course details into the database
    return jdbcTemplate.update(sql,
      course.getTitle(),                              // Course title
      course.getDescription(),                        // Course description
      course.getLanguage().name(),                    // Language
      course.getInstructor().getId(),                 // Instructor ID
      course.getStartDate(),                          // Start date
      course.getCategory().name(),                    // Course category
      course.getPrice(),                              // Course price
      course.getThumbnail()                           // Thumbnail image
    );
  }

  // Method to find all courses
  public List<Course> findAll() {
    String sql = "SELECT * FROM courses"; // Select all courses from the database
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Course.class));
  }

  // Method to find a course by its ID
  public Optional<Course> findById(Long id) {
    String sql = "SELECT * FROM courses WHERE id = ?";
    try {
      // Query for a course by its ID
      Course course = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Course.class), id);
      return Optional.of(course);
    } catch (Exception e) {
      System.out.println(e);  // Handle any exceptions (e.g., course not found)
      return Optional.empty();  // Return an empty optional if not found
    }
  }
}
