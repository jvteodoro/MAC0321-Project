package br.com.agendusp.agendusp.controller.eventControllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPoll;
import br.com.agendusp.agendusp.repositories.EventPoolRepository;

public class EventPoolDataController {
    @Autowired
    EventPoolRepository eventPoolRepository;

    public ArrayList<EventPoll> getAllEventPools(ArrayList<String> eventPoolIdList) {
        // Verifica se a lista de IDs está vazia
        if (eventPoolIdList == null || eventPoolIdList.isEmpty()) {
            throw new IllegalArgumentException("A lista de IDs de EventPool não pode ser nula ou vazia.");
        }
        ArrayList<EventPoll> allEventPools = new ArrayList<>();
        for (String id : eventPoolIdList) {
            allEventPools.add(eventPoolRepository.findById(id).orElse(null));
        }
        return allEventPools;
    }
}
