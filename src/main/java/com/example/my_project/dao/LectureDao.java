package com.example.my_project.dao;

import com.example.my_project.models.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

    public List<Lecture> findLecturesByCourseId(Long courseId) {
        String sql = "SELECT * FROM lectures WHERE course_id = ?";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Lecture.class), courseId);
        } catch (DataAccessException e) {
            System.out.println("Error fetching lectures: " + e.getMessage());
            throw new RuntimeException("Error fetching lectures", e);
        }
    }

    public int updateLecture(Lecture lecture, Long instructorId) {
        if (!isInstructor(lecture.getCourseId(), instructorId)) {
            System.out.println("Unauthorized: You are not the instructor of this course.");
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
                lecture.getCourseId()
            );
        } catch (DataAccessException e) {
            System.out.println("Error updating lecture: " + e.getMessage());
            throw new RuntimeException("Error updating lecture", e);
        }
    }
    public int saveLecture(Lecture lecture) {
        String sql = "INSERT INTO lectures (title, video_url, duration, thumbnail, course_id) VALUES (?, ?, ?, ?, ?)";
        try {
            return jdbcTemplate.update(sql,
                lecture.getTitle(),
                lecture.getVideoUrl(),
                lecture.getDuration().toMinutes(),
                lecture.getThumbnail(),
                lecture.getCourseId() 
            );
        } catch (DataAccessException e) {
            System.out.println("Error saving lecture: " + e.getMessage());
            throw new RuntimeException("Error saving lecture", e);
        }
    }

    public int deleteLecture(Long lectureId, Long courseId, Long instructorId) {
        if (!isInstructor(courseId, instructorId)) {
            System.out.println("Unauthorized: You are not the instructor of this course.");
            return 0; 
        }
        String sql = "DELETE FROM lectures WHERE id = ? AND course_id = ?";
        try {
            return jdbcTemplate.update(sql, lectureId, courseId);
        } catch (DataAccessException e) {
            System.out.println("Error deleting lecture: " + e.getMessage());
            throw new RuntimeException("Error deleting lecture", e);
        }
    }

    public Lecture findByIdAndCourseId(Long lectureId, Long courseId) {
        String sql = "SELECT * FROM lectures WHERE id = ? AND course_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Lecture.class), lectureId, courseId);
        } catch (DataAccessException e) {
            System.out.println("Error fetching lecture: " + e.getMessage());
            throw new RuntimeException("Error fetching lecture", e);
        }
    }
}
