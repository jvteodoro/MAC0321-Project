package br.com.agendusp.agendusp.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import br.com.agendusp.agendusp.dataobjects.EventPool;
import br.com.agendusp.agendusp.documents.CalendarListUserItem;
import br.com.agendusp.agendusp.documents.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    @Query("{'userId' : ?0}")
    @Update("{$push: {'calendarList' : ?1}}")
    Optional<Integer> updateOneByUserId(String userId, CalendarListUserItem calendarListUserItem);

    
    @Query(value = "{ 'userId' : ?0 }")  // Explicit query without projection
    public Optional<User> findByUserId(String userId);
    
@Aggregation(pipeline = {
        "{ $match: { 'userId': ?0 } }",
        "{ $unwind: '$calendarList' }",
        "{ $match: { 'calendarList.calendarId': ?1 } }",
        "{ $replaceRoot: { newRoot: '$calendarList' } }",
        "{ $limit: 1 }"
    })    
    Optional<CalendarListUserItem> findCalendarListUserItemByUserIdAndCalendarId(String userId, String calendarId);

    
    @Query(value = "{ 'userId': ?0, 'calendarList.calendarId': ?1 }", exists = true)
    boolean existsByUserIdAndCalendarId(String userId, String calendarId);

    @Query("{ 'userId' : ?0, 'calendarList.calendarId': ?1 }")
    void deleteCalendarListResourceById(String userId, String calendarId);

    @Query("{'calendarList.calendarId': ?0}")
    @Update("{$pull : {'calendarList.calendarId' : ?0} ")
    void refreshLinks(String calendarId);

    @Query("{'userId': ?0}")
    @Update("{$push : {'eventPoolNotification' : ?1}}")
    void addEventPoolNotification(String userId, EventPool eventPool);
    
    @Query("{'userId': ?0}")
    @Update("{$push : {'eventPoolList' : ?1}}")
    void addEventPool(String userId, EventPool eventPool);

    @Aggregation(pipeline = {
        "{ $match: { 'userId': ?0 } }",
        "{ $unwind: '$eventPoolNotification' }",
        "{ $match: { 'eventPoolNotification.id': ?1 } }",
        "{ $replaceRoot: { newRoot: '$eventPoolNotification' } }",
        "{ $limit: 1 }"
    })    
    EventPool findByEventPoolId(String userId, String eventPoolId);
}
