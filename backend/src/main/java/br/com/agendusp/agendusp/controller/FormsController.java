package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.agendusp.agendusp.dataobjects.Attendee;
import br.com.agendusp.agendusp.dataobjects.EventPool;
import br.com.agendusp.agendusp.repositories.UserRepository;



@RestController
public class FormsController {

    @Autowired
    EventsController eventsController;
    @Autowired
    UserRepository userRepository;
    
    @PostMapping("/pool/sendPool")
    public EventPool sendPool(@RequestParam String userId, @RequestParam String eventPoolId){
        EventPool eventPool = userRepository.findByEventPoolId(userId, eventPoolId);
        Attendee[] attendees =  eventPool.getEvent().getAttendees();
        for (Attendee at: attendees ){
            String attendeeId = at.getCalendarPerson().getId();
            userRepository.addEventPoolNotification(attendeeId, eventPool);
        }
        return eventPool;
    }

    @PostMapping("/pool/create")
    public EventPool createPool(@RequestBody EventPool eventPool){
        String ownerId = eventPool.getOwnerId();
        userRepository.addEventPool(ownerId, eventPool);
        return eventPool;
    }
}
