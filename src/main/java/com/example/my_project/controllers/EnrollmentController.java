package com.example.my_project.controller;

import com.example.my_project.models.*;
import com.example.my_project.service.EnrollmentService;
import com.example.my_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
public class EnrollmentController {
  private final EnrollmentService enrollmentService;
  private final UserService userService;

  @Autowired
  public EnrollmentController(EnrollmentService enrollmentService, UserService userService) {
    this.enrollmentService = enrollmentService;
    this.userService = userService;
  }
  @PostMapping("/course/{courseId}/enroll")
  public String enrollInCourse(@PathVariable Long courseId,Principal principal) {
    User currentUser = userService.findUser(principal.getName());
    try {
      enrollmentService.enrollUserInCourse(courseId, currentUser.getId());
      return "redirect:/course/" + courseId + "?success=true";
    } catch (RuntimeException e) {
      return "redirect:/course/" + courseId + "?error=" + e.getMessage();
    }
  }
}