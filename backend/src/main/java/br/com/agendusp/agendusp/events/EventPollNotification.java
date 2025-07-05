package br.com.agendusp.agendusp.events;

import org.springframework.context.ApplicationEvent;

public class EventPollNotification extends ApplicationEvent {
    String message;
    String eventPollId;

    public EventPollNotification(Object source, String eventPollId, String message){
        super(source);
        this.message = message;
        this.eventPollId = eventPollId;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getEventPollId() {
        return eventPollId;
    }
    public void setEventPollId(String eventPollId) {
        this.eventPollId = eventPollId;
    }
    public String getId(){
        return this.eventPollId;
    }

    
    
}
