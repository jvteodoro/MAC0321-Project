package br.com.agendusp.agendusp.services;

import br.com.agendusp.agendusp.dataobjects.eventObjects.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final List<Notification> notifications = new ArrayList<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void addNotification(Notification notification) {
        // Prevent duplicate notifications for the same user and message
        boolean exists = notifications.stream()
            .anyMatch(n -> n.getUserId().equals(notification.getUserId())
                        && n.getMessage().equals(notification.getMessage()));
        if (!exists) {
            notifications.add(notification);
            // Send notification to the specific user via WebSocket
            messagingTemplate.convertAndSendToUser(
                    notification.getUserId(),
                    "/queue/notifications",
                    notification);
        }
    }

    public List<Notification> getNotificationsForUser(String userId) {
        return notifications.stream()
                .filter(n -> n.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
