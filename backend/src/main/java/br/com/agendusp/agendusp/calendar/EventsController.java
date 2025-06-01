package br.com.agendusp.agendusp.calendar;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import br.com.agendusp.agendusp.CustomOAuth2User;
import br.com.agendusp.agendusp.documents.EventsResource;

public interface EventsController {
        public ResponseEntity<String> delete(String calendarId, String eventId,
                        @AuthenticationPrincipal CustomOAuth2User customUser);

        public String get(String calendarId, String eventId, @AuthenticationPrincipal CustomOAuth2User customUser);

        // public String importEvent(String calendarId, EventResource event);

        public String insert(String calendarId, EventsResource event,
                        @AuthenticationPrincipal CustomOAuth2User customUser);

        // public CalendarEvents instances(String calendarId, String eventId);

        public String list(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser);

        // public String move(String calendarId, String eventId, String destination);

        public String patch(String calendarId, EventsResource event,
                        @AuthenticationPrincipal CustomOAuth2User customUser);

        // public String quickAdd(String calendarId, String text);

        public String update(String calendarId, EventsResource event,
                        @AuthenticationPrincipal CustomOAuth2User customUser);

        // public WatchResponse watch(String calendarId, WatchRequest watchRequest);
}
