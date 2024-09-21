package com.example.my_project.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Getter
public class Course{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String title;

  @Column(length = 1000)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Language language; 

  @ManyToOne
  @JoinColumn(name = "instructor_id", nullable = false)
  private User instructor; 
  
  @Column(name = "start_date")
  private LocalDate startDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  private Category category;
  private Double price=100.0;
  private String thumbnail;
}
