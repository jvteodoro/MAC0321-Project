package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;


public interface AbstractDataController {
    public CalendarListResource updateCalendar(String calendarId, CalendarListResource calResource);
    public CalendarListResource patchCalendar(String calendarId, CalendarListResource calResource);
    public ArrayList<CalendarListResource> getCalendars();
    public void addCalendar(CalendarListResource calResource);
    public CalendarListResource getCalendar(String calendarId);
    public void removeCalendar(String calendarId);
    public void addEvent(String calendarId, EventsResource eventResource);
    public EventsResource getEvent(String eventId, String calendarId);
    public EventsResource updateEvent(String eventId, EventsResource eventResource);
    public ArrayList<EventsResource> getEvents(String calendarId);
    public EventsResource patchEvent(String eventId, EventsResource eventResource);
    public void removeEvent(String eventId, String calendarId);
}
