package br.com.agendusp.agendusp.events;

import org.springframework.context.ApplicationEvent;

public class EventPollDoneEvent extends ApplicationEvent {

    String eventPollId;

    public EventPollDoneEvent(Object source, String eventPollId){
        super(source);
        this.eventPollId = eventPollId;
    }

    public String getEventPollId() {
        return eventPollId;
    }

    public void setEventPollId(String eventPollId) {
        this.eventPollId = eventPollId;
    }

    
}
