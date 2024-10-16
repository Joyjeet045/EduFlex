package com.example.my_project.models;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Message {
  private Long id;
  private Long channelId;
  private Long userId;
  private String content;
  private boolean isInstructor; 
  private LocalDateTime timestamp;
}