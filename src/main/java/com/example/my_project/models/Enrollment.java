package com.example.my_project.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;


import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Getter
public class Enrollment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @JoinColumn(name = "course_id", nullable = false)
  private Course course;

  @ManyToOne
  @JoinColumn(name = "learner_id", nullable = false)
  private User learner;

  @Column(name = "enrollment_date", nullable = false)
  private LocalDate enrollmentDate;

  @Column(nullable = false)
  private Double progress = 0.0; 
}


