package br.com.agendusp.agendusp.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);

    @Query("{'name' : ?0}")
    @Update("$addToSet: {'calendarList' : ?1}")
    User addUserCalendarList(String name, CalendarListResource calendarListResource);
    
    @Query("{ 'userId' : ?0, 'calendarList.id': ?1 }")
    Optional<CalendarListResource> findCalendarListResourceByUserIdAndCalendarId(String userId, String calendarId);

    @Query("{ 'userId' : ?0, 'calendarList.id': ?1 }")
    Boolean existsByCalendarId(String userId, String calendarId);
}
