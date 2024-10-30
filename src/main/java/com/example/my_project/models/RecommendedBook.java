package com.example.my_project.models;

import lombok.Data;

@Data
public class RecommendedBook {
    private Long id;           
    private Long courseId;      
    private Long bookId;        
    private String notes;       
}
