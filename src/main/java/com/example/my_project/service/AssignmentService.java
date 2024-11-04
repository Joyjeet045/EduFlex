package com.example.my_project.service;

import com.example.my_project.dao.*;
import com.example.my_project.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentDao assignmentDao;

    @Autowired
    public AssignmentService(AssignmentDao assignmentDao) {
        this.assignmentDao = assignmentDao;
    }

    public Assignment save(Assignment assignment) {
        return assignmentDao.save(assignment);
    }

    public List<Assignment> findByCourseId(Long courseId) {
        return assignmentDao.findByCourseId(courseId);
    }
}
