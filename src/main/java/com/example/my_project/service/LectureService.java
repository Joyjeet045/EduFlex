package com.example.my_project.service;

import com.example.my_project.dao.LectureDao;
import com.example.my_project.models.Course;
import com.example.my_project.models.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LectureService {
  private final LectureDao lectureDao;

  @Autowired
  public LectureService(LectureDao lectureDao) {
    this.lectureDao = lectureDao;
  }
  public boolean addLectureToCourse(Course course, Lecture lecture) {
    lecture.setCourse(course);
    lectureDao.addLecture(lecture);
    return true;
  }
  public List<Lecture> getLecturesByCourseId(Long courseId) {
    return lectureDao.findLecturesByCourseId(courseId);
  }
}