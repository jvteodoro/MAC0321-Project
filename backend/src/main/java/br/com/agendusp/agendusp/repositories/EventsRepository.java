package br.com.agendusp.agendusp.repositories;


import java.util.Optional;
import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.agendusp.agendusp.documents.EventsResource;

@Repository
public interface EventsRepository extends MongoRepository<EventsResource, String>{
    /*public Optional<EventsResource> findById(String id);
    public List<EventsResource> findAll();*/
    public Optional<EventsResource> findByEventId(String eventId);
    @Query("{'calendarIds': { $in: ?0}}")
    public Optional<ArrayList<EventsResource>> findAllByCalendarId(String calendarId);

    @Query("{'eventId': ?0, 'calendarIds': { $in: ?1}}")
    public Optional<EventsResource> findEventsResourceByEventIdAndCalendarId(String eventId, String calendarId);

    @Query("{ 'eventId': ?0, 'attendees': { $elemMatch: { 'calendarPerson.id': ?1 } } }")
    public Optional<EventsResource> findEventByEventIdAndUserId(String eventId, String userId);

}
