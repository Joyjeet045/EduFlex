package com.example.my_project.models;

import java.time.LocalDateTime;
import lombok.Data;

public class UserProgress {
    private Long id;
    private Long userId;
    private Long lectureId;
    private boolean completed;
    private LocalDateTime completedAt;
}
