package com.example.my_project.service;

import com.example.my_project.dao.MessageDao;
import com.example.my_project.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {
    private final MessageDao messageDao;
    @Autowired
    public MessageService(MessageDao messageDao) {
      this.messageDao = messageDao;
    }
    public List<Message> getMessagesByChatRoomId(Long chatRoomId) {
      return messageDao.findByChatRoomId(chatRoomId);
    }

    public int saveMessage(Message message) {
      return messageDao.save(message);
    }
    public void deleteMessage(Long messageId) {
      messageDao.deleteById(messageId);
    }
}
