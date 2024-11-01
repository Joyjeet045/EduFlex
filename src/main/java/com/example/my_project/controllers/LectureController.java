package com.example.my_project.controllers;

import com.example.my_project.models.*;
import com.example.my_project.service.*; 
import com.example.my_project.dao.*; 

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.security.Principal;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import java.time.Duration;

@Controller
@RequestMapping("/course") 
public class LectureController {
    private final LectureService lectureService;
    private final CommentService commentService; 
    private final CourseService courseService; 
    private final UserService userService; 
    private final UserProgressDao userProgressDao;

    public LectureController(LectureService lectureService, CommentService commentService,CourseService courseService,UserService userService,UserProgressDao userProgressDao) {
      this.lectureService = lectureService;
      this.commentService = commentService;
      this.courseService = courseService;
      this.userService= userService;
      this.userProgressDao=userProgressDao;
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

    @PostMapping("/{courseId}/lecture/{lectureId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long courseId, @PathVariable Long lectureId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/course/" + courseId + "/lecture/" + lectureId; 
    }
    @PostMapping("/{courseId}/lecture/{lectureId}/markAsDone")
    public String markLectureAsDone(@PathVariable Long courseId,
                                    @PathVariable Long lectureId,Principal principal) {
        Long userId=userService.findUser(principal.getName()).getId();
        userProgressDao.saveOrUpdateProgress(userId, lectureId, true);
        return "redirect:/course/" + courseId;
    }

    @GetMapping("/{courseId}/lecture/add")
    public String showAddLectureForm(@PathVariable Long courseId, Model model) {
      Course course = courseService.findCourseById(courseId);
      model.addAttribute("course", course);
      model.addAttribute("lecture", new Lecture());
      return "add-lecture"; 
    }
    @PostMapping("/{courseId}/lecture/add")
    public String addLecture(@PathVariable Long courseId, 
                             @Valid @ModelAttribute Lecture lecture, 
                             BindingResult bindingResult, 
                             Long durationInMinutes) {
        if (bindingResult.hasErrors()) {
            return "/course//{courseId}lecture/add";
        }

        lecture.setDuration(Duration.ofMinutes(durationInMinutes));
        lecture.setCourseId(courseId);
        lectureService.saveLecture(lecture);

        return "redirect:/course/" + courseId; 
    }

}
