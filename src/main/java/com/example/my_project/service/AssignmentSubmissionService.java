package com.example.my_project.service;

import com.example.my_project.dao.*;
import com.example.my_project.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentSubmissionService {

    private final AssignmentSubmissionDao assignmentSubmissionDao;

    @Autowired
    public AssignmentSubmissionService(AssignmentSubmissionDao assignmentSubmissionDao) {
        this.assignmentSubmissionDao = assignmentSubmissionDao;
    }

    public AssignmentSubmission save(AssignmentSubmission submission) {
        return assignmentSubmissionDao.saveSubmission(submission);
    }

    public List<AssignmentSubmission> getSubmissionsByAssignId(Long assignmentId) {
        return assignmentSubmissionDao.getSubmissionsByAssignId(assignmentId);
    }

    public AssignmentSubmission findSubmissionById(Long id) {
        return assignmentSubmissionDao.findSubmissionById(id);
    }

    public List<AssignmentSubmission> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId) {
        return assignmentSubmissionDao.findByAssignmentIdAndStudentId(assignmentId, studentId);
    }

    public void gradeSubmission(Long submissionId, Integer points, String feedback) {
        assignmentSubmissionDao.updateSubmission(submissionId, points, feedback, SubmissionStatus.GRADED);
    }

}
