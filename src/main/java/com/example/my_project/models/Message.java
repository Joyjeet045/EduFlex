package com.example.my_project.models;

import javax.persistence.*;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Getter
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "chatroom_id", nullable = false)
  private ChatRoom chatRoom;

  @ManyToOne
  @JoinColumn(name = "sender_id", nullable = false)
  private User sender;

  @Column(nullable = false, length = 1000)
  private String content;

  @Column(name = "sent_at", nullable = false)
  private LocalDateTime sentAt;

  @PrePersist
  protected void onCreate() {
    sentAt = LocalDateTime.now();
  }
}