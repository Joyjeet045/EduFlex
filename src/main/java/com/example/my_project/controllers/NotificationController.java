package com.example.my_project.controllers;

import com.example.my_project.models.Notification;
import com.example.my_project.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.security.Principal;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/approve/{id}")
    public String approveNotification(@PathVariable Long id, Model model) {
        notificationService.approveNotification(id);
        model.addAttribute("message", "Notification approved successfully!");
        return "redirect:/dashboard";
    }

    @PostMapping("/deny/{id}")
    public String denyNotification(@PathVariable Long id, Model model) {
        notificationService.denyNotification(id);
        model.addAttribute("message", "Notification denied successfully!");
        return "redirect:/dashboard"; 
    }
}
