package br.com.agendusp.agendusp.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agendusp.agendusp.dataobjects.DateTimeInterval;
import br.com.agendusp.agendusp.dataobjects.DateTimeIntervalPool;
import br.com.agendusp.agendusp.dataobjects.EventDate;
import br.com.agendusp.agendusp.dataobjects.EventPool;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.repositories.EventPoolRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;



@RestController
public class FormsController {
    @Autowired
    EventsDataController eventsDataController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EventPoolRepository eventPoolRepository;
    @Autowired
    SimpMessagingTemplate msgTemplate;
    @Autowired
    ObjectMapper objectMapper;
    

    @MessageMapping("/pool/send/{eventPoolId}")
    public void sendPool(@PathVariable String eventPoolId){
        Optional<EventPool> eventPoolOptional = eventPoolRepository.findById(eventPoolId);
        if (eventPoolOptional.isPresent()){
            EventPool eventPool = eventPoolRepository.save(eventPoolOptional.get());
            String destination = "/notify/pool/"+eventPoolId;
            msgTemplate.convertAndSend(destination, eventPool);
        }
    }

    @MessageMapping("/pool/vote/{eventPoolId}")
    public void voteEventPool(@PathVariable String eventPoolId, @RequestParam String dateTimeIntervalId){
        String destination = "/notify/pool/"+eventPoolId;
         msgTemplate.convertAndSend(destination, this.vote(eventPoolId, dateTimeIntervalId));
    }


    @MessageMapping("/pool/create/{eventPoolId}/{dateTimeIntervalId}")
    public void createEventFromPool(@PathVariable String eventPoolId, 
    @PathVariable String dateTimeIntervalId){

        this.createEvent(eventPoolId, dateTimeIntervalId);
        eventPoolRepository.findById(eventPoolId);
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
        ArrayList<EventsResource> allEvents = eventsDataController.getEventsOnInterval(endDate, null);
        for (EventsResource ev: allEvents){
            initialFreeTime = ev.freeTime(initialFreeTime);
        }
        eventPool.setPossibleTimesFromDateTimeIntervalList(initialFreeTime);
       
        String ownerId = eventPool.getOwnerId();
        userRepository.addEventPool(ownerId, eventPool);
        eventPoolRepository.insert(eventPool);
        try {
        System.out.println("CreatedPool: "+objectMapper.writeValueAsString(eventPool));}
        catch (Exception w){}
        return eventPool;
    }

    @PostMapping("/pool/vote")
    public EventPool vote(@RequestParam String eventPoolId, @RequestParam String dateTimeIntervalId ){
        Optional<EventPool> evPool = eventPoolRepository.findById(eventPoolId);
        if (evPool.isPresent()){
            evPool.get().vote(dateTimeIntervalId);
            evPool.get().getDone();
            return evPool.get();
        } else {
            return new EventPool();
        }

    }


    @PostMapping("/pool/createEvent")
    public EventsResource createEvent(@RequestParam String eventPoolId, @RequestParam String dateTimeIntervalId){
        Optional<EventPool> evPool = eventPoolRepository.findById(eventPoolId);
        DateTimeIntervalPool selected;
        if (evPool.isPresent()){
            // Continuar a criação de eventos
            ArrayList<DateTimeIntervalPool> dt = evPool.get().getPosibleTimes();
            EventsResource event = eventsDataController.getEventById(evPool.get().getEventId());
            for (DateTimeIntervalPool dtP: dt){
                if (dtP.getId() == dateTimeIntervalId){
                    selected = dtP;
                    EventDate start = new EventDate(selected.getDateTimeInterval().getStart());
                    EventDate end = new EventDate(selected.getDateTimeInterval().getEnd());
                    event.setStart(start);
                    event.setEnd(end);
                }
            }

            return eventsDataController.updateByObject(event);
        }
        return new EventsResource();
    }
}
