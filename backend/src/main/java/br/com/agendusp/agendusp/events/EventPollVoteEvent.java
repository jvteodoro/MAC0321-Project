package br.com.agendusp.agendusp.events;

import org.springframework.context.ApplicationEvent;

public class EventPollVoteEvent extends ApplicationEvent {

    String userId;
    String eventId;

    public EventPollVoteEvent(Object origin, String userId, String eventId){
        super(origin);
        this.userId = userId;
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    
}
