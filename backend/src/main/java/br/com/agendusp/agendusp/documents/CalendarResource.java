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
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getCalendarResource() {
        return calendarResource;
    }

    public void setCalendarResource(String calendarResource) {
        this.calendarResource = calendarResource;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getSummaryOverride() {
        return summaryOverride;
    }

    public void setSummaryOverride(String summaryOverride) {
        this.summaryOverride = summaryOverride;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

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
