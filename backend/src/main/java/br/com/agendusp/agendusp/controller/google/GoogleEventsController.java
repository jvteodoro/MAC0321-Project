package br.com.agendusp.agendusp.controller.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.dataobjects.eventObjects.EventListResource;
import br.com.agendusp.agendusp.documents.EventsResource;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class GoogleEventsController {
    @Autowired
    RestClient restClient;
    @Autowired
    EventsDataController eventsDataController;
    @Autowired
    UserDataController userDataController;

    @DeleteMapping("/google/events/delete")
    public String delete(@RequestParam String calendarId,
            @RequestParam String eventId,
            @RequestParam(defaultValue = "none") String sendUpdates,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        return restClient.delete()
                .uri("https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events/" + eventId
                        + "?sendUpdates=" + sendUpdates)
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(String.class).getBody();
    }

    @GetMapping("/google/events/get")
    public EventsResource get(@RequestParam String calendarId,
            @RequestParam String eventId,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        return restClient.get()
                .uri("https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events/" + eventId)
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(EventsResource.class).getBody();
    }

    @PostMapping("/google/events/import")
    public EventsResource importEvent(@RequestBody EventsResource importBody,
            @RequestParam String calendarId,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        return restClient.post()
                .uri("https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events/import")
                .body(importBody)
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(EventsResource.class).getBody();
    }

    @PostMapping("/google/events/insert")
    public EventsResource insert(@RequestBody EventsResource eventBody,
            @RequestParam String calendarId,
            @RequestParam(defaultValue = "none") String sendUpdates,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        return restClient.post()
                .uri("https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events?sendUpdates="
                        + sendUpdates)
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .body(eventBody).retrieve().toEntity(EventsResource.class).getBody();
    }

    @GetMapping("/google/events/list")
    public EventListResource list(@RequestParam String calendarId,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        EventListResource eventListResource = restClient.get()
                .uri("https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events")
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(EventListResource.class).getBody();
        for (EventsResource resource : eventListResource.getItems()) {
            try {
                if (resource.getOrganizer() == null || resource.getOrganizer().getEmail() == null) {
                    System.err.println("[SKIP] Event with id=" + resource.getId() + " summary=" + resource.getSummary()
                            + " has null organizer or organizer email.");
                    continue;
                }
                resource.setMainCalendarId(resource.getOrganizer().getEmail());
                resource.addCalendarId(calendarId);
                eventsDataController.addEvent(resource);
            } catch (Exception e) {
                System.err.println("[ERROR] Skipping event with id=" + resource.getId() + " summary="
                        + resource.getSummary() + ": " + e);
            }
        }

        return eventListResource;
    }

        @PostMapping("/google/events/update")
        public EventsResource update(@RequestBody EventsResource event,
                @RequestParam String calendarId,
                @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        if (event.getId() == null || event.getId().isEmpty()) {
            throw new IllegalArgumentException("Event ID must not be null or empty");
        }
        if (event.getStart() == null || event.getEnd() == null) {
            throw new IllegalArgumentException("Event must have a start and end time.");
        }       
        String userId = authorizedClient.getPrincipalName();
        // Atualiza o evento no banco de dados local
        eventsDataController.updateEvent(calendarId, event.getId(), event, userId);
        // Atualiza o evento no Google Calendar
        return restClient.put()
                .uri("https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events/" + event.getId())
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .body(event).retrieve().toEntity(EventsResource.class).getBody();
        }
        
    // Pensar no tipo de dado que um Json somente com as partes escolhidas
    // representa
    // @PatchMapping("/google/events/patch")
    // public EventsResource patch(@RequestBody J)

}
