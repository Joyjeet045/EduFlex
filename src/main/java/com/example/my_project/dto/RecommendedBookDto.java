package com.example.my_project.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.my_project.models.*;
import java.util.*;
import lombok.Data;

@Data
public class RecommendedBookDto {
    private Long id; 
    private Long bookId;
    private String title; 
    private String author; 
    private String thumbnail; 
    private String notes; 

}
