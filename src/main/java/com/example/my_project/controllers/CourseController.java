package com.example.my_project.controllers;

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final LectureService lectureService;

    private final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    public CourseController(CourseService courseService, UserService userService, LectureService lectureService) {
        this.courseService = courseService;
        this.userService = userService;
        this.lectureService = lectureService;
    }

    @GetMapping("/courses")
    public String getAllCourses(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "course-list";  
    }

    @GetMapping("/course/{id}")
    public String viewCourse(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findCourseById(id); 
        if (course == null) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        model.addAttribute("course", course);
        List<Lecture> lectures = lectureService.getLecturesByCourseId(id);
        model.addAttribute("lectures", lectures);
        return "course-details";
    }

    @GetMapping("/courses/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("languages", Language.values());
        model.addAttribute("categories", Category.values());
        return "add-course";
    }

    @PostMapping("/courses/add")
    public String addCourse(@Valid @ModelAttribute Course course,
                            @RequestParam("thumbnail") MultipartFile thumbnailFile,
                            BindingResult result,
                            Model model) {
        if (result.hasErrors()) {
            return "add-course"; 
        }
        if (thumbnailFile.isEmpty()) {
            result.rejectValue("thumbnail", "error.thumbnail", "Please select a thumbnail image");
            return "add-course"; 
        }
        try {
            String fileName = UUID.randomUUID().toString() + "_" + thumbnailFile.getOriginalFilename();
            Path path = Paths.get("uploads/thumbnails/" + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(thumbnailFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            course.setThumbnail(fileName);
        } catch (IOException e) {
            logger.error("Error uploading thumbnail", e);
            result.rejectValue("thumbnail", "error.thumbnail", "Error uploading thumbnail: " + e.getMessage());
            return "add-course"; 
        }
        
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login"; 
        }
        
        course.setInstructor(currentUser); 
        courseService.addCourse(course);
        model.addAttribute("message", "Course added successfully!");
        return "redirect:/courses";
    }
}
