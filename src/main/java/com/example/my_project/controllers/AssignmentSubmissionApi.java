package com.example.my_project.controllers;

import com.example.my_project.models.AssignmentSubmission;
import com.example.my_project.models.SubmissionStatus;
import com.example.my_project.service.AssignmentSubmissionService;
import com.example.my_project.service.FileUploadService;
import com.example.my_project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class AssignmentSubmissionApi{

    private final AssignmentSubmissionService assignmentSubmissionService;
    private final UserService userService;
    private final FileUploadService fileUploadService;

    @Autowired
    public AssignmentSubmissionApi(AssignmentSubmissionService assignmentSubmissionService, 
                                        UserService userService, 
                                        FileUploadService fileUploadService) {
        this.assignmentSubmissionService = assignmentSubmissionService;
        this.fileUploadService = fileUploadService;
        this.userService = userService;
    }

    @PostMapping("/submit")
    public ResponseEntity<AssignmentSubmission> submitAssignment(@RequestParam("file") MultipartFile file,
                                                                @RequestParam("assignmentId") Long assignmentId, 
                                                                Principal principal) {
        Long studentId = userService.findUser(principal.getName()).getId();
        String filePath = fileUploadService.saveFile(file);
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setStudentId(studentId);
        submission.setAssignmentId(assignmentId);
        submission.setAttachmentUrl(filePath);
        submission.setSubmissionDate(new Date());
        submission.setStatus(SubmissionStatus.SUBMITTED);

        assignmentSubmissionService.save(submission);

        return ResponseEntity.ok(submission);
    }
    @GetMapping
    public ResponseEntity<List<AssignmentSubmission>> getSubmissionsByAssignmentId(@RequestParam Long assignmentId, Principal principal) {
        if (assignmentId == null) {
            return ResponseEntity.badRequest().body(null); 
        }
        
        Long userId = userService.findUser(principal.getName()).getId();
        List<AssignmentSubmission> submissions = assignmentSubmissionService.findByAssignmentIdAndStudentId(assignmentId, userId);
        
        if (submissions.isEmpty()) {
            return ResponseEntity.notFound().build(); 
        }
        return ResponseEntity.ok(submissions); 
    }

}
