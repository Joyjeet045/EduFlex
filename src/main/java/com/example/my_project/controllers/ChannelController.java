package com.example.my_project.controllers;

import com.example.my_project.models.*;
import com.example.my_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.security.Principal;

@Controller
public class ChannelController {

    private ChannelService channelService;
    private UserService userService;

    @Autowired
    public ChannelController(UserService userService, ChannelService channelService) {
        this.channelService = channelService;
        this.userService = userService;
    }


    @GetMapping("/community/chat/{courseId}")
    public String viewCommunity(@PathVariable Long courseId, Model model,Principal principal) {
        String username = principal.getName();
        User user=userService.findUser(username);
        
        boolean isTeacher=(user.getRole().equals(UserRole.TEACHER));
        boolean isStudent=(user.getRole().equals(UserRole.STUDENT));
        List<Channel> channels = channelService.getChannelsByCourseId(courseId);
        model.addAttribute("channels", channels);
        model.addAttribute("courseId", courseId);
        model.addAttribute("isTeacher", isTeacher); 
        model.addAttribute("isStudent", isStudent); 
        return "chat";  
    }
}
