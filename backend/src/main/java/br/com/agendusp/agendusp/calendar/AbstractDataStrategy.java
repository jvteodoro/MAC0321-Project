package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.EventsResource;

public interface AbstractDataStrategy {
    public String updateCalendar(String calendarId, CalendarListResource calResource);
    public String patchCalendar(String calendarId, CalendarListResource calResource);
    public ArrayList<String> getCalendars();
    public void addCalendar(CalendarListResource calResource);
    public CalendarListResource getCalendar(String calendarId);
    public void removeCalendar(String calendarId);
    public void addEvent(String calendarId, EventsResource eventResource);
    public EventsResource getEvent(String eventId, String calendarId);
    public EventsResource updateEvent(String calendarId, String eventId, EventsResource eventResource);
    public ArrayList<EventsResource> getEvents(String calendarId);
    public EventsResource patchEvent(String calendarId, String eventId, EventsResource eventResource);
    public void removeEvent(String eventId, String calendarId);
}
