package br.com.agendusp.agendusp.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.agendusp.agendusp.dataobjects.Attendee;
import br.com.agendusp.agendusp.dataobjects.DateTimeInterval;
import br.com.agendusp.agendusp.dataobjects.EventPool;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.repositories.EventPoolRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;



@RestController
public class FormsController {
    @Autowired
    EventsController eventsController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EventPoolRepository eventPoolRepository;
    
    @PostMapping("/pool/sendPool")
    public EventPool sendPool(@RequestParam String userId, @RequestParam String eventPoolId){
        EventPool eventPool = userRepository.findByEventPoolId(userId, eventPoolId);
        ArrayList<Attendee> attendees =  eventPool.getAttendees();
        for (Attendee at: attendees ){
            String attendeeId = at.getCalendarPerson().getId();
            userRepository.addEventPoolNotification(attendeeId, eventPool);
        }
        return eventPool;
    }

    @PostMapping("/pool/create")
    public EventPool createPool(@RequestParam String startDate, 
    @RequestParam String endDate, @RequestBody EventsResource event,
    @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient){
        EventPool eventPool = new EventPool(event);

        DateTimeInterval dateTimeInterval = new DateTimeInterval();
        dateTimeInterval.setStart(LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME));
        dateTimeInterval.setEnd(LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME));
      
        ArrayList<DateTimeInterval> initialFreeTime = new ArrayList<>();
        initialFreeTime.add(dateTimeInterval);


        //Pegar todos os eventos no intervalo
        eventPool.setPossibleTimesFromDateTimeIntervalList(event.freeTime(initialFreeTime));
        String ownerId = eventPool.getOwnerId();
        userRepository.addEventPool(ownerId, eventPool);
        return eventPool;
    }

    @PostMapping("/pool/vote")
    public void vote(@RequestParam String eventPoolId, @RequestParam String dateTimeIntervalId ){
        Optional<EventPool> evPool = eventPoolRepository.findById(eventPoolId);
        if (evPool.isPresent()){
            evPool.get().vote(dateTimeIntervalId);
            evPool.get().getDone();
        }

    }

    @PostMapping("/pool/createEvent")
    public void createEvent(@RequestParam String eventPoolId, @RequestParam String dateTimeIntervalId){
        Optional<EventPool> evPool = eventPoolRepository.findById(eventPoolId);
        if (evPool.isPresent()){
            // Continuar a ciraçõa de eventos
            DateTimeInterval dt = evPool.get().getPosibleTimes();
        }
        
    }
}
