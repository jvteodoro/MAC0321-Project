package br.com.agendusp.agendusp.calendar;

import com.google.api.services.calendar.model.Event.ExtendedProperties;

public abstract class EventResource {

    int links; // It's necessary to know how many calendars links this event.
            // If it is 0, the event can be deleted.

    String kind;
    String etag;
    String id;
    String status;
    String htmlLink;
    String created;
    String updated;
    String summary;
    String description;
    String location;
    String colorId;
    CalendarPerson creator;
    CalendarPerson organizer;
    TimePoint start;
    TimePoint end;
    boolean endTimeUnspecified;
    String[] recurrence;
    String recurringEventId;
    TimePoint originalStartTime;
    String transparency;
    String visibility;
    String iCalUID;
    String sequence;
    Attendee[] attendees;
    boolean attendeesOmitted;
    ExtendedProperties extendedProperties;
    String hangoutLink;

} 