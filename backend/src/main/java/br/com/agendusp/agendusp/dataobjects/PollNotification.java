package br.com.agendusp.agendusp.dataobjects;

import br.com.agendusp.agendusp.dataobjects.eventObjects.Notification;

public class PollNotification extends Notification {

    String eventPollId;

    public PollNotification(){}

    public PollNotification(Object source, String userId, String eventPollId, String message, String type){
        super(source, userId, message, type);
        this.eventPollId = eventPollId;
    }

    public String getEventPollId() {
        return eventPollId;
    }

    public void setEventPollId(String eventPollId) {
        this.eventPollId = eventPollId;
    }
    
}
