package com.example.springbootapp.service.impl;

import com.example.springbootapp.entity.Notification;
import com.example.springbootapp.entity.User;
import com.example.springbootapp.repository.NotificationRepository;
import com.example.springbootapp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification sendNotification(User user, String message, String type) {
        Notification notification = new Notification(user, message, type);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findAll().stream()
                .filter(n -> n.getUser() != null && n.getUser().getId().equals(userId))
                .toList();
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }
} 