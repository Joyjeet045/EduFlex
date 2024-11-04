package com.example.my_project.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AssignmentSubmissionController {

    private AssignmentSubmissionService assignmentSubmissionService; 
    @Autowired
    public AssignmentSubmissionController(AssignmentSubmissionService assignmentSubmissionService){        this.assignmentSubmissionService = assignmentSubmissionService;
        this.assignmentSubmissionService = assignmentSubmissionService;
    }
    
    @GetMapping("/submissions/{assignmentId}")
    public String getSubmissions(@PathVariable("assignmentId") Long assignmentId, Model model) {
        List<AssignmentSubmission> submissions = assignmentSubmissionService.getSubmissionsByAssignId(assignmentId);
        
        model.addAttribute("submissions", submissions);
        model.addAttribute("assignmentId", assignmentId); 
        return "view-submissions";
    }

    @PostMapping("/submissions/grade")
    public String gradeSubmission(@RequestParam("submissionId") Long submissionId,
                                  @RequestParam("points") Integer points,
                                  @RequestParam("feedback") String feedback) {
        assignmentSubmissionService.gradeSubmission(submissionId, points, feedback);
        return "redirect:/submissions/" + submissionId;
    }
}
