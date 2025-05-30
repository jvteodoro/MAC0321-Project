package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;


public interface AbstractDataController {
    public void addCalendar(CalendarListResource calResource);
    public CalendarListResource getCalendar(String calendarId);
    public CalendarListResource patchCalendar(String calendarId, CalendarListResource calResource);
    public CalendarListResource updateCalendar(String calendarId, CalendarListResource calResource);
    public ArrayList<CalendarListResource> getCalendars();
    public ArrayList<EventResource> getEvents(String calendarId);
    public EventResource getEvent(String eventId, String calendarId);
    public void removeCalendar(String calendarId);
    public void addEvent(String calendarId, EventResource eventResource);
    public void removeEvent(String eventId, String calendarId);
    public void updateEvent(String eventId, EventResource eventResource);
}
