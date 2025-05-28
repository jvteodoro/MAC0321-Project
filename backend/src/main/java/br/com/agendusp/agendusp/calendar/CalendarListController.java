package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public interface CalendarListController {
    public ResponseEntity<Void> delete(String calendarId, OAuth2AuthorizedClient authorizedClient);
    public CalendarListResource get(Calendar calendar);
    public CalendarListResource insert(Calendar calendar);
    public CalendarListResource list(OAuth2AuthorizedClient authorizedClient); 
    public CalendarListResource patch(Calendar calendar);
    public CalendarListResource update(Calendar calendar);
    public WatchResponse watch(WatchRequest watchRequest);

}
