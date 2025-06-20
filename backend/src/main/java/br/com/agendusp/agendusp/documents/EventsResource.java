package br.com.agendusp.agendusp.documents;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.api.services.calendar.model.Event.ExtendedProperties;

import br.com.agendusp.agendusp.dataobjects.Attendee;
import br.com.agendusp.agendusp.dataobjects.CalendarPerson;
import br.com.agendusp.agendusp.dataobjects.EventDate;

@Document(collection = "events")
public class EventsResource {

    @Id
    String id;
    String eventId;
    int links; // It's necessary to know how many calendars links this event.
            // If it is 0, the event can be deleted.

    String kind;
    String etag;
    String mainCalendarId;
    ArrayList<String> calendarIds;
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
    EventDate start;
    EventDate end;
    boolean endTimeUnspecified;
    String[] recurrence;
    String recurringEventId;
    EventDate originalStartTime;
    String transparency;
    String visibility;
    String iCalUID;
    String sequence;
    Attendee[] attendees;
    boolean attendeesOmitted;
    ExtendedProperties extendedProperties;
    String hangoutLink;

    public EventsResource() {}

    public EventsResource(int links, String eventId, String kind, String etag, String id, String status, String htmlLink,
            String created, String updated, String summary, String description, String location, String colorId,
            CalendarPerson creator, CalendarPerson organizer, EventDate start, EventDate end,
            boolean endTimeUnspecified, String[] recurrence, String recurringEventId, EventDate originalStartTime,
            String transparency, String visibility, String iCalUID, String sequence, Attendee[] attendees,
            boolean attendeesOmitted, ExtendedProperties extendedProperties, String hangoutLink) {
        this.links = links;
        this.eventId = eventId;
        this.kind = kind;
        this.etag = etag;
        this.id = id;
        this.status = status;
        this.htmlLink = htmlLink;
        this.created = created;
        this.updated = updated;
        this.summary = summary;
        this.description = description;
        this.location = location;
        this.colorId = colorId;
        this.creator = creator;
        this.organizer = organizer;
        this.start = start;
        this.end = end;
        this.visibility = visibility;
        this.endTimeUnspecified = endTimeUnspecified;
        this.originalStartTime = originalStartTime;
        this.recurringEventId = recurringEventId;
        this.recurrence = recurrence;
        this.transparency = transparency;
        this.iCalUID = iCalUID;
        this.sequence = sequence;
        this.attendees = attendees;
        this.attendeesOmitted = attendeesOmitted;
        this.extendedProperties = extendedProperties;
        this.hangoutLink = hangoutLink;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public ArrayList<String> setCalendarIds(ArrayList<String> calendarIds) {
        this.calendarIds = calendarIds;
        return this.calendarIds;
    }

    public ArrayList<String> getCalendarIds() {
        return this.calendarIds;
    }

    public ArrayList<String> addCalendarId(String calendarId) {
        if (this.calendarIds == null) {
            this.calendarIds = new ArrayList<>();
        }
        this.calendarIds.add(calendarId);
        return this.calendarIds;
    }

    public ArrayList<String> removeCalendarId(String calendarId) {
        if (this.calendarIds != null) {
            this.calendarIds.remove(calendarId);
        }
        return this.calendarIds;
    }

    public void increaseLinks(){
        this.links++;
    }

    public String getMainCalendarId() {
        return this.mainCalendarId;
    }

    public void setMainCalendarId(String mainCalendarId) {
        this.mainCalendarId = mainCalendarId;
    }

    public int getLinks() {
        return links;
    }
    public void setLinks(int links) {
        this.links = links;
    }
    public String getKind() {
        return kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getEtag() {
        return etag;
    }
    public void setEtag(String etag) {
        this.etag = etag;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getHtmlLink() {
        return htmlLink;
    }
    public void setHtmlLink(String htmlLink) {
        this.htmlLink = htmlLink;
    }
    public String getCreated() {
        return created;
    }
    public void setCreated(String created) {
        this.created = created;
    }
    public String getUpdated() {
        return updated;
    }
    public void setUpdated(String updated) {
        this.updated = updated;
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
    public String getColorId() {
        return colorId;
    }
    public void setColorId(String colorId) {
        this.colorId = colorId;
    }
    public CalendarPerson getCreator() {
        return creator;
    }
    public void setCreator(CalendarPerson creator) {
        this.creator = creator;
    }
    public CalendarPerson getOrganizer() {
        return organizer;
    }
    public void setOrganizer(CalendarPerson organizer) {
        this.organizer = organizer;
    }
    public EventDate getStart() {
        return start;
    }
    public void setStart(EventDate start) {
        this.start = start;
    }
    public EventDate getEnd() {
        return end;
    }
    public void setEnd(EventDate end) {
        this.end = end;
    }
    public boolean isEndTimeUnspecified() {
        return endTimeUnspecified;
    }
    public void setEndTimeUnspecified(boolean endTimeUnspecified) {
        this.endTimeUnspecified = endTimeUnspecified;
    }
    public String[] getRecurrence() {
        return recurrence;
    }
    public void setRecurrence(String[] recurrence) {
        this.recurrence = recurrence;
    }
    public String getRecurringEventId() {
        return recurringEventId;
    }
    public void setRecurringEventId(String recurringEventId) {
        this.recurringEventId = recurringEventId;
    }
    public EventDate getOriginalStartTime() {
        return originalStartTime;
    }
    public void setOriginalStartTime(EventDate originalStartTime) {
        this.originalStartTime = originalStartTime;
    }
    public String getTransparency() {
        return transparency;
    }
    public void setTransparency(String transparency) {
        this.transparency = transparency;
    }
    public String getVisibility() {
        return visibility;
    }
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
    public String getiCalUID() {
        return iCalUID;
    }
    public void setiCalUID(String iCalUID) {
        this.iCalUID = iCalUID;
    }
    public String getSequence() {
        return sequence;
    }
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
    public Attendee[] getAttendees() {
        return attendees;
    }
    public void setAttendees(Attendee[] attendees) {
        this.attendees = attendees;
    }

    public void addAttendee(Attendee attendee) {
        if (this.attendees == null) {
            this.attendees = new Attendee[0];
        }
        Attendee[] newAttendees = new Attendee[this.attendees.length + 1];
        System.arraycopy(this.attendees, 0, newAttendees, 0, this.attendees.length);
        newAttendees[this.attendees.length] = attendee;
        this.attendees = newAttendees;
    }
    public void removeAttendee(Attendee attendee) {
        if (this.attendees != null) {
            ArrayList<Attendee> attendeesList = new ArrayList<>();
            for (Attendee a : this.attendees) {
                if (!a.getCalendarPerson().getId().equals(attendee.getCalendarPerson().getId())) {
                    attendeesList.add(a);
                }
            }
            this.attendees = attendeesList.toArray(new Attendee[0]);
        }
    }

    public boolean isAttendeesOmitted() {
        return attendeesOmitted;
    }
    public void setAttendeesOmitted(boolean attendeesOmitted) {
        this.attendeesOmitted = attendeesOmitted;
    }
    public ExtendedProperties getExtendedProperties() {
        return extendedProperties;
    }
    public void setExtendedProperties(ExtendedProperties extendedProperties) {
        this.extendedProperties = extendedProperties;
    }
    public String getHangoutLink() {
        return hangoutLink;
    }
    public void setHangoutLink(String hangoutLink) {
        this.hangoutLink = hangoutLink;
    }
}
