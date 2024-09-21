package com.example.my_project.service;

import com.example.my_project.dao.ChatRoomDao;
import com.example.my_project.models.ChatRoom;
import com.example.my_project.models.Course;
import com.example.my_project.models.User;
import com.example.my_project.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatRoomService {
    private final ChatRoomDao chatRoomDao;
    private final CourseService courseService;
    @Autowired
    public ChatRoomService(ChatRoomDao chatRoomDao, CourseService courseService) {
      this.chatRoomDao = chatRoomDao;
      this.courseService = courseService;
    }
    public ChatRoom createChatRoom(String name, Long courseId, User creator) {
      Course course = courseService.findCourseById(courseId);
      ChatRoom chatRoom = new ChatRoom();
      chatRoom.setName(name);
      chatRoom.setCourse(course);
      chatRoom.setWebsocketTopic("/topic/chat/" + courseId); 
      chatRoom.setCreatedAt(LocalDateTime.now());
      chatRoomDao.addChatRoom(chatRoom);
      addUserToChatRoom(chatRoom.getId(), creator.getId()); 
      return chatRoom;
    }
    public ChatRoom getChatRoomById(Long id) {
      return chatRoomDao.findChatRoomById(id);
    }
    public List<ChatRoom> getAllChatRooms() {
      return chatRoomDao.findAllChatRooms();
    }
    public List<ChatRoom> getChatRoomsByCourseId(Long courseId) {
      return chatRoomDao.findChatRoomsByCourseId(courseId);
    }
    public ChatRoom addUserToChatRoom(Long chatRoomId, Long userId) {
      chatRoomDao.addUserToChatRoom(chatRoomId, userId);
      return getChatRoomById(chatRoomId);
    }
    public ChatRoom removeUserFromChatRoom(Long chatRoomId, Long userId) {
      chatRoomDao.removeUserFromChatRoom(chatRoomId, userId);
      return getChatRoomById(chatRoomId);
    }
    public void deleteChatRoom(Long id) {
      chatRoomDao.deleteChatRoom(id);
    }
}
