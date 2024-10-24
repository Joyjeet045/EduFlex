package com.example.my_project.controllers;

import com.example.my_project.models.User;
import com.example.my_project.models.Course;
import com.example.my_project.service.UserService;
import com.example.my_project.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.*;
import java.io.IOException;  
import java.time.LocalDate; 

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.security.Principal;


import org.springframework.validation.FieldError;

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

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user")  User user,
            BindingResult bindingResult,
            @RequestParam("password1") String password1,
            @RequestParam("password2") String password2,
            Model model) {

        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                System.out.println("Error in object '" + error.getObjectName() + "' on field '" + error.getField() + "': " + error.getDefaultMessage());
            }
            return "register";
        }

        validateUserInput(user, password1, password2, errors);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "register"; 
        }

        user.setPassword(password1);
        user.setJoinDate(LocalDate.now());

        if (!registerNewUser(user, model)) {
            return "register";
        }

        return "redirect:/login"; 
    }

    private void validateUserInput(User user, String password1, String password2,
                                     Map<String, String> errors) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            errors.put("username", "Username is required");
        }
        
        if (!password1.equals(password2)) {
            errors.put("password", "Passwords do not match");
        }
    }

    private boolean handleProfilePictureUpload(MultipartFile profilePicture, User user,
                                            Map<String, String> errors, Model model) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + profilePicture.getOriginalFilename();
            Path path = Paths.get("uploads/profilePictures/" + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(profilePicture.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePicture(fileName); 
        } catch (IOException e) {
            errors.put("profilePicture", "Error uploading profile picture: " + e.getMessage());
            model.addAttribute("errors", errors);
            return false;
        }
        return true;
    }

    private boolean registerNewUser(User user, Model model) {
        try {
            userService.registerUser(user);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to register user. Please try again.");
            return false;
        }
        return true;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login"; 
    }


    @PostMapping("/login")
    public String postLogin(@RequestParam String username,
                            @RequestParam String password,
                            Model model) {
        System.out.println("Login attempt with username: " + username); 
        User user = userService.loginUser(username, password);
        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
        
        return "redirect:/courses";
    }

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}