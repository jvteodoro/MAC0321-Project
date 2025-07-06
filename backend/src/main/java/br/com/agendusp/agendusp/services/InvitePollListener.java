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
import br.com.agendusp.agendusp.events.EventPollNotification;

@Component
public class InvitePollListener implements ApplicationListener<EventPollNotification> {
    @Autowired
    UserDataController userDataController;
    @Autowired
    EventPollDataController eventPollDataController;
    @Autowired
    NotificationService notificationService;

    @Override
    public void onApplicationEvent(EventPollNotification event) {
        EventPoll evPoll = eventPollDataController.getById(event.getEventPollId());
        ArrayList<Attendee> attendees = evPoll.getAttendees();
        String organizerId = evPoll.getOwnerId();
        String type = "invitePoll";
        String message = "Nova enquete criada! Não se esqueça de votar";
        
        for (Attendee at: attendees){
            String userId = at.getCalendarPerson().getId();
            if (userId == null || userId.equals(organizerId)) continue; // skip organizer
            PollNotification notification = new PollNotification(userId, evPoll.getEventId(), message, type);
            userDataController.addEventPollNotification(userId, notification);
            // Add to NotificationService for WebSocket and REST API
            notificationService.addNotification(notification);
        }
    }
}
