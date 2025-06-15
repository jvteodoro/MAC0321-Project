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
    EventsResource event;
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
    public EventsResource getEvent() {
        return event;
    }
    public void setEvent(EventsResource event) {
        this.event = event;
    }

    
    
}
