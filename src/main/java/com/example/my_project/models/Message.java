package com.example.my_project.models;


import java.time.LocalDateTime;
import lombok.Data;


@Data
public class Message {
    private Long id;
    private ChatRoom chatRoom;
    private User sender;
    private String content;
    private LocalDateTime timestamp;
}
