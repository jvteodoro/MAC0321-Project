package br.com.agendusp.agendusp.calendar;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import com.google.api.client.json.Json;

public interface CalendarListController {
    public ResponseEntity<Void> delete(String calendarId, OAuth2AuthorizedClient authorizedClient);
    public Json get(String calendarId);
    public Json insert(CalendarListResource calendar, OAuth2AuthorizedClient authorizedClient);
    public Json list(OAuth2AuthorizedClient authorizedClient); 
    public Json patch(CalendarListResource calendar);
    public Json update(CalendarListResource calendar);
    public Json watch(WatchRequest watchRequest);

}
