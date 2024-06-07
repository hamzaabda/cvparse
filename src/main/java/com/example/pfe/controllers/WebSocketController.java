// WebSocketController.java (ajout d'un point de fin REST pour tester les notifications)
package com.example.pfe.controllers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/send-notification")
    public String sendNotification(@RequestParam String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
        return "Notification sent: " + message;
    }
}
