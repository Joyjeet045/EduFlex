package com.example.my_project.models;

import com.example.my_project.models.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Set;

import lombok.Data;
import lombok.Getter;

@Entity
@Data
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  @ManyToOne
  @JoinColumn(name = "lecture_id", nullable = false)
  private Lecture lecture;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;  

  @Lob
  @Column(nullable = false)
  private String commentContent; 

  @Column(nullable = false)
  private Integer upvotes = 0;  

  @Column(nullable = false)
  private Integer downvotes = 0; 

  @ManyToOne
  @JoinColumn(name = "reply_of", referencedColumnName = "commentId")
  private Comment replyOf;  
  
  @OneToMany(mappedBy = "replyOf", cascade = CascadeType.ALL)
  private Set<Comment> replies; 
}