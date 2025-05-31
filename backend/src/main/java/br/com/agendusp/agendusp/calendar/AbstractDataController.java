package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;

public interface AbstractDataController {
    public CalendarListResource updateCalendar(String calendarId, CalendarListResource calResource, String userId);

    public CalendarListResource patchCalendar(String calendarId, CalendarListResource calResource,
            String userId);

    public ArrayList<CalendarListResource> getCalendars(String userId);

    public void addCalendar(CalendarListResource calResource, String userId);

    public CalendarListResource getCalendar(String calendarId, String userId);

    public void removeCalendar(String calendarId, String userId);

    public void addEvent(String calendarId, EventsResource eventResource,
            String userId);

    public EventsResource getEvent(String eventId, String calendarId,
            String userId);

    public EventsResource updateEvent(String calendarId, String eventId, EventsResource eventResource,
            String userId);

    public ArrayList<EventsResource> getEvents(String calendarId, String userId);

    public EventsResource patchEvent(String calendarId, String eventId, EventsResource eventResource,
            String userId);

    public void removeEvent(String eventId, String calendarId, String userId);
}
