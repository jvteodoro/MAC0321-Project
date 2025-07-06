package br.com.agendusp.agendusp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.dataobjects.PollNotification;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.events.EventPollDoneEvent;
@Component
public class EventPollDoneEventListener{

    @Autowired
    UserDataController userDataController;
    @Autowired
    EventsDataController eventsDataController;

    @EventListener
    public void onApplicationEvent(EventPollDoneEvent event) {
        System.err.println("EVento finalizado ouvido!");
        EventsResource eventResource = eventsDataController.getEventById(event.getEventPollId());
        String userId = eventResource.getOrganizer().getId();
        String message = "Votação finalizada! Crie o evento";
        PollNotification notification = new PollNotification(this, userId, eventResource.getId(), message, "pollDone");
        userDataController.addEventPollNotification(userId, notification);
    }

}
