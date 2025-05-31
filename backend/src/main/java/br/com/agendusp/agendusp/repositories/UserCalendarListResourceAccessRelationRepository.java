package br.com.agendusp.agendusp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.agendusp.agendusp.calendar.UserCalendarListRelation;

public interface UserCalendarListResourceAccessRelationRepository
        extends MongoRepository<UserCalendarListRelation, String> {
    public Optional<List<UserCalendarListRelation>> findAllByUserId(String userId);
    // public UserCalendarListRelation insert(UserCalendarListRelation relation);
}
