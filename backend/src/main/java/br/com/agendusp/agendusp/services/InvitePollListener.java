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
public class InvitePollListener {
    @Autowired
    UserDataController userDataController;
    @Autowired
    EventPollDataController eventPoolDataController;

    public void onApplicationEvent(EventPollNotification event){
        EventPoll evPoll = eventPoolDataController.getById(event.getEventPollId());
        ArrayList<Attendee> attendees = evPoll.getAttendees();
        PollNotification notification = new PollNotification(event);
        for (Attendee at: attendees){
            String userId = at.getCalendarPerson().getId();
            userDataController.addEventPollNotification(userId, notification);            
        }
    }
}
