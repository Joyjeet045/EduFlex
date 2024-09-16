package com.example.my_project.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.Duration;


import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Getter
public class Lecture {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  private Course course;

  @Column(nullable = false, length = 100)
  private String title;

  
  @Pattern(regexp = "^(https?|ftp)://.*$", message = "Invalid URL format")
  private String videoUrl;

  @Column(nullable = false)
  private Duration duration; 
  private String thumbnail;

}