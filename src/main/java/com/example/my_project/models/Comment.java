package com.example.my_project.models;


import java.util.Set;
import lombok.Data;

@Data
public class Comment {
    private Long commentId;
    private Lecture lecture;
    private User user;
    private String commentContent;
    private Integer upvotes = 0;
    private Integer downvotes = 0;
    private Comment replyOf;
    private Set<Comment> replies;
}
