package com.example.cms.controllers;

import com.example.my_project.models.ChatRoom;
import com.example.my_project.models.Message;
import com.example.my_project.models.User;
import com.example.my_project.service.ChatRoomService;
import com.example.my_project.service.MessageService;
import com.example.my_project.service.UserService;
import com.example.my_project.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatController {

    private ChatRoomService chatRoomService;
    private MessageService messageService;
    private final UserService userService;

    @Autowired
    public ChatController(ChatRoomService chatRoomService, MessageService messageService, UserService userService) {
        this.chatRoomService = chatRoomService;
        this.messageService = messageService;
        this.userService = userService; 
    }
    @GetMapping
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomService.getAllChatRooms();
    }

    @GetMapping("/course/{courseId}")
    public List<ChatRoom> getChatRoomsByCourseId(@PathVariable Long courseId) {
        return chatRoomService.getChatRoomsByCourseId(courseId);
    }

    @GetMapping("/{roomId}")
    public ChatRoom getChatRoomById(@PathVariable Long roomId) {
        return chatRoomService.getChatRoomById(roomId);
    }
    
    @PostMapping("/{courseId}")
    public ChatRoom createChatRoom(@PathVariable Long courseId, @RequestParam String name) {
        User creator = userService.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));
        return chatRoomService.createChatRoom(name, courseId, creator);
    }

    @DeleteMapping("/{roomId}")
    public String deleteChatRoom(@PathVariable Long roomId) {
        chatRoomService.deleteChatRoom(roomId);
        return "Chat room with ID " + roomId + " has been deleted.";
    }

    @MessageMapping("/chat/{roomId}/sendMessage")
    @SendTo("/topic/chat/{roomId}")
    public Message sendMessage(@DestinationVariable Long roomId, @Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(roomId);
        User sender = (User) headerAccessor.getSessionAttributes().get("user");
        message.setSender(sender);
        message.setChatRoom(chatRoom);
        messageService.saveMessage(message);
        return message;
    }

    @MessageMapping("/chat/{roomId}/addUser")
    @SendTo("/topic/chat/{roomId}")
    public String addUser(@DestinationVariable Long roomId, @Payload User user, SimpMessageHeaderAccessor headerAccessor) {
        ChatRoom chatRoom = chatRoomService.addUserToChatRoom(roomId, user.getId());
        headerAccessor.getSessionAttributes().put("user", user);
        headerAccessor.getSessionAttributes().put("room", chatRoom);
        return user.getUsername() + " joined the chat";
    }

    @MessageMapping("/chat/{roomId}/removeUser")
    @SendTo("/topic/chat/{roomId}")
    public String removeUser(@DestinationVariable Long roomId, @Payload User user) {
        ChatRoom chatRoom = chatRoomService.removeUserFromChatRoom(roomId, user.getId());
        return user.getUsername() + " left the chat";
    }
}
