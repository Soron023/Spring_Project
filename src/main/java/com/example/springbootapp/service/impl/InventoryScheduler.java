package com.example.springbootapp.service.impl;

import com.example.springbootapp.entity.Product;
import com.example.springbootapp.entity.User;
import com.example.springbootapp.repository.ProductRepository;
import com.example.springbootapp.service.NotificationService;
import com.example.springbootapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryScheduler {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    // Runs every day at 8am
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkLowStock() {
        List<Product> lowStockProducts = productRepository.findAll().stream()
                .filter(p -> p.getStockQuantity() != null && p.getStockQuantity() < 10)
                .toList();
        if (!lowStockProducts.isEmpty()) {
            List<User> admins = userService.getAllUsers().stream()
                    .filter(u -> u.getRole().equals("ADMIN"))
                    .toList();
            for (Product product : lowStockProducts) {
                for (User admin : admins) {
                    notificationService.sendNotification(
                        admin,
                        "Low stock alert: " + product.getName() + " (" + product.getStockQuantity() + ")",
                        "INVENTORY"
                    );
                }
            }
        }
    }
} 