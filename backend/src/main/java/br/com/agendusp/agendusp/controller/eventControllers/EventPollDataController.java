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
import br.com.agendusp.agendusp.events.EventPollNotification;
import br.com.agendusp.agendusp.repositories.EventPoolRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

public class EventPollDataController {

    @Autowired
    EventsDataController eventsDataController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EventPoolRepository eventPoolRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ApplicationEventPublisher applicationPublisher;

    public ArrayList<EventPoll> getAllEventPools(ArrayList<String> eventPoolIdList) {
        // Verifica se a lista de IDs está vazia
        if (eventPoolIdList == null || eventPoolIdList.isEmpty()) {
            throw new IllegalArgumentException("A lista de IDs de EventPool não pode ser nula ou vazia.");
        }
        ArrayList<EventPoll> allEventPools = new ArrayList<>();
        for (String id : eventPoolIdList) {
            allEventPools.add(eventPoolRepository.findById(id).orElse(null));
        }
        return allEventPools;
    }
    public EventPoll getById(String eventPollId){
        Optional<EventPoll> evPollOptional = eventPoolRepository.findById(eventPollId);
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
        EventPoll eventPool = new EventPoll(event);

        System.out.println("[DEBUG] 1");
        DateTimeInterval dateTimeInterval = new DateTimeInterval();
        dateTimeInterval.setStart(LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME));
        dateTimeInterval.setEnd(LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME));

        System.out.println("[DEBUG] 2");
        ArrayList<DateTimeInterval> initialFreeTime = new ArrayList<>();
        initialFreeTime.add(dateTimeInterval);

        // Pegar todos os eventos no intervalo
        ArrayList<EventsResource> allEvents = eventsDataController.getEventsOnInterval(dateTimeInterval);
        System.out.println("[DEBUG] 3");
        for (EventsResource ev : allEvents) {
            initialFreeTime = ev.freeTime(initialFreeTime);
            System.out.println("[DEBUG] A");
        }
        eventPool.setPossibleTimesFromDateTimeIntervalList(initialFreeTime);
        System.out.println("[DEBUG] 4");

        String ownerId = eventPool.getOwnerId();
        userRepository.addEventPool(ownerId, eventPool.getId());
        eventPoolRepository.insert(eventPool);
        try {
            System.out.println("CreatedPool: " + objectMapper.writeValueAsString(eventPool));
        } catch (Exception w) {
        }

        String message = "Você foi convidado a votar em uma enquete de evento!";
        EventPollNotification notification = new EventPollNotification(this, eventPool.getId(), message);
        applicationPublisher.publishEvent(notification);

        return eventPool;
    }

    public EventPoll create(String eventId,  String startDate, String endDate){
        throw new UnsupportedOperationException("Use create(eventId, startDate, endDate, userId) instead.");
    }
}
