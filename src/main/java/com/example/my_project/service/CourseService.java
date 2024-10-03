package com.example.my_project.service;

import com.example.my_project.dao.CourseDao;
import com.example.my_project.dao.EnrollmentDao;
import com.example.my_project.service.UserService;
import com.example.my_project.models.Course;
import com.example.my_project.models.Enrollment;
import com.example.my_project.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.my_project.models.UserRole;
import java.time.LocalDate;
import java.util.*;

@Service
public class CourseService {
  private final CourseDao courseDao;
  private final EnrollmentDao enrollmentDao;
  private final UserService userService;
  @Autowired
  public CourseService(CourseDao courseDao, EnrollmentDao enrollmentDao,UserService userService) {
    this.courseDao = courseDao;
    this.enrollmentDao = enrollmentDao;
    this.userService = userService;
  }
  public Course findCourseById(Long id) {
    return courseDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));
  }

  public List<Course> findAll() {
    return courseDao.findAll();
  }
  public boolean enrollLearner(Course course, User learner){
    Optional<Enrollment> existingEnrollment = enrollmentDao.findEnrollmentByCourseAndLearner(course.getId(), learner.getId());
    if(existingEnrollment.isPresent()){
      return false;
    }
    Enrollment enrollment = new Enrollment();
    enrollment.setCourse(course);
    enrollment.setLearner(learner);
    enrollment.setEnrollmentDate(LocalDate.now());
    enrollment.setProgress(0.0);
    enrollmentDao.enrollLearner(enrollment);
    return true;
  }

  public boolean addCourse(Course course){
    if (!userService.isCurrentUserTeacher()) {
      throw new IllegalArgumentException("Only users with the role TEACHER can add courses.");
    }
    Optional<User> currentUserOptional = userService.getCurrentUser();
    if (currentUserOptional.isPresent()) {
      User currentUser = currentUserOptional.get();
      course.setInstructor(currentUser);
      return courseDao.addCourse(course) > 0; 
    }
    else{
      return false;
    }
  }
}