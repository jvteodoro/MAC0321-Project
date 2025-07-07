package br.com.agendusp.agendusp.services;

import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.dataobjects.PollNotification;
import br.com.agendusp.agendusp.dataobjects.eventObjects.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    private final List<Notification> notifications = new ArrayList<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserDataController userDataController;

    public void addNotification(Notification notification) {
        // Prevent duplicate notifications for the same user and message
        boolean exists = notifications.stream()
            .anyMatch(n -> n.getId().equals(notification.getId()));
        if (!exists) {
            notifications.add(notification);
            // Send notification to the specific user via WebSocket
            System.out.println("[NotificationService] Sending notification to user: " + notification.getUserId());
            messagingTemplate.convertAndSendToUser(
                    notification.getUserId(),
                    "/queue/notifications",
                    notification);
        }
    }

    public ArrayList<PollNotification> getNotificationsForUser(String userId) {
        return userDataController.findUser(userId).getEventPollNotifications();
    }
}
