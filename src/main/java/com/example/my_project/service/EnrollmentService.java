package com.example.my_project.service;

import com.example.my_project.dao.EnrollmentDao;
import com.example.my_project.models.Course;
import com.example.my_project.models.Enrollment;
import com.example.my_project.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentService {
  private final EnrollmentDao enrollmentDao;
  private final CourseService courseService;
  private final UserService userService;

  @Autowired
  public EnrollmentService(EnrollmentDao enrollmentDao, CourseService courseService, UserService userService) {
    this.enrollmentDao = enrollmentDao;
    this.courseService = courseService;
    this.userService = userService;
  }

  public Enrollment enrollUserInCourse(Long courseId, Long userId) {
    Course course=courseService.findCourseById(courseId);
    User user=userService.getUserById(userId);
    if(course!=null && user!=null){
      Enrollment existingEnrollment = enrollmentDao.findByUserAndCourse(userId, courseId);
      if(existingEnrollment!=null){
        System.out.println("User is already enrolled in this course.");
        return existingEnrollment;
      }
      else{
        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setCourse(course);
        newEnrollment.setLearner(user);
        newEnrollment.setEnrollmentDate(LocalDate.now());
        newEnrollment.setProgress(0.0);

        enrollmentDao.save(newEnrollment);
        return newEnrollment;
      }
    }
    else{
      throw new RuntimeException("Invalid course or user ID.");
    }
  }
  public List<Enrollment> getEnrollmentsByUser(Long userId) {
    return enrollmentDao.findByUserId(userId);
  }
  public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
    return enrollmentDao.findByCourseId(courseId);
  }
  public Enrollment getEnrollmentByUserAndCourse(Long userId, Long courseId) {
      return enrollmentDao.findByUserAndCourse(userId, courseId);
  }
}