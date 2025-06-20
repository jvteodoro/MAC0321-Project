package br.com.agendusp.agendusp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;

import br.com.agendusp.agendusp.CustomOAuth2User;
import br.com.agendusp.agendusp.documents.EventsResource;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface EventsController {
        public ResponseEntity<String> delete(String calendarId, String eventId,
                       @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        public String get(String calendarId, String eventId,
                        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        // public String importEvent(String calendarId, EventResource event);

        public String insert(String calendarId, @RequestBody EventsResource event,
                       @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        public String addAttendee(String calendarId, String attendeeId , @RequestBody EventsResource event,
                       @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        // public CalendarEvents instances(String calendarId, String eventId);

        public String list(String calendarId,
                        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        // public String move(String calendarId, String eventId, String destination);

        public String patch(String calendarId, @RequestBody EventsResource event,
                       @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        // public String quickAdd(String calendarId, String text);

        public String update(String calendarId, EventsResource event,
                       @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient);

        // public WatchResponse watch(String calendarId, WatchRequest watchRequest);
}
