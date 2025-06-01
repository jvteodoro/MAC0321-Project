package br.com.agendusp.agendusp.calendar;

import br.com.agendusp.agendusp.documents.EventsResource;

public class CalendarEvents {

    String kind = "calendar#events";
    String etag;
    String summary;
    String description;
    String updated;
    String timeZone;
    String accessRole;
    AbstractCalendarReminder[] defaultReminders;
    String nextPageToken;
    String nextSyncToken;
    EventsResource[] items;
}
