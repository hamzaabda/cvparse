package com.example.pfe.services;

import com.example.pfe.models.Notification;

import com.example.pfe.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setDateTime(LocalDateTime.now());
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    // Méthode pour marquer une notification comme lue
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow();
        notification.setRead(true);
        notificationRepository.save(notification);
    }
    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

}
