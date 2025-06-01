package br.com.agendusp.agendusp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.agendusp.agendusp.documents.UserCalendarRelation;

public interface UserCalendarListResourceAccessRelationRepository
        extends MongoRepository<UserCalendarRelation, String> {
    public Optional<List<UserCalendarRelation>> findAllByUserId(String userId);
    // public UserCalendarListRelation insert(UserCalendarListRelation relation);
}
