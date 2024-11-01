package com.example.my_project.models;

import java.time.Duration;
import lombok.Data;

@Data
public class Lecture {
    private Long id;
    private Long courseId;
    private String title;
    private String videoUrl;
    private Duration duration;
    private String thumbnail;

    public String getFormattedDuration() {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
