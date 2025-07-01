package br.com.agendusp.agendusp.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.agendusp.agendusp.documents.CalendarResource;

@Repository
public interface CalendarRepository extends MongoRepository<CalendarResource, String> {
    public Optional<CalendarResource> findByCalendarId(String calendarId);

    public Optional<CalendarResource> findById(String id);
    // public Optional<CalendarListResource> findById(String id);
    // public CalendarListResource insert(CalendarListResource
    // calendarListResource);
    // public CalendarListResource insert(CalendarListResource
    // calendarListResource);
}
