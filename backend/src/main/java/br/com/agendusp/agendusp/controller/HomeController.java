package br.com.agendusp.agendusp.controller;

import org.springframework.web.bind.annotation.RestController;

import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.dataobjects.DateTimeInterval;
import br.com.agendusp.agendusp.documents.EventsResource;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HomeController {

    @Autowired
    EventsDataController eventsDataController;

    @GetMapping("/test")
    public String test(){
        return "Working!";
    }

    @GetMapping("/stats")
    public String generateStats(
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end") LocalDateTime end,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        DateTimeInterval interval = new DateTimeInterval();
        interval.setStart(start);
        interval.setEnd(end);

        System.out.println("[DEBUG] interval " + interval.getStart() + " | " + interval.getEnd());
        ArrayList<EventsResource> allEvents = eventsDataController.getEventsOnInterval(authorizedClient.getPrincipalName(), interval);
        System.out.println("[DEBUG] allEvents " + allEvents);
        int eventsNum = allEvents.size();
        long timeInEvents = 0;
        int canceledEvents = 0;
        for (EventsResource ev : allEvents) {
            timeInEvents += java.time.Duration.between(ev.getStart().getDateTime(), ev.getEnd().getDateTime())
                    .toHours();
            if ("cancelled".equals(ev.getStatus())) {
                canceledEvents += 1;
            }
        }

        return "Você participou de " + eventsNum +
                " eventos, o que corresponde a " + timeInEvents +
                " horas. Desses eventos, " + canceledEvents + " foram cancelados.";
    }
}
