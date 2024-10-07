package com.example.my_project.models;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class Comment {
  private Long id;
  private Lecture lecture; 
  private User user;       
  private String message;
  private Timestamp createdAt; 
  private int likes;       
  private int dislikes;    
}
