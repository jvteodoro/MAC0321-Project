package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public interface CalendarListController {
    public ResponseEntity<Void> delete(String calendarId, OAuth2AuthorizedClient authorizedClient);
    public CalendarListResource get(String calendarId);
    public CalendarListResource insert(CalendarListResource calendar, OAuth2AuthorizedClient authorizedClient);
    public ArrayList<CalendarListResource> list(OAuth2AuthorizedClient authorizedClient); 
    public CalendarListResource patch(CalendarListResource calendar);
    public CalendarListResource update(CalendarListResource calendar);
    public WatchResponse watch(WatchRequest watchRequest);

}
