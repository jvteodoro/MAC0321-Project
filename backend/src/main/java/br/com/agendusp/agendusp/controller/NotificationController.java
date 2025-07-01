package br.com.agendusp.agendusp.controller;
package br.com.agendusp.agendusp.controller;

import br.com.agendusp.agendusp.dataobjects.eventObjects.Notification;
import br.com.agendusp.agendusp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{userId}")
    public List<Notification> getNotifications(@PathVariable String userId) {
        return notificationService.getNotificationsForUser(userId);
    }
}

