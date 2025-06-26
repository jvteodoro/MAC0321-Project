package br.com.agendusp.agendusp.controller.eventControllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPool;
import br.com.agendusp.agendusp.repositories.EventPoolRepository;

public class EventPoolDataController {
    @Autowired
    EventPoolRepository eventPoolRepository;

    public ArrayList<EventPool> getAllEventPools(ArrayList<String> eventPoolIdList){
        ArrayList<EventPool> allEventPools = new ArrayList<>();
        for (String id: eventPoolIdList){
            allEventPools.add(eventPoolRepository.findById(id).orElse(null));
        }
        return allEventPools;
    }
}
