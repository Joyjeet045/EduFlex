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
import java.time.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final LectureService lectureService;
    private final EnrollmentService enrollmentService;
    private final UserProgressDao userProgressDao;
    private final RecommendedService recommendedService;
    private final UserBookService userBookService;
    private final BookService bookService;
    private final ChannelService channelService;


    @Autowired
    public CourseController(CourseService courseService, UserService userService, LectureService lectureService,EnrollmentService enrollmentService,UserProgressDao userProgressDao,RecommendedService recommendedService,UserBookService userBookService,BookService bookService,ChannelService channelService) {
        this.courseService = courseService;
        this.userService = userService;
        this.channelService=channelService;
        this.lectureService = lectureService;
        this.enrollmentService=enrollmentService;
        this.userProgressDao=userProgressDao;
        this.recommendedService=recommendedService;
        this.userBookService=userBookService;
        this.bookService=bookService;
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
        User user=userService.findUser(principal.getName());
        Long userId=user.getId();
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
        boolean isInstructor = course.getInstructorId() != null && 
                        course.getInstructorId().equals(userId);
        boolean isStudent=user.getRole().equals(UserRole.STUDENT);
        double courseProgress = calculateCourseProgress(lectures, userId);
        model.addAttribute("lectureCompletionStatus", lectureCompletionStatus);
        model.addAttribute("course", course);
        model.addAttribute("enrolled",learnerIds);
        model.addAttribute("lectures", lectures);
        model.addAttribute("courseProgress", courseProgress);
        model.addAttribute("isInstructor", isInstructor);
        model.addAttribute("user", user);
        model.addAttribute("isStudent", isStudent);
        return "course-details";
    }

    @GetMapping("/courses/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("languages", Language.values());
        model.addAttribute("categories", Category.values());
        Long userId = 24L;
        List<UserBook> userBooks = userBookService.findAllByUserId(userId);
        List<Book> books = userBooks.stream()
                                .map(userBook -> bookService.findBookById(userBook.getBookId()))  // Fetch Book by bookId
                                .collect(Collectors.toList());

        model.addAttribute("books", books);

        return "add-course";
    }

    @PostMapping("/courses/add")
    public String addCourse(@Valid @ModelAttribute Course course,
                            @RequestParam("thumbnail") String thumbnailUrl,
                            BindingResult result,
                            @RequestParam Map<String, String> allParams,
                            Model model, Principal principal) {
        if (result.hasErrors()) {
            return "add-course"; 
        }
        
        if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
            result.rejectValue("thumbnail", "error.thumbnail", "Please provide a thumbnail URL");
            return "add-course"; 
        }

        course.setThumbnail(thumbnailUrl);
        course.setStartDate(LocalDate.now());

        User currentUser = userService.findUser(principal.getName());
        if (currentUser == null) {
            return "redirect:/login"; 
        }

        course.setInstructorId(currentUser.getId()); 
        Course added=courseService.addCourse(course);
        Map<Integer, Map<String, String>> bookGroups = new HashMap<>();
    
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("recommendedBooks[")) {
                Matcher matcher = Pattern.compile("recommendedBooks\\[(\\d+)\\]\\.(\\w+)").matcher(key);
                if (matcher.find()) {
                    int index = Integer.parseInt(matcher.group(1));
                    String field = matcher.group(2);
                    bookGroups.computeIfAbsent(index, k -> new HashMap<>())
                            .put(field, entry.getValue());
                }
            }
        }
        
        for (Map<String, String> bookData : bookGroups.values()) {
            String bookIdStr = bookData.get("bookId");
            String notes = bookData.get("notes");
            
            if (bookIdStr != null && !bookIdStr.isEmpty()) {
                try {
                    RecommendedBook recommendedBook = new RecommendedBook();
                    recommendedBook.setCourseId(added.getId());
                    recommendedBook.setBookId(Long.parseLong(bookIdStr));
                    recommendedBook.setNotes(notes != null ? notes : "");
                    
                    try {
                        recommendedService.addRecommendedBook(recommendedBook);
                    } catch (Exception e) {
                        // Log the error and continue with other books
                    }
                } catch (NumberFormatException e) {
                    result.rejectValue("recommendedBooks", "error.bookId", "Invalid book ID");
                    return "add-course";
                }
            }
        }
        List<String> defaultChannels = Arrays.asList(
            "general",
             "projects",
             "doubts"
        );

        try {
            for (String channel : defaultChannels) {
                channelService.createChannel(added.getId(),channel); 
            }
        } catch (Exception e) {
        }

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

