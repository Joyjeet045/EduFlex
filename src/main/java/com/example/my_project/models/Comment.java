package com.example.my_project.models;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class Comment {
  private Long id;
  private Long userId;
  private Long lectureId; 
  private String message;
  private Timestamp createdAt; 
  private int likes;       
  private int dislikes;    
}
