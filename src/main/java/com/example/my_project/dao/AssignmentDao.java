package com.example.my_project.dao;

import com.example.my_project.models.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class AssignmentDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AssignmentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Assignment save(Assignment assignment) {
        String sql = "INSERT INTO assignments (title, description, course_id, created_date, due_date, max_points) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            assignment.getTitle(),
            assignment.getDescription(),
            assignment.getCourseId(),
            assignment.getCreatedDate(),
            assignment.getDueDate(),
            assignment.getMaxPoints()
        );
        return assignment;
    }

    public List<Assignment> findByCourseId(Long courseId) {
        String sql = "SELECT * FROM assignments WHERE course_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Assignment.class), courseId);
    }
}
