package br.com.agendusp.agendusp.dataobjects.eventObjects;

import java.util.ArrayList;

import br.com.agendusp.agendusp.dataobjects.DateTimeInterval;
import br.com.agendusp.agendusp.dataobjects.DateTimeIntervalPoll;
import br.com.agendusp.agendusp.documents.EventsResource;

// representa enquete de melhor horario para um evento
public class EventPoll {
    ArrayList<Attendee> attendees;
    // Inicia com o numero de atendee e toda vez que alguém responde diminui um
    // Quando estiver em zero => todos responderam
    int done;
    String id;
    String ownerId;
    String eventId;
    ArrayList<DateTimeIntervalPoll> posibleTimes;

    public EventPoll () {}

    public EventPoll (EventsResource event) {
      this.setEvent(event);
      this.done = event.getAttendees().size() - 1;
    }

    public void addPossibleTimesFromDateTimeIntervalList(ArrayList<DateTimeInterval> dtList) {
        int currentId = 0;
        if (this.posibleTimes == null) {
            this.posibleTimes = new ArrayList<>();
        }
        for (DateTimeInterval dt : dtList) {
            DateTimeIntervalPoll dtip = new DateTimeIntervalPoll(dt, currentId++);
            System.out.println("[DEBUG] DateTimeIntervalPoll id: " + dtip.getId());
            this.posibleTimes.add(dtip);
        }
    }
    public void setPossibleTimesFromDateTimeIntervalList(ArrayList<DateTimeInterval> dtList) {
        int currentId = 0;
        ArrayList<DateTimeIntervalPoll> intervalPoll = new ArrayList<>();
        for (DateTimeInterval dt : dtList) {
            intervalPoll.add(new DateTimeIntervalPoll(dt, currentId++));
        }
        this.posibleTimes = intervalPoll;
    }

    public void vote(int dateTimeIntervalPollId) {
        for (DateTimeIntervalPoll dtPoll : this.posibleTimes){
            if (dtPoll.getId() == dateTimeIntervalPollId){
                dtPoll.vote();
            }
        }
        this.done = done - 1;
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
        this.ownerId = event.getOrganizer().getId();
    }

    @Override
    public String toString() {
        return "EventPoll{" +
                "attendees=" + attendees +
                ", done=" + done +
                ", id='" + id + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", eventId=" + eventId +
                '}';
    }
    public ArrayList<DateTimeIntervalPoll> getPosibleTimes() {
        return posibleTimes;
    }
    public void setPosibleTimes(ArrayList<DateTimeIntervalPoll> posibleTimes) {
        this.posibleTimes = posibleTimes;
    }
    
}
