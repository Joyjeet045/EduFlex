package com.example.my_project.controllers;

import com.example.my_project.models.Course;
import com.example.my_project.service.CourseService;
import com.example.my_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;
import jakarta.validation.Valid;

@Controller
public class CourseController {
    
    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping("/courses")
    public String getAllCourses(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "course-list";  
    }

    @GetMapping("/course/{id}")
    public String viewCourse(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        model.addAttribute("course", course);
        return "course";
    }

    @GetMapping("/courses/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("user", userService.getCurrentUser());
        return "add-course";  
    }

    @PostMapping("/courses/add")
    public String addCourse( @ModelAttribute Course course, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-course";  
        }
        course.setInstructor(userService.getCurrentUser());
        courseService.addCourse(course);
        model.addAttribute("message", "Course added successfully!");
        return "redirect:/courses"; 
    }
}
