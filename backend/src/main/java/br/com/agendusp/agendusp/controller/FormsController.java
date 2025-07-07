package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agendusp.agendusp.controller.eventControllers.EventPollDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.dataobjects.DateTimeIntervalPoll;
import br.com.agendusp.agendusp.dataobjects.PollNotification;
import br.com.agendusp.agendusp.dataobjects.eventObjects.EventDate;
import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPoll;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.events.EventPollVoteEvent;
import br.com.agendusp.agendusp.repositories.EventPollRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

@RestController
public class FormsController {

    @Autowired
    EventsDataController eventsDataController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDataController userDataController;
    @Autowired
    EventPollRepository eventPollRepository;
    @Autowired
    SimpMessagingTemplate msgTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    EventPollDataController eventPollDataController;
    @Autowired
    ApplicationEventPublisher applicationPublisher;


    @MessageMapping("/poll/send/{eventPollId}")
    public void sendPoll(@PathVariable String eventPollId) {
        // No-op or remove this method if not needed, as notifications should go through
        // NotificationService
    }

    @MessageMapping("/poll/vote/{eventPollId}")
    public void voteEventPoll(@PathVariable String eventPollId, @RequestParam int dateTimeIntervalId) {
        // No-op or remove this method if not needed, as notifications should go through
        // NotificationService
    }

    @MessageMapping("/poll/create/{eventPollId}/{dateTimeIntervalId}")
    public void createEventFromPoll(@PathVariable String eventPollId,
            @PathVariable int dateTimeIntervalId, @RequestParam String userId) {

        this.createEvent(eventPollId, dateTimeIntervalId, userId);
        eventPollRepository.findById(eventPollId);
    }

    @GetMapping("/poll/create")
    public EventPoll createPoll(@RequestParam String eventId, @RequestParam String startDate,
            @RequestParam String endDate,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {

        String userId = authorizedClient.getPrincipalName();
        try {
            EventPoll poll = eventPollDataController.create(eventId, startDate, endDate, userId);
            // sendPoll(poll.getId()); // REMOVE this line, handled by NotificationService
            return poll;
        } catch (IllegalArgumentException ex) {
            // Return an empty poll with error message (or handle as preferred)
            // EventPoll errorPoll = new EventPoll();
            // Optionally, set a field or log the error
            throw ex;
        }
    }

    @PostMapping("/poll/vote")
    public EventPoll vote(@RequestParam String eventPollId, @RequestParam int dateTimeIntervalId, @RequestParam String userId) {
        Optional<EventPoll> evPoll = eventPollRepository.findById(eventPollId);

        if (evPoll.isPresent()) {
            System.out.println("Votando!");
            eventPollDataController.vote(eventPollId, dateTimeIntervalId);
            System.out.println("Votado!");
            User user = userDataController.findUser(userId);
            ArrayList<PollNotification> not = user.getEventPollNotifications();
            Optional<PollNotification> toRemove = (not.stream().filter(p -> p.getEventPollId().equals(eventPollId)).findFirst());
            for (PollNotification pollNot: not){
                if (pollNot.getEventPollId().equals(eventPollId)){
                //    not.remove(pollNot);
                }
            }
            user.setEventPollNotifications(not);
            System.out.println("New user:");
            try {
            System.out.println(objectMapper.writeValueAsString(user.getEventPollNotifications()));
            } catch (Exception e){}
            userRepository.save(user);
            
            // evPoll.get().vote(dateTimeIntervalId);
            // REMOVE WebSocket notification here, handled by NotificationService
            
            EventPollVoteEvent vote = new EventPollVoteEvent(this, userId, eventPollId);
            applicationPublisher.publishEvent(vote);
            return evPoll.get();
        } else {
            return new EventPoll();
        }

    }

    @PostMapping("/poll/createEvent")
    public EventsResource createEvent(@RequestParam String eventPollId, @RequestParam int dateTimeIntervalId, @RequestParam String userId) {
        Optional<EventPoll> evPoll = eventPollRepository.findById(eventPollId);
        DateTimeIntervalPoll selected;
        if (evPoll.isPresent()) {
            // Continuar a criação de eventos
            ArrayList<DateTimeIntervalPoll> dt = evPoll.get().getPosibleTimes();
            EventsResource event = eventsDataController.getEventById(evPoll.get().getEventId());
            for (DateTimeIntervalPoll dtP : dt) {
                if (dtP.getId() == dateTimeIntervalId) {
                    selected = dtP;
                    EventDate start = new EventDate(selected.getDateTimeInterval().getStart());
                    EventDate end = new EventDate(selected.getDateTimeInterval().getEnd());
                    event.setStart(start);
                    event.setEnd(end);
                }
            }
            EventPollVoteEvent vote = new EventPollVoteEvent(this, userId, eventPollId);
            applicationPublisher.publishEvent(vote);
            return eventsDataController.updateByObject(event);
        }
    return new EventsResource();
}

    @GetMapping("/poll/byEvent")
    public EventPoll getPollByEvent(@RequestParam String eventId) {
        return eventPollRepository.findByEventId(eventId).orElse(null);
    }
}
