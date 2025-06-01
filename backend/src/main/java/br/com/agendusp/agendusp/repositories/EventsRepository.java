package br.com.agendusp.agendusp.repositories;


import java.lang.reflect.Array;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.EventsResource;

@Repository
public interface EventsRepository extends MongoRepository<EventsResource, String>{
    /*public Optional<EventsResource> findById(String id);
    public List<EventsResource> findAll();*/
    @Query("{'calendarIds': { $in: ?0}}")
    public Optional<ArrayList<EventsResource>> findAllBycalendarId(String calendarId);

}
