package br.com.agendusp.agendusp.calendar;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.EventsResource;

public interface AbstractDataController {
        public CalendarListResource updateCalendar(String calendarId, CalendarListResource calResource, String userId);

        public CalendarListResource patchCalendar(String calendarId, CalendarListResource calResource,
                        String userId);

        public CalendarListResource[] getCalendars(String userId) throws Exception;

        public void addCalendar(CalendarResource calResource, String userId);

        public CalendarListResource getCalendar(String calendarId, String userId);

        public void removeCalendar(String calendarId, String userId);

        public void addEvent(String calendarId, EventsResource eventResource,
                        String userId, String accessRole);

        public EventsResource getEvent(String eventId, String calendarId,
                        String userId);

        public EventsResource updateEvent(String calendarId, String eventId, EventsResource eventResource,
                        String userId);

        public EventsResource[] getEvents(String calendarId, String userId);

        public EventsResource patchEvent(String calendarId, String eventId, EventsResource eventResource,
                        String userId);

        public void removeEvent(String eventId, String calendarId, String userId);

        public void cancelEvent(String eventId, String calendarId, String userId);
        public void addCalendarListResource(CalendarListResource calResource, String userId, String accessRole);
}
