package br.com.agendusp.agendusp.calendar;

import org.springframework.http.HttpStatusCode;

public interface EventsController {
    public HttpStatusCode delete(String calendarId, String eventId, String sendUpdates);
    public EventResource get(String calendarId, String eventId,
        String timeZone, String alwaysIncludeEmail, String maxAttendees);
    public EventResource importEvent(String calendarId, EventResource event);
    public EventResource insert(String calendarId, EventResource event,
                                String sendUpdates, String maxAttendees);
    public CalendarEvents instances(String calendarId, String eventId);
    public CalendarEvents list(String calendarId);
    public EventResource move(String calendarId, String eventId, String destination);
    public EventResource patch(String calendarId, String eventId, EventResource event);
    public EventResource quickAdd(String calendarId, String text);
    public EventResource update(String calendarId, String eventId, EventResource event);
    public WatchResponse watch(String calendarId, WatchRequest watchRequest);
}
