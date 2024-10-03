package com.example.my_project.dao;

import com.example.my_project.models.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
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

    public int addLecture(Lecture lecture) {
        String sql = "INSERT INTO lectures(course_id, title, video_url, duration, thumbnail) VALUES(?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
            lecture.getCourse().getId(),
            lecture.getTitle(),
            lecture.getVideoUrl(),
            lecture.getDuration().toMinutes(),
            lecture.getThumbnail()
        );
    }

    public List<Lecture> findLecturesByCourseId(Long courseId) {
        String sql = "SELECT * FROM lectures WHERE course_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Lecture.class), courseId);
    }
}
