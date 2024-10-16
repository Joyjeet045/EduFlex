package com.example.my_project.dao;

import com.example.my_project.models.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChannelDao {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public ChannelDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  public int save(Channel channel) {
    final String insertSql = "INSERT INTO channels (course_id, name) VALUES (?, ?)";
    try {
      return jdbcTemplate.update(insertSql, channel.getCourseId(), channel.getName());
    } catch (DataAccessException e) {
      System.out.println("Error saving channel: " + e.getMessage());
      throw new RuntimeException("Error saving channel to the database", e);
    }
  }
  public List<Channel> findByCourseId(Long courseId) {
    String sql = "SELECT * FROM channels WHERE course_id = ?";
    try {
      return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Channel.class), courseId);
    } catch (DataAccessException e) {
      System.out.println("Error fetching channels by course ID: " + e.getMessage());
      throw new RuntimeException("Error fetching channels by course ID", e);
    }
  }
}
