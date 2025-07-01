package br.com.agendusp.agendusp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPool;

import java.util.List;
import java.util.Optional;


public interface EventPoolRepository extends MongoRepository<EventPool, String>{
    public Optional<EventPool> findById(String id);
}
