package br.com.agendusp.agendusp.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.agendusp.agendusp.CustomOAuth2User;
import br.com.agendusp.agendusp.dataobjects.EventDate;
import br.com.agendusp.agendusp.documents.EventsResource;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class LocalEventsController implements EventsController {

    @Autowired
    private Gson gson;
    @Autowired
    private EventsDataController eventsDataController;

    public LocalEventsController() {}

    @GetMapping("/events/listWindows")
    public boolean[][] listWindows(@RequestParam String calendarId, @RequestParam String endDate) {

        int divTempo = 24;
         // Quando 0 então a menor unidade é hora
        // Quando 1 então a menor unidade é minuto
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime dateEndObj = LocalDateTime.parse(endDate);
        int dayNum = ((int)ChronoUnit.DAYS.between(today, dateEndObj));
        ArrayList<EventsResource> eventsOnInterval = eventsDataController.getEventsOnInterval(calendarId, dateEndObj);
        boolean[][] dateVec = new boolean[dayNum][divTempo];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (EventsResource ev: eventsOnInterval){
            EventDate end = ev.getEnd();
            EventDate start = ev.getStart();
            LocalDateTime startDateTime = start.getDateTime();//LocalDateTime.parse(start.getDateTime(), formatter);
            LocalDateTime endDateTime = end.getDateTime();//LocalDateTime.parse(end.getDateTime(), formatter);
            int endDayLoop = 1;
            
            int difDayStart = (int)ChronoUnit.DAYS.between(today, startDateTime);

            if (startDateTime.getDayOfYear() != endDateTime.getDayOfYear()){
                endDayLoop = (int)ChronoUnit.DAYS.between(today, endDateTime);
            }
            boolean hasExecuted = false;
            int j =0;
            for (int i = difDayStart ; i < endDayLoop; i++){
                for ( j = (hasExecuted == false )? 
                startDateTime.getHour()*60+startDateTime.getMinute(): 0;
                j < endDateTime.getHour()*60+endDateTime.getMinute(); j++){
                     dateVec[i][j] = true;
                     hasExecuted = true;
                } 
            }
        }
        return dateVec;
    }

    @DeleteMapping("/events/delete/{calendarId}/{eventId}")
    public ResponseEntity<String> delete(@PathVariable String calendarId,@PathVariable String eventId,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        String userId = authorizedClient.getPrincipalName();
        
        eventsDataController.removeEvent(eventId, calendarId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/events/get")
    public String get(String calendarId, String eventId, @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        String userId = authorizedClient.getPrincipalName();
        return gson.toJson(eventsDataController.getEvent(eventId, calendarId, userId));
    }

    @PostMapping("/events/addAttendee/{calendarId}/{attendeeId}")
    public String addAttendee(String calendarId, String attendeeId, @RequestBody EventsResource event,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        String userId = authorizedClient.getPrincipalName();
        return gson.toJson(eventsDataController.addAtendeeToEvent(event.getEventId(), calendarId, userId, attendeeId));
    }

    @PostMapping("/events/insert/{calendarId}")
    public String insert(String calendarId, @RequestBody EventsResource event,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        String userId = authorizedClient.getPrincipalName();
        eventsDataController.createEvent(calendarId, event, userId);
        return gson.toJson(event);
    }

    @GetMapping("/events/list")
    public String list(@RequestBody String calendarId, @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        String userId = authorizedClient.getPrincipalName();
        return gson.toJson(eventsDataController.getEvents(calendarId, userId));
    }

    @PutMapping("/events/update/{calendarId}")
    public String update(String calendarId, EventsResource event,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        String userID = authorizedClient.getPrincipalName();
        return gson.toJson(eventsDataController.updateEvent(calendarId, event.getId(), event, userID));
    }

    @PatchMapping("/events/patch/{calendarId}")
    public String patch(String calendarId, @RequestBody EventsResource event, @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        String userId = authorizedClient.getPrincipalName();
        JsonObject body = new JsonObject(); // atualizacão parcial (dos atributos nao nulos)

        if (event.getStart() != null) {
            body.add("start", gson.toJsonTree(event.getStart()));
        }
        if (event.getEnd() != null) {
            body.add("end", gson.toJsonTree(event.getEnd()));
        }
        if (event.getSummary() != null) {
            body.addProperty("summary", event.getSummary());
        }
        if (event.getLocation() != null) {
            body.addProperty("location", event.getLocation());
        }
        if (event.getAttendees() != null) {
            body.add("attendees", gson.toJsonTree(event.getAttendees()));
        }

        return gson.toJson(eventsDataController.patchEvent(calendarId, event.getId(),
                gson.fromJson(body, EventsResource.class), userId));
    }
}
