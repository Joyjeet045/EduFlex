package com.example.my_project.controllers;

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Controller
public class MessageController {

    private final MessageService messageService;
    private final ChannelService channelService;

    @Autowired
    public MessageController(MessageService messageService,ChannelService channelService) {
        this.messageService = messageService;
        this.channelService = channelService;
    }

    @GetMapping("/api/messages/{channelId}")
    public ResponseEntity<HashMap<String, Object>> getMessagesForChannel(@PathVariable Long channelId) {
        System.out.println(channelId);
        List<Message> messages = messageService.getMessagesByChannelId(channelId);
        Channel channel=channelService.getChannelById(channelId);
        System.out.println(channel);
        HashMap<String, Object> response = new HashMap<>();
        response.put("channel", channel);
        response.put("messages", messages);
        return ResponseEntity.ok(response);
    }
}
