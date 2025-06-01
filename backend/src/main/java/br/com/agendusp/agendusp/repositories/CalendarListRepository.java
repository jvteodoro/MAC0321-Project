package br.com.agendusp.agendusp.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.agendusp.agendusp.calendar.CalendarListResource;

@Repository
public interface CalendarListRepository extends MongoRepository<CalendarListResource, String> {
    //public Optional<CalendarListResource> findById(String id);
    //public CalendarListResource insert(CalendarListResource calendarListResource);
    // public CalendarListResource insert(CalendarListResource
    // calendarListResource);
}
