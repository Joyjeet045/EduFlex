package com.example.my_project.dao;

import com.example.my_project.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MessageDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Message message) {
        String sql = "INSERT INTO messages (chatroom_id, sender_id, content, timestamp) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, 
            message.getChatRoom().getId(), 
            message.getSender().getId(), 
            message.getContent(), 
            message.getTimestamp()
        );
    }

    public List<Message> findByChatRoomId(Long chatRoomId) {
        String sql = "SELECT * FROM messages WHERE chatroom_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Message.class), chatRoomId);
    }

    public int deleteById(Long messageId) {
        String sql = "DELETE FROM messages WHERE id = ?";
        return jdbcTemplate.update(sql, messageId);
    }
}
