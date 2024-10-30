package com.example.my_project.models;

import lombok.Data;

@Data
public class UserBook {
  private Long id;        
  private Long userId;     
  private Long bookId;         
  private int totalCopies;     
  private int availableCopies; 
}
