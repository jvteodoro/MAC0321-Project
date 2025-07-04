package br.com.agendusp.agendusp.dataobjects;

import br.com.agendusp.agendusp.events.EventPollNotification;

public class PollNotification {
    String id;
    String message;

    public PollNotification(){}

    public PollNotification(EventPollNotification event){
        this.id = event.getEventPollId();
        this.message = event.getMessage();
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }



    
}
