package com.example.my_project.controller;

import com.example.my_project.models.Message;
import com.example.my_project.service.MessageService;
import com.example.my_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketController {
  private final MessageService messageService;
  private final UserService userService;
  private final SimpMessagingTemplate messagingTemplate;

  @Autowired
  public WebSocketController(MessageService messageService, UserService userService, SimpMessagingTemplate messagingTemplate) {
    this.messageService = messageService;
    this.userService = userService;
    this.messagingTemplate = messagingTemplate;
  }

  @MessageMapping("/channel/{channelId}/sendMessage")  
  public void handleMessage(Long channelId, String content, Principal principal) {
    Long userId = userService.findUser(principal.getName()).getId();
    boolean isInstructor = userService.isTeacher(principal.getName());

    messageService.sendMessage(channelId, userId, content, isInstructor);
  }

}