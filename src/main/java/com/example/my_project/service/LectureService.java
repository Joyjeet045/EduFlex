package com.example.my_project.service;

import com.example.my_project.dao.LectureDao;
import com.example.my_project.models.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService {

    private final LectureDao lectureDao;

    @Autowired
    public LectureService(LectureDao lectureDao) {
        this.lectureDao = lectureDao;
    }

    public List<Lecture> getLecturesByCourseId(Long courseId) {
        return lectureDao.findLecturesByCourseId(courseId);
    }

    public Lecture findLectureById(Long lectureId, Long courseId) {
        return lectureDao.findByIdAndCourseId(lectureId, courseId);
    }
    public int saveLecture(Lecture lecture) {
        return lectureDao.saveLecture(lecture);
    }

}
