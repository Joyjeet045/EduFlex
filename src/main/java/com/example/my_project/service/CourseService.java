package com.example.my_project.service;

import com.example.my_project.dao.CourseDao;
import com.example.my_project.dao.EnrollmentDao;
import com.example.my_project.models.Course;
import com.example.my_project.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseDao courseDao;
    private final EnrollmentDao enrollmentDao;
    private final UserService userService;

    @Autowired
    public CourseService(CourseDao courseDao, EnrollmentDao enrollmentDao, UserService userService) {
        this.courseDao = courseDao;
        this.enrollmentDao = enrollmentDao;
        this.userService = userService;
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

    public boolean addCourse(Course course) {
        if (!userService.isCurrentUserTeacher()) {
            throw new IllegalArgumentException("Only users with the role TEACHER can add courses.");
        }

        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            course.setInstructor(currentUser); 
            return courseDao.addCourse(course) > 0;  
        } else {
            return false;  
        }
    }

    public List<Course> getAllCoursesByInstructor(Long instructorId) {
        return courseDao.findByInstructorId(instructorId);
    }
}
