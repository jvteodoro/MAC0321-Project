package br.com.agendusp.agendusp.controller.eventControllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agendusp.agendusp.dataobjects.DateTimeInterval;
import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPoll;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.events.EventPollDoneEvent;
import br.com.agendusp.agendusp.events.EventPollNotification;
import br.com.agendusp.agendusp.repositories.EventPollRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

public class EventPollDataController {

    @Autowired
    EventsDataController eventsDataController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EventPollRepository eventPollRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ApplicationEventPublisher applicationPublisher;

    public ArrayList<EventPoll> getAllEventPolls(ArrayList<String> eventPollIdList) {
        ArrayList<EventPoll> allEventPolls = new ArrayList<>();
        for (String id : eventPollIdList) {
            allEventPolls.add(eventPollRepository.findById(id).orElse(null));
        }
        return allEventPolls;
    }



    public EventPoll getById(String eventPollId){
        Optional<EventPoll> evPollOptional = eventPollRepository.findById(eventPollId);
        if (evPollOptional.isPresent()){
            return evPollOptional.get();
        } else {
            return null;
        }

    }

    public EventPoll create(String eventId,  String startDate, String endDate, String userId){
        EventsResource event = eventsDataController.getEventById(eventId);
        // Only organizer can create poll
        if (event.getOrganizer() == null || event.getOrganizer().getId() == null
            || !event.getOrganizer().getId().equals(userId)) {
            throw new IllegalArgumentException("Apenas o organizador pode criar uma enquete para este evento.");
        }
        EventPoll eventPoll = new EventPoll(event);

        System.out.println("[DEBUG] 1");
        DateTimeInterval dateTimeInterval = new DateTimeInterval();
        dateTimeInterval.setStart(LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME));
        dateTimeInterval.setEnd(LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME));

        System.out.println("[DEBUG] 2");
        ArrayList<DateTimeInterval> initialFreeTime = new ArrayList<>();
        initialFreeTime.add(dateTimeInterval);

        // Pegar todos os eventos no intervalo
        ArrayList<EventsResource> allEvents = eventsDataController.getEventsOnInterval(userId, dateTimeInterval);
        System.out.println("[DEBUG] 3");
        try {
           System.out.println("Free Time: "+objectMapper.writeValueAsString(initialFreeTime));
        } catch (Exception e){}
        
        for (EventsResource ev : allEvents) {
            try {
            System.out.println("Event :"+objectMapper.writeValueAsString(ev));
            } catch (Exception e){}
            initialFreeTime = ev.freeTime(initialFreeTime);
            System.out.println("[DEBUG] A");
        }
        eventPoll.setPossibleTimesFromDateTimeIntervalList(initialFreeTime);
        System.out.println("[DEBUG] 4");

        String ownerId = eventPoll.getOwnerId();
        userRepository.addEventPoll(ownerId, eventPoll.getId());
        eventPollRepository.insert(eventPoll);
        try {
            System.out.println("CreatedPoll: " + objectMapper.writeValueAsString(eventPoll));
        } catch (Exception w) {
        }

        String message = "Você foi convidado a votar em uma enquete de evento!";
        EventPollNotification notification = new EventPollNotification(this, eventPoll.getId(), message);
        applicationPublisher.publishEvent(notification);

        return eventPoll;
    }
    public EventPoll vote(String eventPollId, int dateTimeIntervalId) {
        Optional<EventPoll> evPoll = eventPollRepository.findById(eventPollId);
        if (evPoll.isPresent()) {
            evPoll.get().vote(dateTimeIntervalId);
            evPoll.get().getDone();
            String message = "Voto novo  no horário "+dateTimeIntervalId;
            // EventPollNotification notification = new EventPollNotification(this, eventPollId, message);
            // applicationPublisher.publishEvent(notification);
            eventPollRepository.save(evPoll.get());
            if (evPoll.get().getDone() == 0){
                System.err.println("Event done!");
                EventPollDoneEvent pollDone = new EventPollDoneEvent(this, evPoll.get().getEventId());
                applicationPublisher.publishEvent(pollDone);
            }
            return evPoll.get();
        } else {
            return new EventPoll();
        }

    }

    public EventPoll create(String eventId,  String startDate, String endDate){
        throw new UnsupportedOperationException("Use create(eventId, startDate, endDate, userId) instead.");
    }
}
