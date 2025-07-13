package com.example.springbootapp.service;

import com.example.springbootapp.entity.Notification;
import com.example.springbootapp.entity.User;
import java.util.List;

public interface NotificationService {
    Notification sendNotification(User user, String message, String type);
    List<Notification> getUserNotifications(Long userId);
    void markAsRead(Long notificationId);
} 