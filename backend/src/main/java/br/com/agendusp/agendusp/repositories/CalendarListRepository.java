package br.com.agendusp.agendusp.repositories;


import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.agendusp.agendusp.calendar.CalendarListResource;

public interface CalendarListRepository extends MongoRepository<CalendarListResource, String> {
    public Optional<CalendarListResource> findById(String id);
    public CalendarListResource insert(CalendarListResource calendarListResource);
}
