package com.example.my_project.models;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Issue {
  private Long id;
  private Long bookId;         
  private Long userId;           
  private Timestamp issueDate;
  private Timestamp returnDate;  
}
