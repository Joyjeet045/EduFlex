package com.example.my_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.my_project.service.*;
import com.example.my_project.models.*;
import com.example.my_project.dto.*;

import java.util.List;
import lombok.Data;

@RestController
@RequestMapping("/api/courses")
public class RecommendedBooks {

    private RecommendedService recommendedService;

    @Autowired
    public RecommendedBooks(RecommendedService recommendedService) {
        this.recommendedService = recommendedService;
    }

    @GetMapping("/{courseId}/recommendedBooks")
    public ResponseEntity<List<RecommendedBookDto>> getRecommendedBooks(@PathVariable Long courseId) {
        List<RecommendedBookDto> recommendedBooks = recommendedService.getRecommendedBooks(courseId);
        if (!recommendedBooks.isEmpty()) {
            return ResponseEntity.ok(recommendedBooks);
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

}
