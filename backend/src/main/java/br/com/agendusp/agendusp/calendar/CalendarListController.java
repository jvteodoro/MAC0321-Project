package br.com.agendusp.agendusp.calendar;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public interface CalendarListController {
    public ResponseEntity<Void> delete(String calendarId, OAuth2AuthorizedClient authorizedClient);
    public String get(String calendarId, OAuth2AuthorizedClient authorizedClient);
    public String insert(CalendarListResource calendar, OAuth2AuthorizedClient authorizedClient);
    public String list(OAuth2AuthorizedClient authorizedClient); 
    public String patch(CalendarListResource calendar, OAuth2AuthorizedClient authorizedClient);
    public String update(CalendarListResource calendar, OAuth2AuthorizedClient authorizedClient);
    
    // public String watch(WatchRequest watchRequest);

}
