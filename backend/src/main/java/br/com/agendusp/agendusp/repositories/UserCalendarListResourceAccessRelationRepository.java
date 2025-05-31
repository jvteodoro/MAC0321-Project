package br.com.agendusp.agendusp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.agendusp.agendusp.calendar.UserCalendarListRelation;

public interface UserCalendarListResourceAccessRelationRepository extends MongoRepository<UserCalendarListRelation, String> {
    public UserCalendarListRelation insert(UserCalendarListRelation relation);
}
