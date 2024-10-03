package com.example.my_project.dao;

import com.example.my_project.models.ChatRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatRoomDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatRoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addChatRoom(ChatRoom chatRoom) {
        String sql = "INSERT INTO chatrooms(course_id, name, websocket_topic, created_at, updated_at) VALUES(?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
            chatRoom.getCourse().getId(),
            chatRoom.getName(),
            chatRoom.getWebsocketTopic(),
            chatRoom.getCreatedAt()
        );
    }

    public List<ChatRoom> findChatRoomsByCourseId(Long courseId) {
        String sql = "SELECT * FROM chatrooms WHERE course_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ChatRoom.class), courseId);
    }

    public ChatRoom findChatRoomById(Long chatRoomId) {
        String sql = "SELECT * FROM chatrooms WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ChatRoom.class), chatRoomId);
    }

    public List<ChatRoom> findAllChatRooms() {
        String sql = "SELECT * FROM chatrooms";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ChatRoom.class));
    }

    public int addUserToChatRoom(Long chatRoomId, Long userId) {
        String sql = "INSERT INTO chatroom_participants(chatroom_id, user_id) VALUES(?, ?)";
        return jdbcTemplate.update(sql, chatRoomId, userId);
    }

    public int removeUserFromChatRoom(Long chatRoomId, Long userId) {
        String sql = "DELETE FROM chatroom_participants WHERE chatroom_id = ? AND user_id = ?";
        return jdbcTemplate.update(sql, chatRoomId, userId);
    }

    public int deleteChatRoom(Long chatRoomId) {
        String sql = "DELETE FROM chatrooms WHERE id = ?";
        return jdbcTemplate.update(sql, chatRoomId);
    }
}
