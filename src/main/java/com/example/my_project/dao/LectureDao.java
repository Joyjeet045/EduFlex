package com.example.my_project.dao;

import com.example.my_project.models.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Repository
public class LectureDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LectureDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private boolean isInstructor(Long courseId, Long instructorId) {
        String sql = "SELECT COUNT(*) FROM courses WHERE id = ? AND instructor_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, courseId, instructorId);
        return count > 0;
    }

    public int addLecture(Lecture lecture, Long instructorId) {
        if (!isInstructor(lecture.getCourse().getId(), instructorId)) {
            return 0; 
        }
        String sql = "INSERT INTO lectures(course_id, title, video_url, duration, thumbnail) VALUES(?, ?, ?, ?, ?)";
        try {
            return jdbcTemplate.update(sql,
                lecture.getCourse().getId(),
                lecture.getTitle(),
                lecture.getVideoUrl(),
                lecture.getDuration().toMinutes(),
                lecture.getThumbnail()
            );
        } catch (DataAccessException e) {
            throw new RuntimeException("Error adding lecture", e);
        }
    }

    public List<Lecture> findLecturesByCourseId(Long courseId) {
        String sql = "SELECT * FROM lectures WHERE course_id = ?";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Lecture.class), courseId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error fetching lectures", e);
        }
    }

    public int updateLecture(Lecture lecture, Long instructorId) {
        if (!isInstructor(lecture.getCourse().getId(), instructorId)) {
            return 0;
        }

        String sql = "UPDATE lectures SET title = ?, video_url = ?, duration = ?, thumbnail = ? WHERE id = ? AND course_id = ?";
        try {
            return jdbcTemplate.update(sql,
                lecture.getTitle(),
                lecture.getVideoUrl(),
                lecture.getDuration().toMinutes(),
                lecture.getThumbnail(),
                lecture.getId(),
                lecture.getCourse().getId()
            );
        } catch (DataAccessException e) {
            throw new RuntimeException("Error updating lecture", e);
        }
    }

    public int deleteLecture(Long lectureId, Long courseId, Long instructorId) {
        if (!isInstructor(courseId, instructorId)) {
            return 0; 
        }
        String sql = "DELETE FROM lectures WHERE id = ? AND course_id = ?";
        try {
            return jdbcTemplate.update(sql, lectureId, courseId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error deleting lecture", e);
        }
    }
}
