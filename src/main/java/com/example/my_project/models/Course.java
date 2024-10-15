package com.example.my_project.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import java.util.*;

@Data
public class Course {
    private Long id;
    private String title;
    private String description;
    private Language language;
    private Long instructor;
    private LocalDate startDate;
    private Category category;
    private BigDecimal price;
    private String thumbnail;
    private int maxParticipants;
}
