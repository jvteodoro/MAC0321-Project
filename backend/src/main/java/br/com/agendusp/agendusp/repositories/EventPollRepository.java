package br.com.agendusp.agendusp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPoll;

import java.util.Optional;

public interface EventPollRepository extends MongoRepository<EventPoll, String> {
    public Optional<EventPoll> findById(String id);

    // Find poll by eventId
    Optional<EventPoll> findByEventId(String eventId);
}
