package br.com.agendusp.agendusp.calendar;

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
    AbstractEvent[] items;
}
