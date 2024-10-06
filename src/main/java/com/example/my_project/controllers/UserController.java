package com.example.my_project.controllers;

import com.example.my_project.models.User;
import com.example.my_project.models.Course;
import com.example.my_project.service.UserService;
import com.example.my_project.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class UserController {
    
    private final UserService userService;
    private final CourseService courseService; 

    @Autowired
    public UserController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/user/profile/{id}")
    public String getUserProfile(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/error";  
        }
        List<Course> coursesTaught = courseService.getAllCoursesByInstructor(id);
        model.addAttribute("user", user);
        model.addAttribute("coursesTaught", coursesTaught);
        return "userProfile"; 
    }
}
