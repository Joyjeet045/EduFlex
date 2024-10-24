package com.example.my_project.models;

import lombok.Data;

@Data
public class Notification {
    private Long id;
    private Long bookId;
    private Long userId; 
    private String message;
    private boolean approved;
    private boolean rejected;
}
