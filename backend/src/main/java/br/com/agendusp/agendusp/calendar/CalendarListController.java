package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;

import org.springframework.http.HttpStatusCode;

public interface CalendarListController {
    public ResponseEntity<Void> delete(String calendarId);
    //public ArrayList<Calendar> get(Calendar calendar);
    //public CalendarList insert(Calendar calendar);
    public CalendarList list(OAuth2AuthorizedClient authorizedClient); 
    //public CalendarList patch(Calendar calendar);
    //public CalendarList update(Calendar calendar);
    //public WatchResponse watch(WatchRequest watchRequest);

}
