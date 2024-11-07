package com.example.my_project.dao;

import com.example.my_project.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class CourseDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Course addCourse(Course course) {
        final String checkExistenceSql = "SELECT COUNT(*) FROM courses WHERE title = ?";
        try {
            int count = jdbcTemplate.queryForObject(checkExistenceSql, Integer.class, course.getTitle());
            if (count > 0) {
                System.out.println("Course with title " + course.getTitle() + " already exists.");
                return null; // Indicate failure due to duplicate
            }
        } catch (DataAccessException e) {
            System.out.println("Error checking if course exists: " + e.getMessage());
            throw new RuntimeException("Error checking course existence", e);
        }

        final String insertSql = "INSERT INTO courses (title, description, language, instructor_id, start_date, category, price, thumbnail, max_participants) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            int rowsAffected = jdbcTemplate.update(connection -> {
                var ps = connection.prepareStatement(insertSql, new String[] {"id"});
                ps.setString(1, course.getTitle());
                ps.setString(2, course.getDescription());
                ps.setString(3, course.getLanguage().name());
                ps.setLong(4, course.getInstructorId());
                ps.setDate(5, Date.valueOf(course.getStartDate()));
                ps.setString(6, course.getCategory().name());
                ps.setDouble(7, course.getPrice().doubleValue());
                ps.setString(8, course.getThumbnail());
                ps.setInt(9, course.getMaxParticipants());
                return ps;
            }, keyHolder);

            if (rowsAffected > 0) {
                course.setId(keyHolder.getKey().longValue());
                return course;
            } else {
                System.out.println("Error adding course: No rows affected.");
                return null;
            }
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

    public List<Long> getEnrolledLearnerIds(Long courseId) {
        String sql = "SELECT learner_id FROM enrollments WHERE course_id = ?";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("learner_id"), courseId);
        } catch (DataAccessException e) {
            System.out.println("Error fetching enrolled learner IDs: " + e.getMessage());
            throw new RuntimeException("Error fetching enrolled learner IDs", e);
        }
    }

    public int updateCourse(Course course) {
        final String updateSql = "UPDATE courses SET title = ?, description = ?, language = ?, instructor_id = ?, start_date = ?, category = ?, price = ?, thumbnail = ?, max_participants = ? WHERE id = ?";
        try {
            return jdbcTemplate.update(updateSql,
                course.getTitle(),
                course.getDescription(),
                course.getLanguage().name(),
                course.getInstructorId(),
                course.getStartDate(),
                course.getCategory().name(),
                course.getPrice(),
                course.getThumbnail(),
                course.getMaxParticipants(),
                course.getId() 
            );
        } catch (DataAccessException e) {
            System.out.println("Error updating course: " + e.getMessage());
            throw new RuntimeException("Error updating course in the database", e);
        }
    }

}
