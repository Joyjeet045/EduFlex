package com.example.my_project.controllers;

import com.example.my_project.models.Comment;
import com.example.my_project.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.*;
import java.util.*;
@RestController
@RequestMapping("/api/comments")

public class CommentController {
  private final CommentService commentService;
  @Autowired
  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }
  @PostMapping("/add")
  public ResponseEntity<String> addComment(@RequestBody @Valid Comment comment) {
    boolean added = commentService.addComment(comment);
    if (added) {
      return ResponseEntity.ok("Comment added successfully!");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add comment.");
    }
  }
  @GetMapping("/lecture/{lectureId}")
    public ResponseEntity<List<Comment>> getCommentsByLecture(@PathVariable Long lectureId) {
      List<Comment> comments = commentService.getCommentsByLecture(lectureId);
      return ResponseEntity.ok(comments);
  }
}