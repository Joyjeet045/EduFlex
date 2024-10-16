package com.example.my_project.controllers;

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ChannelController {
    
    @Autowired
    private ChannelService channelService;

    @GetMapping("/community/chat/{courseId}")
    public String viewCommunity(@PathVariable Long courseId, Model model) {
        List<Channel> channels = channelService.getChannelsByCourseId(courseId);
        model.addAttribute("channels", channels);
        model.addAttribute("courseId", courseId);
        return "chat";  
    }
}
