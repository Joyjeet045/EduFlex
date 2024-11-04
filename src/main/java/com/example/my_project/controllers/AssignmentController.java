package com.example.my_project.controllers;

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@Controller
@RequestMapping("/assignments")
public class AssignmentController {

    private AssignmentService assignmentService;
    private CourseService courseService; 

    @Autowired
    public AssignmentController(CourseService courseService,AssignmentService assignmentService) {
        this.assignmentService=assignmentService;
        this.courseService = courseService;
    }

    @GetMapping("/create/{courseId}")
    public String showCreateAssignmentForm(@PathVariable("courseId") Long courseId, Model model) {

        model.addAttribute("assignment", new Assignment());
        model.addAttribute("course", courseService.findCourseById(courseId));
        return "create-assignment"; 
    }

    @PostMapping("/create/{courseId}")
    public String createAssignment(@PathVariable("courseId") Long courseId,
                                   @RequestParam("due_date") String dueDateStr,
                                   @RequestParam("max_points") Integer maxPoints,
                                   Assignment assignment) {
        assignment.setCourseId(courseId);
        assignment.setCreatedDate(convertToDateViaInstant(LocalDate.now())); 

        LocalDate dueDate = LocalDate.parse(dueDateStr);  
        assignment.setDueDate(convertToDateViaInstant(dueDate));
        assignment.setMaxPoints(maxPoints);
        assignmentService.save(assignment);
        return "redirect:/course/{courseId}";
    }

    @GetMapping("/api/{courseId}")
    @ResponseBody 
    public List<Assignment> getAssignment(@PathVariable Long courseId) {
      return assignmentService.findByCourseId(courseId);
    }
    public static Date convertToDateViaInstant(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }


}
