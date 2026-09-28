package com.globalco.controller;

import com.globalco.services.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private EmailNotificationService emailNotificationService;

    @GetMapping("/sent")
    public String NotificationController() throws Exception {
//        emailNotificationService.sendStatusChangedEmail();
        return "email sent";
    }
}
