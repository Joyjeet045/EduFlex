package com.example.my_project.models;


import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ChatRoom {
    private Long id;
    private String name;
    private Course course;
    private List<User> participants;
    private LocalDateTime createdAt;
    private String websocketTopic;

    public void setDefaultValues() {
        this.createdAt = LocalDateTime.now();
        this.websocketTopic = "chat_" + System.currentTimeMillis();  // Unique topic
    }
}
