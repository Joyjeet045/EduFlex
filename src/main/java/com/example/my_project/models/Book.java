package com.example.my_project.models;

import lombok.Data;

@Data
public class Book {
  private Long id;
  private String title;
  private String author;
  private String category;
  private String thumbnail;
}
