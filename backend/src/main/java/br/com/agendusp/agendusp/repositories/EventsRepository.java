package br.com.agendusp.agendusp.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.agendusp.agendusp.calendar.EventsResource;


public interface EventsRepository extends MongoRepository<EventsResource, String>{

}
