package br.com.agendusp.agendusp.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.dataobjects.PollNotification;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.events.EventPollNotification;
import br.com.agendusp.agendusp.events.EventPollVoteEvent;
import br.com.agendusp.agendusp.repositories.UserRepository;

@Component
public class VoteListener {

    @Autowired
    UserDataController userDataController;
    @Autowired
    UserRepository userRepository;


    @EventListener
    public void onApplicationEvent (EventPollVoteEvent event){
        System.out.println("Event voted listened!");
        User user = userDataController.findUser(event.getUserId());   
        ArrayList<PollNotification> pollNotifications = user.getEventPollNotifications();
        Optional<PollNotification> selected = pollNotifications.stream().filter(p -> p.getEventPollId().equals(event.getEventId())).findAny();
        if (selected.isPresent()){
            int index = pollNotifications.indexOf(selected.get());
            pollNotifications.remove(index);
            user.setEventPollNotifications(pollNotifications);
            userRepository.save(user);
        }
        
    }
}
