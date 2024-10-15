package com.example.my_project.models;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Enrollment {
    private Long id;
    private Long courseId;
    private Long learnerId;
    private LocalDate enrollmentDate;
    private Double progress = 0.0;
}
