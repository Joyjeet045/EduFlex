package com.example.my_project.controllers;

import com.example.my_project.models.*;
import com.example.my_project.service.*; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import java.time.Duration;

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
      System.out.println(comments);
      model.addAttribute("comments", comments);
      
      Map<Long, String> userNames = new HashMap<>();
      for (Comment comment : comments) {
          User user = userService.getUserById(comment.getUserId());
          if (user != null) {
              userNames.put(comment.getUserId(), user.getUsername());
          } else {
              userNames.put(comment.getUserId(), "Anonymous");
          }
      }
      model.addAttribute("comments", comments);
      model.addAttribute("userNames", userNames); 
      Course course = courseService.findCourseById(courseId);
      model.addAttribute("course", course); 
      return "lecture-details";
    }

    @PostMapping("/{courseId}/lecture/{lectureId}/comments")
    public String addComment(@PathVariable Long courseId, @PathVariable Long lectureId, @ModelAttribute Comment comment, Principal principal) {
      User currentUser = userService.findUser(principal.getName());
      System.out.println("Current User: " + currentUser);
      if (currentUser !=null) {
        comment.setUserId(currentUser.getId());
        comment.setLectureId(lectureId);
        commentService.saveComment(comment);
        return "redirect:/course/" + courseId + "/lecture/" + lectureId;
      } else {
        return "redirect:/error?debug=controller"; 
      }
    }

    @PostMapping("/{courseId}/lecture/{lectureId}/comments/{commentId}/like")
    public ResponseEntity<Map<String, Integer>> likeComment(
            @PathVariable Long courseId, 
            @PathVariable Long lectureId, 
            @PathVariable Long commentId) {
        
      int updatedLikes = commentService.likeComment(commentId);  
      System.out.println(updatedLikes);      
      Map<String, Integer> response = new HashMap<>();
      response.put("likes", updatedLikes);        
      return ResponseEntity.ok(response);
    }

    @PostMapping("/{courseId}/lecture/{lectureId}/comments/{commentId}/dislike")
    public ResponseEntity<Map<String, Integer>> dislikeComment(
            @PathVariable Long courseId, 
            @PathVariable Long lectureId, 
            @PathVariable Long commentId) {
        
      int updatedLikes = commentService.dislikeComment(commentId);  
      System.out.println(updatedLikes);      
      Map<String, Integer> response = new HashMap<>();
      response.put("dislikes", updatedLikes);        
      return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}/lecture/add")
    public String showAddLectureForm(@PathVariable Long courseId, Model model) {
        model.addAttribute("courseId", courseId);
        model.addAttribute("lecture", new Lecture()); 
        return "add-lecture"; 
    }

    @PostMapping("/{courseId}/lecture/add")
    public String addLecture(@PathVariable Long courseId, @ModelAttribute Lecture lecture, @RequestParam("durationInMinutes") int durationInMinutes) {
        lecture.setCourseId(courseId); 
        lecture.setDuration(Duration.ofMinutes(durationInMinutes));        
        lectureService.saveLecture(lecture); 
        return "redirect:/course/" + courseId; 
    }

    @PostMapping("/{courseId}/lecture/{lectureId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long courseId, @PathVariable Long lectureId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/course/" + courseId + "/lecture/" + lectureId; 
    }
}
