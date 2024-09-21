package com.example.my_project.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Entity
@Data

public class ChatRoom {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  private Course course;

  @ManyToMany
  @JoinTable(
      name = "chatroom_participants",
      joinColumns = @JoinColumn(name = "chatroom_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private List<User> participants;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "websocket_topic", nullable = false, unique = true)
  private String websocketTopic;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    websocketTopic = "chat_" + System.currentTimeMillis(); // Unique
  }
}