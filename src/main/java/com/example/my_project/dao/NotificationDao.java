package com.example.my_project.dao;

import com.example.my_project.models.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NotificationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Notification notification) {
        final String insertSql = "INSERT INTO notifications (book_id, user_id, message, approved, rejected) VALUES (?, ?, ?, ?, ?)";
        try {
            return jdbcTemplate.update(insertSql,
                notification.getBookId(),
                notification.getUserId(),
                notification.getMessage(),
                notification.isApproved(),
                notification.isRejected()
            );
        } catch (DataAccessException e) {
            System.err.println("Error saving notification: " + e.getMessage());
            throw new RuntimeException("Error saving notification to the database", e);
        }
    }
    public int update(Notification notification) {
        final String updateSql = "UPDATE notifications SET book_id = ?, user_id = ?, message = ?, approved = ?, rejected = ? WHERE id = ?";
        try {
            return jdbcTemplate.update(updateSql,
                notification.getBookId(),
                notification.getUserId(),
                notification.getMessage(),
                notification.isApproved(),
                notification.isRejected(),
                notification.getId() // Assuming you have a getter for id
            );
        } catch (DataAccessException e) {
            System.err.println("Error updating notification: " + e.getMessage());
            throw new RuntimeException("Error updating notification in the database", e);
        }
    }

    public List<Notification> findAll() {
        String sql = "SELECT * FROM notifications";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Notification.class));
        } catch (DataAccessException e) {
            System.err.println("Error fetching notifications: " + e.getMessage());
            throw new RuntimeException("Error fetching notifications from the database", e);
        }
    }

    public Notification findById(Long id) {
        String sql = "SELECT * FROM notifications WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Notification.class), id);
        } catch (DataAccessException e) {
            System.err.println("Error fetching notification by ID: " + e.getMessage());
            return null;
        }
    }

    public void markAsApproved(Long notificationId) {
        final String updateSql = "UPDATE notifications SET approved = true WHERE id = ?";
        try {
            jdbcTemplate.update(updateSql, notificationId);
        } catch (DataAccessException e) {
            System.err.println("Error approving notification: " + e.getMessage());
            throw new RuntimeException("Error approving notification in the database", e);
        }
    }

    public void markAsRejected(Long notificationId) {
        final String updateSql = "UPDATE notifications SET rejected = true WHERE id = ?";
        try {
            jdbcTemplate.update(updateSql, notificationId);
        } catch (DataAccessException e) {
            System.err.println("Error rejecting notification: " + e.getMessage());
            throw new RuntimeException("Error rejecting notification in the database", e);
        }
    }
    
    public List<Notification> findAllPendingNotifications() {
        String sql = "SELECT * FROM notifications WHERE approved = false AND rejected = false";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Notification.class));
        } catch (DataAccessException e) {
            System.out.println("Error fetching pending notifications: " + e.getMessage());
            throw new RuntimeException("Error fetching pending notifications", e);
        }
    }

}
