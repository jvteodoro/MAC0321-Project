package br.com.agendusp.agendusp.controller;

import br.com.agendusp.agendusp.dataobjects.PollNotification;
import br.com.agendusp.agendusp.dataobjects.eventObjects.Notification;
import br.com.agendusp.agendusp.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{userId}")
    public List<PollNotification> getNotifications(@PathVariable String userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    @GetMapping("/me")
    public List<PollNotification> getMyNotifications(
            Authentication authentication,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient
    ) {
        String userId = authorizedClient != null ? authorizedClient.getPrincipalName() : null;
        if (userId == null) {
            throw new RuntimeException("Usuário não autenticado");
        }
        List<PollNotification> notifications = notificationService.getNotificationsForUser(userId);
        return notifications;
    }
}
