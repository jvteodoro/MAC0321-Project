package br.com.agendusp.agendusp.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class LocalEventsController implements EventsController {

    @Autowired
    private final AbstractDataController dataController;
    @Autowired
    private final Gson gson;

    public LocalEventsController(AbstractDataController dataController, Gson gson) {
        this.dataController = dataController;
        this.gson = gson;
    }

    @DeleteMapping("/events/delete/{calendarId}/{eventId}")
    public ResponseEntity<void> delete(@PathVariable String calendarId, @PathVariable String eventId, OAuth2AuthorizedClient authorizedClient) {
        dataController.removeEvent(eventId, calendarId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/events/get")
    public String get(String calendarId, String eventId, OAuth2AuthorizedClient authorizedClient) {
        return gson.toJson(dataController.getEvent(eventId, calendarId));
    }

    @PostMapping("/events/insert/{calendarId}")
    public String insert(String calendarId, EventsResource event, OAuth2AuthorizedClient authorizedClient) {
        dataController.addEvent(calendarId, event);
        return gson.toJson(event);
    }

    @GetMapping("/events/list")
    public String list(String calendarId, OAuth2AuthorizedClient authorizedClient) {
        return gson.toJson(dataController.getEvents(calendarId));
    }

    @PutMapping("/events/update/{calendarId}")
    public String update(String calendarId, EventsResource event,
            OAuth2AuthorizedClient authorizedClient) {
        return gson.toJson(dataController.updateEvent(calendarId, event.getId(), event));
    }

    @PatchMapping("/events/patch/{calendarId}")
    public String patch(String calendarId, EventsResource event,
            OAuth2AuthorizedClient authorizedClient) {
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

        return gson.toJson(dataController.patchEvent(calendarId, event.getId(), gson.fromJson(body, EventsResource.class)));
    }
}
