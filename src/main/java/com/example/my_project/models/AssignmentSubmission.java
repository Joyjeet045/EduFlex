package com.example.my_project.models;

import lombok.Data;

import java.util.Date;

@Data
public class AssignmentSubmission {
    private Long id;
    private Long assignmentId;
    private Long studentId;
    private Date submissionDate;
    private String attachmentUrl;
    private SubmissionStatus status;
    private Integer points;
    private String feedback;
}
