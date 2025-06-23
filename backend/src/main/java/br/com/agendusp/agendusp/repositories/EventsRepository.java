package br.com.agendusp.agendusp.repositories;


import java.util.Optional;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.agendusp.agendusp.documents.EventsResource;

@Repository
public interface EventsRepository extends MongoRepository<EventsResource, String>{
    /*public Optional<EventsResource> findById(String id);
    public List<EventsResource> findAll();*/

    @Query("{'id': ?0}")
    public Optional<EventsResource> findById(String eventId);
    @Query("{'calendarIds': ?0}")
    public Optional<ArrayList<EventsResource>> findAllByCalendarId(String calendarId);

    @Query("{'id': ?0, 'calendarIds': ?1}")
    public Optional<EventsResource> findEventsResourceByEventIdAndCalendarId(String eventId, String calendarId);

    @Query("{ 'id': ?0, 'attendees': { $elemMatch: { 'calendarPerson.id': ?1 } } }")
    public Optional<EventsResource> findEventByEventIdAndUserId(String eventId, String userId);

    @Query("{ 'calendarIds': {$elemMatch: ?0}, 'end.date' : { $elemMatch: ?1}}")
    public Optional<ArrayList<EventsResource>> findEventsByEndDate(String calendarId, LocalDateTime endDate);

    @Query("{ 'start.dateTime': { $gte: ?0 }, 'end.dateTime': { $lte: ?1 } }")
    public Optional<ArrayList<EventsResource>> findEventosDentroDoIntervalo(LocalDateTime dataInicio, LocalDateTime dataFim);

}
