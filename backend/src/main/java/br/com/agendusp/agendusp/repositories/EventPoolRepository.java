package br.com.agendusp.agendusp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPoll;

import java.util.List;
import java.util.Optional;

public interface EventPoolRepository extends MongoRepository<EventPoll, String> {
    public Optional<EventPoll> findById(String id);
}
