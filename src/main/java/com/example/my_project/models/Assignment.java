package com.example.my_project.models;

import lombok.Data;

import java.util.Date;

@Data
public class Assignment {
    private Long id;
    private String title;
    private String description;
    private Date dueDate;
    private Date createdDate;
    private Long courseId;
    private Integer maxPoints;
}
