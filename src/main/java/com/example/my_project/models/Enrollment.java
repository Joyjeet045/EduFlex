package com.example.my_project.models;


import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;

@Data
public class Enrollment {
    private Long id;
    private Course course;
    private User learner;
    private LocalDate enrollmentDate;
    private Double progress = 0.0;
}
