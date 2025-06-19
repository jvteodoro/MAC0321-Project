package br.com.agendusp.agendusp.dataobjects;

public class Attendee {
    private CalendarPerson calendarPerson;
    private boolean organizer;
    private boolean resource;
    private boolean optional;
    private String responseStatus;
    private String comment;
    private int additionalGuests;

    public CalendarPerson getCalendarPerson() {
        return calendarPerson;
    }

    public void setCalendarPerson(CalendarPerson calendarPerson) {
        this.calendarPerson = calendarPerson;
    }

    public boolean isOrganizer() {
        return organizer;
    }

    public boolean isResource() {
        return resource;
    }

    public void setResource(boolean resource) {
        this.resource = resource;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAdditionalGuests() {
        return additionalGuests;
    }

    public void setAdditionalGuests(int additionalGuests) {
        this.additionalGuests = additionalGuests;
    }

    public Attendee(){
        
    }

    public Attendee(CalendarPerson calendarPerson) {
        this.calendarPerson = calendarPerson;
        this.organizer = false; // defaults to false
        this.resource = false; // defaults to false
        this.optional = false; // defaults to false
        this.responseStatus = "needsAction"; // defaults to "needsAction"
        this.comment = ""; // defaults to empty string
        this.additionalGuests = 0; // defaults to 0

    }

    public Attendee(CalendarPerson calendarPerson, boolean organizer){
        this.calendarPerson = calendarPerson;
        this.organizer = organizer; // defaults to false
        this.resource = false; // defaults to false
        this.optional = false; // defaults to false
        this.responseStatus = "needsAction"; // defaults to "needsAction"
        this.comment = ""; // defaults to empty string
        this.additionalGuests = 0; // defaults to 0
    }

}
