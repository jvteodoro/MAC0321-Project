package br.com.agendusp.agendusp.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private AbstractDataController dataController;
    @Autowired
    private Gson gson;

    public LocalEventsController() {}

    @GetMapping("/events/listWindows")
    public boolean[][] listWindows(@RequestParam String calendarId, @RequestParam String endDate) {

        int divTempo = 24;
         // Quando 0 então a menor unidade é hora
        // Quando 1 então a menor unidade é minuto
        LocalDate today = LocalDate.now();
        LocalDate dateEndObj = LocalDate.parse(endDate);
        int dayNum = ((int)ChronoUnit.DAYS.between(today, dateEndObj));
        ArrayList<EventsResource> eventsOnInterval = dataController.getEventsOnInterval(calendarId, endDate);
        boolean[][] dateVec = new boolean[dayNum][divTempo];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (EventsResource ev: eventsOnInterval){
            EventDate end = ev.getEnd();
            EventDate start = ev.getStart();
            LocalDateTime startDateTime = LocalDateTime.parse(start.getDateTime(), formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(end.getDateTime(), formatter);
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
    public ResponseEntity<String> delete(@PathVariable String calendarId, @PathVariable String eventId,
            @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        dataController.removeEvent(eventId, calendarId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/events/get")
    public String get(String calendarId, String eventId, @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        return gson.toJson(dataController.getEvent(eventId, calendarId, userId));
    }

    @PostMapping("/events/addAttendee/{calendarId}/{attendeeId}")
    public String addAttendee(String calendarId, String attendeeId, @RequestBody EventsResource event,
            @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        return gson.toJson(dataController.addAtendeeToEvent(event.getEventId(), calendarId, userId, attendeeId));
    }

    @PostMapping("/events/insert/{calendarId}")
    public String insert(String calendarId, @RequestBody EventsResource event,
            @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        dataController.createEvent(calendarId, event, userId);
        return gson.toJson(event);
    }

    @GetMapping("/events/list")
    public String list(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
        return gson.toJson(dataController.getEvents(calendarId, userId));
    }

    @PutMapping("/events/update/{calendarId}")
    public String update(String calendarId, EventsResource event,
            @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userID = customUser.getUser().getId();
        return gson.toJson(dataController.updateEvent(calendarId, event.getId(), event, userID));
    }

    @PatchMapping("/events/patch/{calendarId}")
    public String patch(String calendarId, @RequestBody EventsResource event, @AuthenticationPrincipal CustomOAuth2User customUser) {
        String userId = customUser.getUser().getId();
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

        return gson.toJson(dataController.patchEvent(calendarId, event.getId(),
                gson.fromJson(body, EventsResource.class), userId));
    }
}
