package com.example.my_project.service;

import com.example.my_project.dao.LectureDao;
import com.example.my_project.models.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService {

    private final LectureDao lectureDao;
    private final UserService userService;

    @Autowired
    public LectureService(LectureDao lectureDao, UserService userService) {
        this.lectureDao = lectureDao;
        this.userService = userService;
    }

    public boolean addLecture(Lecture lecture) {
        Long instructorId = userService.getCurrentUser().getId();
        int result = lectureDao.addLecture(lecture, instructorId);
        return result > 0;
    }

    public List<Lecture> getLecturesByCourseId(Long courseId) {
        return lectureDao.findLecturesByCourseId(courseId);
    }
}
