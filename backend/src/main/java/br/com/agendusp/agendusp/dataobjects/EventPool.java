package br.com.agendusp.agendusp.dataobjects;

import java.util.ArrayList;

import br.com.agendusp.agendusp.documents.EventsResource;

public class EventPool {
    ArrayList<Attendee> attendees;
    // Inicia com o número de atendee e toda vez que alguém responde diminui um
    // Quando estiver em zero => todos responderam
    int done;
    String id;
    String ownerId;
    String eventId;
    ArrayList<DateTimeIntervalPool> posibleTimes;

    public EventPool (){}

    public EventPool (EventsResource event){
      this.setEvent(event);
    }

    public void setPossibleTimesFromDateTimeIntervalList(ArrayList<DateTimeInterval> dtList){
        for (DateTimeInterval dt:  dtList){
            posibleTimes.add(new DateTimeIntervalPool(dt));
        }
    }
    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }
    public void setAttendees(ArrayList<Attendee> attendees) {
        this.attendees = attendees;
    }
    public int getDone() {
        return done;
    }
    public void setDone(int done) {
        this.done = done;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public String getEventId() {
        return this.eventId;
    }
    public void setEvent(EventsResource event) {
        this.eventId = event.getId();
        this.id = event.getId();
        this.done = event.getAttendees().size();
        this.attendees = event.getAttendees();
    }

    @Override
    public String toString() {
        return "EventPool{" +
                "attendees=" + attendees +
                ", done=" + done +
                ", id='" + id + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", eventId=" + eventId +
                '}';
    }
    public ArrayList<DateTimeIntervalPool> getPosibleTimes() {
        return posibleTimes;
    }
    public void setPosibleTimes(ArrayList<DateTimeIntervalPool> posibleTimes) {
        this.posibleTimes = posibleTimes;
    }
    
}
