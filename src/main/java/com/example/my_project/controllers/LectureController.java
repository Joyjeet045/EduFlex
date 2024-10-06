package com.example.my_project.controllers;

import com.example.my_project.models.*;
import com.example.my_project.service.*; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/course") 
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/{courseId}/lecture/{lectureId}")
    public String showLectureDetails(@PathVariable Long courseId, @PathVariable Long lectureId, Model model) {
        Lecture lecture = lectureService.findLectureById(lectureId, courseId);
        model.addAttribute("lecture", lecture);
        return "lecture-details";
    }
}
