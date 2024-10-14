package com.example.my_project.service;

import com.example.my_project.dao.*;
import com.example.my_project.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CommentService {

    private final CommentDao commentDao;

    @Autowired
    public CommentService(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public int saveComment(Comment comment) {
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis())); 
        return commentDao.saveComment(comment);
    }

    public List<Comment> getCommentsByLecture(Long lectureId) {
        return commentDao.getCommentsByLecture(lectureId);
    }

    public Comment findCommentById(Long id) {
        return commentDao.findCommentById(id);
    }

    public int deleteComment(Long id) {
        return commentDao.deleteComment(id);
    }

    public int likeComment(Long commentId) {
        commentDao.updateLikes(commentId, 1);
        return commentDao.getLikesCount(commentId);
    }

    public int dislikeComment(Long commentId) {
        commentDao.updateDislikes(commentId, 1);
        return commentDao.getDislikesCount(commentId);
    }

}
