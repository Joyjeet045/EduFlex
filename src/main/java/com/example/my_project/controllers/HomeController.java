package com.example.my_project.controller;

import com.example.my_project.models.Course;
import com.example.my_project.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class HomeController {
    private final CourseService courseService;
    @Autowired
    public HomeController(CourseService courseService) {
      this.courseService = courseService;
    }
    @GetMapping("/home")
    public String home(Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        return "home"; 
    }
}
