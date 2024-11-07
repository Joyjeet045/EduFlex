package com.example.my_project.service;

import com.example.my_project.dao.*;
import com.example.my_project.models.*;
import com.example.my_project.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class CourseService {

    private final CourseDao courseDao;
    private final BookDao bookDao;
    private final EnrollmentDao enrollmentDao;
    private final UserService userService;

    @Autowired
    public CourseService(CourseDao courseDao, EnrollmentDao enrollmentDao, UserService userService,BookDao bookDao) {
        this.courseDao = courseDao;
        this.enrollmentDao = enrollmentDao;
        this.userService = userService;
        this.bookDao=bookDao;
    }

    public Course findCourseById(Long id) {
        Course course = courseDao.findById(id);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with id: " + id);
        }
        return course;
    }

    public List<Course> findAll() {
        return courseDao.findAll();
    }

    public Course addCourse(Course course) {
        if (course != null) {
            return courseDao.addCourse(course);  
        } else {
            return null;  
        }
    }
    public boolean updateCourse(Course course) {
        if (course != null) {
            return courseDao.updateCourse(course) > 0;  
        } else {
            return false;  
        }
    }

    public List<Course> getAllCoursesByInstructor(Long instructorId) {
        return courseDao.findByInstructorId(instructorId);
    }
    
}
