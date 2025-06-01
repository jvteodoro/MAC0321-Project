package br.com.agendusp.agendusp.documents;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;

import br.com.agendusp.agendusp.dataobjects.CalendarPerson;

public class CalendarResource {
    
    @Id
    String id;
    String calendarId;
    String kind = "calendar#calendar";
    String calendarResource;
    String etag;
    String summary;
    String description;
    String location;
    String timeZone;
    String summaryOverride;
    boolean hidden;
    boolean selected;
    CalendarPerson owner;
    ArrayList<CalendarPerson> writers;
    ArrayList<CalendarPerson> readers;
    ArrayList<CalendarPerson> freeBusyReaders;

    // ArrayList<DefaultReminder> defaultReminders;
    // ArrayList<> notificationSettings;
    boolean primary;
    boolean deleted;
    
    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public CalendarPerson getOwner() {
        return owner;
    }

    public void setOwner(CalendarPerson owner) {
        this.owner = owner;
    }

    public ArrayList<CalendarPerson> getWriters() {
        return writers;
    }

    public void setWriters(ArrayList<CalendarPerson> writers) {
        this.writers = writers;
    }

    public ArrayList<CalendarPerson> getReaders() {
        return readers;
    }

    public void setReaders(ArrayList<CalendarPerson> readers) {
        this.readers = readers;
    }

    public ArrayList<CalendarPerson> getFreeBusyReaders() {
        return freeBusyReaders;
    }

    public void setFreeBusyReaders(ArrayList<CalendarPerson> freeBusyReaders) {
        this.freeBusyReaders = freeBusyReaders;
    }

}
