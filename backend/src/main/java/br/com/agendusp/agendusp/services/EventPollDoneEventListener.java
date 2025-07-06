package br.com.agendusp.agendusp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.dataobjects.PollNotification;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.events.EventPollDoneEvent;
import br.com.agendusp.agendusp.events.EventPollNotification;

public class EventPollDoneEventListener implements ApplicationListener<EventPollDoneEvent> {

    @Autowired
    UserDataController userDataController;
    @Autowired
    EventsDataController eventsDataController;

    @Override
    public void onApplicationEvent(EventPollDoneEvent event) {
        EventsResource eventResource = eventsDataController.getEventById(event.getEventPollId());
        String userId = eventResource.getOrganizer().getId();
        String message = "Votação finalizada! Crie o evento";
        PollNotification notification = new PollNotification(userId, eventResource.getId(), message, "pollDone");
        userDataController.addEventPollNotification(userId, notification);
    }

}
