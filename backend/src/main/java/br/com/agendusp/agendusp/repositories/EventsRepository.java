package br.com.agendusp.agendusp.repositories;


import java.lang.reflect.Array;
import java.util.Optional;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.agendusp.agendusp.calendar.CalendarListResource;
import br.com.agendusp.agendusp.calendar.EventsResource;

@Repository
public interface EventsRepository extends MongoRepository<EventsResource, String>{
    /*public Optional<EventsResource> findById(String id);
    public List<EventsResource> findAll();*/

}
