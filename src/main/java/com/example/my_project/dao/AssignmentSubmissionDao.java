package com.example.my_project.dao;

import com.example.my_project.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AssignmentSubmissionDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AssignmentSubmissionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AssignmentSubmission saveSubmission(AssignmentSubmission submission) {
        String sql = "INSERT INTO assignment_submissions (assignment_id, student_id, submission_date, attachment_url, status, points, feedback) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            submission.getAssignmentId(),
            submission.getStudentId(),
            submission.getSubmissionDate(),
            submission.getAttachmentUrl(),
            submission.getStatus().name(), 
            submission.getPoints(),
            submission.getFeedback()
        );

        Long generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        submission.setId(generatedId); 
        return submission;
    }

    public List<AssignmentSubmission> getSubmissionsByAssignId(Long assignmentId) {
        String sql = "SELECT * FROM assignment_submissions WHERE assignment_id =? AND status =?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AssignmentSubmission.class), assignmentId,"SUBMITTED");
    }

    public AssignmentSubmission findSubmissionById(Long id) {
        String sql = "SELECT * FROM assignment_submissions WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(AssignmentSubmission.class), id);
    }

    public void updateSubmission(Long submissionId, Integer points, String feedback, SubmissionStatus status) {
        String sql = "UPDATE assignment_submissions SET points = ?, feedback = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, points, feedback, status.name(), submissionId);
    }

    public List<AssignmentSubmission> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId) {
        String sql = "SELECT * FROM assignment_submissions WHERE assignment_id = ? AND student_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AssignmentSubmission.class), assignmentId, studentId);
    }
}
