package br.com.agendusp.agendusp.controller;

import org.springframework.web.bind.annotation.RestController;

import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.dataobjects.DateTimeInterval;
import br.com.agendusp.agendusp.documents.EventsResource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class HomeController {

    @Autowired
    EventsDataController eventsDataController;

    @GetMapping("/stats")
    public String generateStats(@RequestBody DateTimeInterval interval) {
        ArrayList<EventsResource> allEvents = eventsDataController.getEventsOnInterval(interval);
        int eventsNum = allEvents.size();
        long timeInEvents = 0;
        int canceledEvents = 0;
        for (EventsResource ev: allEvents){
            timeInEvents += ChronoUnit.HOURS.between(ev.getEnd().getDateTime(), ev.getStart().getDateTime());
            if (ev.getStatus() == "cancelled"){
                canceledEvents += 1;
            }            
        }

        return "Você participou de "+eventsNum+
        "eventos, o que corresponde a "+timeInEvents+
        " horas"+". Desses eventos, "+canceledEvents+" foram cancelados.";
    }
}
