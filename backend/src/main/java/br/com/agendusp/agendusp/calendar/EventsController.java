package br.com.agendusp.agendusp.calendar;

import org.springframework.http.HttpStatusCode;

public interface EventsController {
    public HttpStatusCode delete(String calendarId, String eventId);
    public String get(String calendarId, String eventId);
    // public String importEvent(String calendarId, EventResource event);
    public String insert(String calendarId, EventsResource event);
    // public CalendarEvents instances(String calendarId, String eventId);
    public String list(String calendarId);
    // public String move(String calendarId, String eventId, String destination);
    public String patch(String calendarId, String eventId, EventsResource event);
    // public String quickAdd(String calendarId, String text);
    public String update(String calendarId, String eventId, EventsResource event);
    // public WatchResponse watch(String calendarId, WatchRequest watchRequest);
}
