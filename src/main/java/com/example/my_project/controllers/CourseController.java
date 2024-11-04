package com.example.my_project.controllers;

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import com.example.my_project.dao.*;
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
import java.util.*;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final LectureService lectureService;
    private final EnrollmentService enrollmentService;
    private final UserProgressDao userProgressDao;

    private final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    public CourseController(CourseService courseService, UserService userService, LectureService lectureService,EnrollmentService enrollmentService,UserProgressDao userProgressDao) {
        this.courseService = courseService;
        this.userService = userService;
        this.lectureService = lectureService;
        this.enrollmentService=enrollmentService;
        this.userProgressDao=userProgressDao;
    }

    @GetMapping("/courses")
    public String getAllCourses(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Language language,
            Model model, 
            Principal principal) {
        
        String username = principal.getName();
        Map<Long, Integer> courseLectureCounts = new HashMap<>();

        List<Course> courses = courseService.findAll();

        if (title != null || category != null || language != null) {
            courses = courses.stream()
                    .filter(course -> (title == null || course.getTitle().toLowerCase().contains(title.toLowerCase())))
                    .filter(course -> (category == null || course.getCategory() == category))
                    .filter(course -> (language == null || course.getLanguage() == language))
                    .collect(Collectors.toList());
        }

        for (Course course : courses) {
            int lectureCount = lectureService.getLecturesByCourseId(course.getId()).size();
            courseLectureCounts.put(course.getId(), lectureCount);
        }

        model.addAttribute("username", username);
        model.addAttribute("courseLectureCounts", courseLectureCounts);
        model.addAttribute("courses", courses); 
        return "course-list";  
    }


    @GetMapping("/course/{id}")
    public String viewCourse(@PathVariable("id") Long id, Model model,Principal principal) {
        Course course = courseService.findCourseById(id); 
        Long userId=userService.findUser(principal.getName()).getId();

        if (course == null) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(id);
        List<Long> learnerIds = enrollments.stream()
                                    .map(Enrollment::getLearnerId)
                                    .toList();
        List<Lecture> lectures = lectureService.getLecturesByCourseId(id);
        Map<Long, Boolean> lectureCompletionStatus = new HashMap<>();
        for (Lecture lecture : lectures) {
            boolean isCompleted = userProgressDao.isLectureCompletedByUser(userId, lecture.getId());
            lectureCompletionStatus.put(lecture.getId(), isCompleted);
        }
        boolean isInstructor = course.getInstructor() != null && 
                        course.getInstructor().equals(userId);

        double courseProgress = calculateCourseProgress(lectures, userId);
        model.addAttribute("lectureCompletionStatus", lectureCompletionStatus);
        model.addAttribute("course", course);
        model.addAttribute("enrolled",learnerIds);
        model.addAttribute("lectures", lectures);
        model.addAttribute("courseProgress", courseProgress);
        model.addAttribute("isInstructor", isInstructor);
        return "course-details";
    }

    @GetMapping("/courses/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("languages", Language.values());
        model.addAttribute("categories", Category.values());
        return "add-course";
    }

    @PostMapping("/courses/add")
    public String addCourse(@Valid @ModelAttribute Course course,
                        @RequestParam("thumbnail") String thumbnailUrl,
                        BindingResult result,
                            Model model, Principal principal) {
        if (result.hasErrors()) {
            return "add-course"; 
        }
        
        if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
            result.rejectValue("thumbnail", "error.thumbnail", "Please provide a thumbnail URL");
            return "add-course"; 
        }

        course.setThumbnail(thumbnailUrl);

        User currentUser = userService.findUser(principal.getName());
        if (currentUser == null) {
            return "redirect:/login"; 
        }

        course.setInstructor(currentUser.getId()); 
        courseService.addCourse(course);
        model.addAttribute("message", "Course added successfully!");
        return "redirect:/courses";
    }

    private double calculateCourseProgress(List<Lecture> lectures, Long userId) {
        int totalLectures = lectures.size();
        int completedLectures = 0;

        for (Lecture lecture : lectures) {
            if (userProgressDao.isLectureCompletedByUser(userId, lecture.getId())) {
                completedLectures++;
            }
        }
        
        return (totalLectures > 0) ? (double) completedLectures / totalLectures * 100 : 0;
    }
}

