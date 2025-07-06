package br.com.agendusp.agendusp.dataobjects;

import br.com.agendusp.agendusp.dataobjects.eventObjects.Notification;
import br.com.agendusp.agendusp.events.EventPollNotification;

public class PollNotification extends Notification {

    public PollNotification(){}

    public PollNotification(String userId, String eventPollId, String message, String type){
        super(userId, message, eventPollId);
    }
    
}
