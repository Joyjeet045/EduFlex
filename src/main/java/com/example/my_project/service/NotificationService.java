package com.example.my_project.service;

import com.example.my_project.dao.*;
import com.example.my_project.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.sql.*;

@Service
public class NotificationService {
    private final NotificationDao notificationDao;
    private final IssueService issueService;
    private final BookService bookService;

    @Autowired
    public NotificationService(NotificationDao notificationDao,IssueService issueService,BookService bookService) {
      this.notificationDao = notificationDao;
      this.issueService = issueService;
      this.bookService= bookService;
    }

    public void createNotification(Notification notification) {
      notificationDao.save(notification); 
    }

    public List<Notification> getAllNotifications() {
      return notificationDao.findAll();
    }

    public void approveNotification(Long notificationId) {
        Notification notification = notificationDao.findById(notificationId);
        if (notification != null) {
          notification.setApproved(true);
          notificationDao.update(notification); 

          Issue issue = new Issue();
          issue.setBookId(notification.getBookId());
          issue.setUserId(notification.getUserId());
          issue.setIssueDate(new Timestamp(System.currentTimeMillis())); 

          issueService.createIssue(issue); 
          bookService.issueBook(notification.getBookId());
        }
    }

    public void denyNotification(Long notificationId) {
        Notification notification = notificationDao.findById(notificationId);
        if (notification != null) {
          notification.setRejected(true);
          notificationDao.save(notification); 
        }
    }
    public List<Notification> getPendingNotifications() {
        return notificationDao.findAllPendingNotifications();
    }
}
