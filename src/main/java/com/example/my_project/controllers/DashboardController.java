package com.example.my_project.controllers; 

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public DashboardController(EnrollmentService enrollmentService, CourseService courseService,UserService userService) {
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.userService= userService;
    }

    @GetMapping("/dashboard")
    public String userDashboard(Model model, Principal principal) {
        Long userId =userService.findUser(principal.getName()).getId(); 
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUser(userId);
        List<Course> enrolledCourses = enrollments.stream()
                                    .map(enrollment -> courseService.findCourseById(enrollment.getCourseId()))
                                    .collect(Collectors.toList());
        model.addAttribute("enrolledCourses", enrolledCourses);
        return "dashboard";
    }
}
