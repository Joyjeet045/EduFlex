package com.example.my_project.controllers;

import com.example.my_project.models.*;
import com.example.my_project.service.*; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@RequestMapping("/course") 
public class LectureController {
    private final LectureService lectureService;
    private final CommentService commentService; 
    private final CourseService courseService; 
    private final UserService userService; 

    public LectureController(LectureService lectureService, CommentService commentService,CourseService courseService,UserService userService) {
      this.lectureService = lectureService;
      this.commentService = commentService;
      this.courseService = courseService;
      this.userService= userService;
    }

    @GetMapping("/{courseId}/lecture/{lectureId}")
    public String showLectureDetails(@PathVariable Long courseId, @PathVariable Long lectureId, Model model) {
      Lecture lecture = lectureService.findLectureById(lectureId, courseId);
      model.addAttribute("lecture", lecture);
      List<Comment> comments = commentService.getCommentsByLecture(lectureId);
      model.addAttribute("comments", comments); 
      Course course = courseService.findCourseById(courseId);
      model.addAttribute("course", course); 
      return "lecture-details";
    }

    @PostMapping("/{courseId}/lecture/{lectureId}/comments")
    public String addComment(@PathVariable Long courseId, @PathVariable Long lectureId, @ModelAttribute Comment comment) {
      Lecture lecture = lectureService.findLectureById(lectureId, courseId);
      User currentUser = userService.getCurrentUser();
      System.out.println(currentUser);
      if (lecture != null && currentUser !=null) {
        comment.setLecture(lecture);
        comment.setUser(currentUser); //current user got coming
        commentService.saveComment(comment);
        return "redirect:/course/" + courseId + "/lecture/" + lectureId;
      } else {
        return "redirect:/error?debug=controller"; 
      }
    }

    @PostMapping("/{courseId}/lecture/{lectureId}/comments/{commentId}/like")
    public String likeComment(@PathVariable Long courseId, @PathVariable Long lectureId, @PathVariable Long commentId) {
      commentService.likeComment(commentId);
      return "redirect:/course/" + courseId + "/lecture/" + lectureId; 
    }

    @PostMapping("/{courseId}/lecture/{lectureId}/comments/{commentId}/dislike")
    public String dislikeComment(@PathVariable Long courseId, @PathVariable Long lectureId, @PathVariable Long commentId) {
      commentService.dislikeComment(commentId);
      return "redirect:/course/" + courseId + "/lecture/" + lectureId; 
    }

    @PostMapping("/{courseId}/lecture/{lectureId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long courseId, @PathVariable Long lectureId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/course/" + courseId + "/lecture/" + lectureId; 
    }
}
