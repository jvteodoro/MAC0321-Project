package br.com.agendusp.agendusp.repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPoll;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByGoogleId(String googleId);

    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

    @Query("{'id' : ?0}")
    @Update("{$push: {'calendarList' : ?1}}")
    Optional<Integer> insertCalendarListResourceByUserId(String userId, CalendarListResource calendarListResource);

    @Query(value = "{ 'id' : ?0 }") // Explicit query without projection
    public Optional<User> findByUserId(String userId);

    @Aggregation(pipeline = {
            "{ $match: { 'id': ?0 } }",
            "{ $unwind: '$calendarList' }",
            "{ $match: { 'calendarList.id': ?1 } }",
            "{ $replaceRoot: { newRoot: '$calendarList' } }",
            "{ $limit: 1 }"
    })
    Optional<CalendarListResource> findCalendarListResourceByIdAndCalendarId(String userId, String calendarId);

    @Query(value = "{ 'id': ?0, 'calendarList.calendarId': ?1 }", exists = true)
    boolean existsByUserIdAndCalendarId(String userId, String calendarId);
    
    @Query("{'id': ?0}")
    @Update("{ '$pull': { 'calendarList': { 'calendarId': ?1 } } }")    
    void deleteCalendarListResourceById(String userId, String calendarId);

    @Query("{'calendarList.calendarId': ?0}")
    @Update("{ '$pull': { 'calendarList': { 'calendarId': ?0 } } }")    
    void refreshLinks(String calendarId);

    @Query("{'id': ?0}")
    @Update("{$push : {'eventPollNotifications' : ?1}}")
    void addEventPollNotification(String userId, EventPoll eventPoll);

    @Query("{'id': ?0}")
    @Update("{$push : {'eventPollList' : ?1}}")
    void addEventPoll(String userId, String eventPollId);

    @Aggregation(pipeline = {
            "{ $match: { 'id': ?0 } }",
            "{ $unwind: '$eventPollNotifications' }",
            "{ $match: { 'eventPollNotifications.id': ?1 } }",
            "{ $replaceRoot: { newRoot: '$eventPollNotifications' } }",
            "{ $limit: 1 }"
    })
    Optional<EventPoll> findEventPollNotificationByEventPollId(String userId, String eventPollId);
    
    // Essa função não pode retornar apenas um arrya. O mongo requer
    // que seja retornado o document todo
    @Query(value = "{ 'id' : ?0}", fields = "{'calendarList': 1}")
    ArrayList<CalendarListResource> getCalendarList(String userId);

    @Query("{ 'id': ?0 }")
    @Update(" {$push: {'calendarList' : ?1 }}")
    void addCalendarListResource(String userId, CalendarListResource item);
    // @Aggregation(pipeline = {
    // "{ $match: { 'userId' : ?0 }}",
    // "{ $unwind: '$calendarList'}",
    // "{ $match: { 'calendarList.'}}"
    // })
    // CalendarListUserItem insertCalendarListUserItem();
}
