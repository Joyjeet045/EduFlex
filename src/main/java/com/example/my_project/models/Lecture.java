package com.example.my_project.models;

import java.time.Duration;
import lombok.Data;

@Data
public class Lecture {
    private Long id;
    private Course course;
    private String title;
    private String videoUrl;
    private Duration duration;
    private String thumbnail;
}
