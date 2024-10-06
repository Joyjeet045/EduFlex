package com.example.my_project.dao;

import com.example.my_project.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addCourse(Course course) {
        final String checkExistenceSql = "SELECT COUNT(*) FROM courses WHERE title = ?";
        try {
            int count = jdbcTemplate.queryForObject(checkExistenceSql, Integer.class, course.getTitle());
            if (count > 0) {
                System.out.println("Course with title " + course.getTitle() + " already exists.");
                return 0;
            }
        } catch (DataAccessException e) {
            System.out.println("Error checking if course exists: " + e.getMessage());
            throw new RuntimeException("Error checking course existence", e);
        }

        final String insertSql = "INSERT INTO courses (title, description, language, instructor_id, start_date, category, price, thumbnail) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            return jdbcTemplate.update(insertSql,
                course.getTitle(),
                course.getDescription(),
                course.getLanguage().name(),
                course.getInstructor().getId(),
                course.getStartDate(),
                course.getCategory().name(),
                course.getPrice(),
                course.getThumbnail()
            );
        } catch (DataAccessException e) {
            System.out.println("Error adding course: " + e.getMessage());
            throw new RuntimeException("Error adding course to the database", e);
        }
    }

    public List<Course> findAll() {
        String sql = "SELECT * FROM courses";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Course.class));
        } catch (DataAccessException e) {
            System.out.println("Error fetching courses: " + e.getMessage());
            throw new RuntimeException("Error fetching courses", e);
        }
    }

    public Course findById(Long id) {
        String sql = "SELECT * FROM courses WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Course.class), id);
        } catch (DataAccessException e) {
            System.out.println("Error fetching course by ID: " + e.getMessage());
            throw new RuntimeException("Error fetching course by ID", e);
        }
    }

    public List<Course> findByInstructorId(Long instructorId) {
        String sql = "SELECT * FROM courses WHERE instructor_id = ?";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Course.class), instructorId);
        } catch (DataAccessException e) {
            System.out.println("Error fetching courses by instructor ID: " + e.getMessage());
            throw new RuntimeException("Error fetching courses by instructor ID", e);
        }
    }
}
