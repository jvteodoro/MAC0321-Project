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

    User insertUser(User user);

    @Query("{'name' : ?0}")
    @Update("$addToSet: {'calendarList' : ?1}")
    User addUserCalendarList(String name, CalendarListResource calendarListResource);

    public Optional<User> findByUserId(String userId);
    
    @Query("{ 'userId' : ?0, 'calendarList.id': ?1 }")
    Optional<CalendarListResource> findCalendarListResourceByUserIdAndCalendarId(String userId, String calendarId);

    @Query("{ 'userId' : ?0, 'calendarList.id': ?1 }")
    Boolean existsByCalendarId(String userId, String calendarId);

    @Query("{ 'userId' : ?0, 'calendarList.id': ?1 }")
    void deleteCalendarListResourceById(String userId, String calendarId);

    @Query("{'calendarList.id': ?0}")
    @Update("{$pull : {'calendarList.id' : ?0} ")
    void refreshLinks(String calendarId);
}
