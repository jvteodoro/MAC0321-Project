package br.com.agendusp.agendusp.calendar;

import org.springframework.http.HttpStatusCode;

public interface EventsController {
    public HttpStatusCode cancel(String calendarID, String eventId, String sendUpdates);
    public HttpStatusCode delete(String calendarId, String eventId, String sendUpdates);
    public AbstractEvent get(String calendarId, String eventId,
        String timeZone, String alwaysIncludeEmail, String maxAttendees);
    public AbstractEvent importEvent(String calendarId, AbstractEvent event);
    public AbstractEvent insert(String calendarId, AbstractEvent event,
                                String sendUpdates, String maxAttendees);
    public CalendarEvents instances(String calendarId, String eventId);
    public CalendarEvents list(String calendarId);
    public AbstractEvent move(String calendarId, String eventId, String destination);
    public AbstractEvent patch(String calendarId, String eventId, AbstractEvent event);
    public AbstractEvent quickAdd(String calendarId, String text);
    public AbstractEvent update(String calendarId, String eventId, AbstractEvent event);
    public WatchResponse watch(String calendarId, WatchRequest watchRequest);
}
