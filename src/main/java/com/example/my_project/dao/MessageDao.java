package com.example.my_project.dao;

import com.example.my_project.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public class MessageDao {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public MessageDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  public int save(Message message) {
    final String insertSql = "INSERT INTO messages (channel_id, user_id, content, is_instructor, timestamp) VALUES (?, ?, ?, ?, ?)";
    try {
      return jdbcTemplate.update(insertSql, message.getChannelId(), message.getUserId(), message.getContent(), message.isInstructor(), message.getTimestamp());
    } catch (DataAccessException e) {
      System.out.println("Error saving message: " + e.getMessage());
      throw new RuntimeException("Error saving message to the database", e);
    }
  }
  
  public List<Message> findByChannelId(Long channelId) {
    String sql = "SELECT * FROM messages WHERE channel_id = ?";
    try {
      return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Message.class), channelId);
    } catch (DataAccessException e) {
      System.out.println("Error fetching messages by channel ID: " + e.getMessage());
      throw new RuntimeException("Error fetching messages by channel ID", e);
    }
  }

}

