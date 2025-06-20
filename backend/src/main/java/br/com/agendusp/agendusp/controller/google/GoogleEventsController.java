package br.com.agendusp.agendusp.controller.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import br.com.agendusp.agendusp.controller.EventsDataController;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.dataobjects.EventListResource;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

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
                        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient){
        return restClient.delete()
        .uri("https://www.googleapis.com/calendar/v3/calendars/"+calendarId+"/events/"+eventId+"?sendUpdates="+sendUpdates)
        .headers(headers ->headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
        .retrieve().toEntity(String.class).getBody();
    }
    @GetMapping("/google/events/get")
    public EventsResource get(@RequestParam String calendarId,
    @RequestParam String eventId,
    @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient){
        return restClient.get()
        .uri("https://www.googleapis.com/calendar/v3/calendars/"+calendarId+"/events/"+eventId)
        .headers(headers ->headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
        .retrieve().toEntity(EventsResource.class).getBody();
    }

    @PostMapping("/google/events/import")
    public EventsResource importEvent(@RequestBody EventsResource importBody, 
    @RequestParam String calendarId,
    @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient){
        return  restClient.post()
        .uri("https://www.googleapis.com/calendar/v3/calendars/"+calendarId+"/events/import").body(importBody)
        .headers(headers ->headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
        .retrieve().toEntity(EventsResource.class).getBody();
    }

    @PostMapping("/google/events/insert")
    public EventsResource insert(@RequestBody EventsResource eventBody, 
    @RequestParam String calendarId,
    @RequestParam(defaultValue = "none") String sendUpdates,
    @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient){
        return restClient.post().uri("https://www.googleapis.com/calendar/v3/calendars/"+calendarId+"/events?sendUpdates="+sendUpdates)
        .headers(headers ->headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
        .body(eventBody).retrieve().toEntity(EventsResource.class).getBody();
    }

    @GetMapping("/google/events/list")
    public EventListResource list(@RequestParam String calendarId, 
    @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient){
        EventListResource eventListResource = restClient.get().uri("https://www.googleapis.com/calendar/v3/calendars/"+calendarId+"/events")
        .headers(headers ->headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
        .retrieve().toEntity(EventListResource.class).getBody();
        for (EventsResource resource: eventListResource.getItems()){
            resource.setMainCalendarId(resource.getOrganizer().getEmail());
            resource.addCalendarId(calendarId);
            eventsDataController.addEvent(resource);
        }

        return eventListResource;
    }

    //Pensar no tipo de dado que um Json somente com as partes escolhidas representa
    // @PatchMapping("/google/events/patch")
    // public EventsResource patch(@RequestBody J)

}
