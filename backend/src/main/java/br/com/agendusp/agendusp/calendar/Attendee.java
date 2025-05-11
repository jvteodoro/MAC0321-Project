package br.com.agendusp.agendusp.calendar;

public abstract class Attendee {
    CalendarPerson calendarPerson;
    final boolean organizer;
    boolean resource;
    boolean optional;
    CalendarResponseStatus responseStatus;
    String comment;
    int additionalGuests;

    public Attendee(CalendarPerson calendarPerson) {
        this.calendarPerson = calendarPerson;
        this.organizer = false; // defaults to false
        this.resource = false; // defaults to false
        this.optional = false; // defaults to false
        this.responseStatus = new CalendarResponseStatus(); // defaults to "needsAction"
        this.comment = ""; // defaults to empty string
        this.additionalGuests = 0; // defaults to 0

    }

    public Attendee(CalendarPerson calendarPerson, boolean organizer){
        this.calendarPerson = calendarPerson;
        this.organizer = organizer; // defaults to false
        this.resource = false; // defaults to false
        this.optional = false; // defaults to false
        this.responseStatus = new CalendarResponseStatus(); // defaults to "needsAction"
        this.comment = ""; // defaults to empty string
        this.additionalGuests = 0; // defaults to 0
    }

}
