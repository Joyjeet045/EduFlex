package com.example.my_project.controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.my_project.models.Course;
import com.example.my_project.models.User;
import com.example.my_project.service.CourseService;
import com.example.my_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
  private final CourseService courseService;
  private final UserService userService;
  @Autowired
  public CourseController(CourseService courseService, UserService userService) {
    this.courseService = courseService;
    this.userService = userService;
  }
  @PostMapping("/{courseId}/enroll/{userId}")
  public EnrollUserToCourse(@PathVariable Long courseId, @PathVariable Long userId){
    Optional<Course> course = courseService.findCourseById(courseId);
    Optional<User> user = userService.getUserById(userId);
    if(course.isPresent() && user.isPresent()){
      courseService.enrollLearner(course.get(),user.get());
      return "Enrollment successful!";
    }
    else{
      return "Enrollment failed: Course or User not found.";
    }
  }
  @GetMapping("/all")
  public List<Course> getAllCourses(@RequestParam(value = "title", required = false) String title,
    @RequestParam(value = "category", required = false) String category,
    @RequestParam(value = "instructorId", required = false) Long instructorId) {
    List<Course>course=CourseService.findAll();
    if (title != null) {
      courses = courses.stream()
      .filter(c -> c.getTitle().toLowerCase().contains(title.toLowerCase()))
      .collect(Collectors.toList());
    }
    if (category != null) {
      courses = courses.stream()
      .filter(c -> c.getCategory().equalsIgnoreCase(category))
      .collect(Collectors.toList());
    }
    if (instructorId != null) {
      courses = courses.stream()
      .filter(c -> c.getInstructor().getId().equals(instructorId))
      .collect(Collectors.toList());
    }
    return courses;
  }
}