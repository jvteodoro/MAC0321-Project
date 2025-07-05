package br.com.agendusp.agendusp.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventPollDataController;
import br.com.agendusp.agendusp.dataobjects.PollNotification;
import br.com.agendusp.agendusp.dataobjects.eventObjects.Attendee;
import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPoll;
import br.com.agendusp.agendusp.dataobjects.eventObjects.Notification;
import br.com.agendusp.agendusp.events.EventPollNotification;

@Component
public class InvitePollListener implements ApplicationListener<EventPollNotification> {
    @Autowired
    UserDataController userDataController;
    @Autowired
    EventPollDataController eventPoolDataController;
    @Autowired
    NotificationService notificationService;

    @Override
    public void onApplicationEvent(EventPollNotification event) {
        EventPoll evPoll = eventPoolDataController.getById(event.getEventPollId());
        ArrayList<Attendee> attendees = evPoll.getAttendees();
        PollNotification notification = new PollNotification(event);
        String organizerId = evPoll.getOwnerId();
        for (Attendee at: attendees){
            String userId = at.getCalendarPerson().getId();
            if (userId == null || userId.equals(organizerId)) continue; // skip organizer
            userDataController.addEventPollNotification(userId, notification);
            // Add to NotificationService for WebSocket and REST API
            notificationService.addNotification(new Notification(
                userId,
                "Você foi convidado a votar em uma enquete para o evento: " + evPoll.getEventId()
            ));
        }
    }
}
