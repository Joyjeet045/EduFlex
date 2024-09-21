package com.example.my_project.controller;

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/courses")
public class LectureController {
  private final LectureService lectureService;
  private final CourseService courseService;
  @Autowired
  public LectureController(LectureService lectureService,CourseService courseService) {
    this.lectureService = lectureService;
    this.courseService = courseService;
  }
  @GetMapping("/{courseId}/lectures")
  public List<Lecture> getLecturesByCourseId(@PathVariable Long courseId) {
    return lectureService.getLecturesByCourseId(courseId);
  }
  @PostMapping("/{courseId}/lectures")
  public String addLectureToCourse(@PathVariable Long courseId, @RequestBody Lecture lecture) {
    Optional<Course> course = courseService.findCourseById(courseId);
    if (course.isPresent()) {
      lectureService.addLectureToCourse(course.get(), lecture);
      return "Lecture added successfully!";
    } 
    else {
      return "Course not found.";
    }
  }
}

