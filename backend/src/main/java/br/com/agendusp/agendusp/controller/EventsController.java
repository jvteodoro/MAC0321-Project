package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.agendusp.agendusp.documents.EventsResource;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface EventsController {
        public ResponseEntity<String> delete(String calendarId, String eventId,
                        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        public EventsResource get(String calendarId, String eventId,
                        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        // public String importEvent(String calendarId, EventResource event);

        public EventsResource insert(String calendarId, @RequestBody EventsResource event,
                        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        public EventsResource addAttendee(String calendarId, String attendeeId, @RequestBody EventsResource event,
                        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        // public CalendarEvents instances(String calendarId, String eventId);

        public ArrayList<EventsResource> list(@RequestParam String calendarId,
                        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        // public String move(String calendarId, String eventId, String destination);

        public String patch(String calendarId, @RequestBody EventsResource event,
                        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        // public String quickAdd(String calendarId, String text);

        public EventsResource update(@RequestBody EventsResource event, @RequestParam String calendarId,
                        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        // public WatchResponse watch(String calendarId, WatchRequest watchRequest);
}
