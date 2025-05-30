package br.com.agendusp.agendusp.calendar;

import org.springframework.http.HttpStatusCode;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public interface EventsController {
    public HttpStatusCode delete(String calendarId, String eventId, OAuth2AuthorizedClient authorizedClient);

    public String get(String calendarId, String eventId, OAuth2AuthorizedClient authorizedClient);

    // public String importEvent(String calendarId, EventResource event);

    public String insert(String calendarId, EventsResource event, OAuth2AuthorizedClient authorizedClient);

    // public CalendarEvents instances(String calendarId, String eventId);

    public String list(String calendarId, OAuth2AuthorizedClient authorizedClient);

    // public String move(String calendarId, String eventId, String destination);

    public String patch(String calendarId, EventsResource event,
            OAuth2AuthorizedClient authorizedClient);

    // public String quickAdd(String calendarId, String text);

    public String update(String calendarId, EventsResource event,
            OAuth2AuthorizedClient authorizedClient);

    // public WatchResponse watch(String calendarId, WatchRequest watchRequest);
}
