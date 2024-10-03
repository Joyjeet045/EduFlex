package com.example.my_project.service;

import com.example.my_project.dao.CommentDao;
import com.example.my_project.models.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;
    public CommentService(CommentDao commentDao) {
      this.commentDao = commentDao;
    }
    public boolean addComment(Comment comment) {
      return commentDao.saveComment(comment) > 0;
    }
    public List<Comment> getCommentsByLecture(Long lectureId) {
      return commentDao.findByLectureId(lectureId);
    }
}
