package com.example.my_project.service;

import com.example.my_project.dao.MessageDao;
import com.example.my_project.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
  private final MessageDao messageDao;
  private final SimpMessagingTemplate messagingTemplate; 

  @Autowired
  public MessageService(MessageDao messageDao, SimpMessagingTemplate messagingTemplate) {
    this.messageDao = messageDao;
    this.messagingTemplate = messagingTemplate;
  }

  public void sendMessage(Long channelId, Long userId, String content, boolean isInstructor) {
    Message message = new Message();
    message.setChannelId(channelId);
    message.setUserId(userId);
    message.setContent(content);
    message.setInstructor(isInstructor);
    message.setTimestamp(LocalDateTime.now());

    messageDao.save(message);
    messagingTemplate.convertAndSend("/topic/channel"+channelId, message);
  }

  public List<Message> getMessagesByChannelId(Long channelId) {
    return messageDao.findByChannelId(channelId);
  }

}