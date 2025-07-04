package br.com.agendusp;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agendusp.agendusp.AgendUspApplication;
import br.com.agendusp.agendusp.MongoTestContainer;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.calendarControllers.CalendarDataController;
import br.com.agendusp.agendusp.dataobjects.calendarObjects.CalendarPerson;
import br.com.agendusp.agendusp.dataobjects.eventObjects.Attendee;
import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPoll;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.events.EventPollNotification;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

@SpringBootTest(classes = AgendUspApplication.class)
@AutoConfigureMockMvc
public class UserRepositoryTest extends MongoTestContainer {
    @Autowired
    UserDataController userDataController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    private User setupFind() {
        
        String googleId = "4321234";
        String emailTest = "test@gmail.com";
        User user = new User(googleId, emailTest, googleId);
        Optional<User> userOp = userRepository.findById(googleId);
        if (userOp.isPresent()){
            userRepository.delete(user);
        }
        userRepository.insert(user);
        return user;
    }

    EventPoll createEventPoll(){
        EventPoll evPool = new EventPoll();
        EventsResource ev = new EventsResource();
        ev.setId("testEvent");
        Attendee at1 = new Attendee();
        at1.setCalendarPerson(new CalendarPerson("attende1", "attende@email.com", "Attendee 1"));
        ev.addAttendee(at1);
        evPool.setEvent(ev);
        return evPool;
    }

    public CalendarListResource createDummyCalListRes(String id){
        CalendarListResource calRes = new CalendarListResource();
        calRes.setId(id);
        calRes.setCalendarId(id);
        return calRes;
    }


    @Test
    @Order(1)
    @WithMockUser
    public void findByGoogleIdTest() throws Exception {
        User user = setupFind();
        Optional<User> recoveredUser = userRepository.findByGoogleId(user.getGoogleId());
        if (recoveredUser.isEmpty()) {
        } else {
            assertEquals(objectMapper.writeValueAsString(user),
                    objectMapper.writeValueAsString(recoveredUser.get()));
        }
        userRepository.delete(user);

    }  
    @Test
    @Order(2)
    @WithMockUser
    public void findByUserIdTest() throws Exception {
        User user = setupFind();
        Optional<User> recoveredUser = userRepository.findByUserId(user.getGoogleId());
        if (recoveredUser.isEmpty()) {
        } else {
            assertEquals(objectMapper.writeValueAsString(user),
                    objectMapper.writeValueAsString(recoveredUser.get()));
        }
        userRepository.delete(user);

    }

    @Test
    @Order(3)
    public void insertCalendarListResourceByUserIdTest() throws Exception{
        String calId = "testId";
        CalendarListResource calResource = new CalendarListResource();
        calResource.setId(calId);
        User user = setupFind();
        userRepository.insertCalendarListResourceByUserId(user.getId(), calResource);
        Optional<CalendarListResource> retrievedCalResource = userRepository.findById(user.getId())
        .orElseThrow(() -> new Exception("Erro, usuário não encontrado"))
        .getCalendarList()
        .stream().filter(p -> p.getId().equals(calId)).findFirst();
        if (retrievedCalResource.isPresent()){
            assertEquals(objectMapper.writeValueAsString(calResource),
                        objectMapper.writeValueAsString(retrievedCalResource));
        }

    }

    @Test
    @Order(4)
    public void findCalendarListResourceByIdAndCalendarIdTest() throws Exception{
        String calId = "testId";
        CalendarListResource calResource = new CalendarListResource();
        calResource.setId(calId);
        User user = setupFind();
        String userId = user.getId();
        userRepository.insertCalendarListResourceByUserId(userId, calResource);
        user = userRepository.findById(user.getId()).orElseThrow(() -> new Exception("Error creating user"));
        Optional<CalendarListResource> retrievedCalRes = userRepository.findCalendarListResourceByIdAndCalendarId(userId, calId);
        if  (retrievedCalRes.isPresent()){
            assertEquals(
                objectMapper.writeValueAsString(calResource), 
                objectMapper.writeValueAsString(retrievedCalRes));
        } else {
            throw new Exception("Error retrieving calendar list resource");
        }

    }

    @Test
    @Order(5)
    public void deleteCalendarListResourceByIdTest(){
        String calId = "testId";
        CalendarListResource calResource = new CalendarListResource();
        calResource.setId(calId);
        User user = setupFind();
        String userId = user.getId();
        userRepository.insertCalendarListResourceByUserId(userId, calResource);
        userRepository.deleteCalendarListResourceById(userId, calId);
        assertTrue(userRepository.findCalendarListResourceByIdAndCalendarId(userId, calId).isEmpty());
    }
    @Test
    @Order(5)
    public void refreshLinksTest(){
        String calId = "testId";
        CalendarListResource calResource = new CalendarListResource();
        calResource.setId(calId);
        User user = setupFind();
        String userId = user.getId();
        userRepository.insertCalendarListResourceByUserId(userId, calResource);
        userRepository.refreshLinks(calId);
        assertTrue(userRepository.findCalendarListResourceByIdAndCalendarId(userId, calId).isEmpty());
    }

    @Test
    @Order(6)
    public void existsByUserIdAndCalendarIdTest() {

        String userId = "testUser";
        String calendarId = "test-calendar-id";
        User user = new User(userId);
        CalendarListResource calendarListResource = new CalendarListResource();
        calendarListResource.setCalendarId(calendarId);
        user.addCalendarListResource(calendarListResource);
        userRepository.insert(user);
        assertTrue(userRepository.existsByUserIdAndCalendarId(userId, calendarId));
    }

    @Test
    @Order(7)
    public void addEventPoolNotificationTest() throws Exception {
        String eventId = "testEvent";
        User user = setupFind();
        String userId = user.getId();
        EventPoll evPoll = new EventPoll();
        evPoll.setId(eventId);
        userRepository.addEventPoolNotification(userId, evPoll);
        Optional<User> retrievedUser = userRepository.findById(userId);
        if (retrievedUser.isPresent()){
            EventPollNotification retrievedEvPoll = retrievedUser.get()
                .getEventPoolNotifications()
                .stream()
                .filter(p -> p.getId().equals( evPoll.getId() )).findFirst().get();

            assertEquals(
                objectMapper.writeValueAsString(evPoll),
                objectMapper.writeValueAsString(retrievedEvPoll)
                );
        }
    }

    @Test
    @Order(8)
    public void addEventPoolTest() throws Exception {
        User user = setupFind();
        EventPoll evPoll = createEventPoll();
        String userId = user.getId();

        userRepository.addEventPool(userId, evPoll.getId());
        User recoveredUser = userRepository.findById(userId).orElseThrow(() -> new Exception("Erro ao adicionar o usuário"));
        assertTrue(evPoll
        .getId()
        .equals(recoveredUser
            .getEventPoolList()
            .stream()
            .filter(p -> p.equals(evPoll.getId())).findFirst().get()
             ));
        
    }

    @Test
    @Order(9)
    public void findEventPoolNotificationByEventPoolIdTest() throws Exception {
        User user = setupFind();
        EventPoll evPoll = createEventPoll();
        String userId = user.getId();

        userRepository.addEventPoolNotification(userId, evPoll);

        EventPoll recoveredEvPoll = userRepository
        .findEventPoolNotificationByEventPoolId(userId, evPoll.getId())
        .orElseThrow(() -> new Exception("Erro ao adicionar event pool notification"));

        assertEquals(objectMapper.writeValueAsString(evPoll), 
        objectMapper.writeValueAsString(recoveredEvPoll));
    }

    // @Test
    // @Order (9)
    // public void getCalendarListTest() throws Exception {
    //     User user = setupFind();
    //     String userId = user.getId();
    //     String calRes1Id = "testCalRes1";
    //     String calRes2Id = "testCalRes2";
    //     String calRes3Id = "testCalRes3";
    //     String calRes4Id = "testCalRes4";
    //     CalendarListResource calRes1 = createDummyCalListRes(calRes1Id);
    //     CalendarListResource calRes2 = createDummyCalListRes(calRes2Id);
    //     CalendarListResource calRes3 = createDummyCalListRes(calRes3Id);
    //     CalendarListResource calRes4 = createDummyCalListRes(calRes4Id);

    //     userRepository.addCalendarListResource(userId, calRes1);
    //     userRepository.addCalendarListResource(userId, calRes2);
    //     userRepository.addCalendarListResource(userId, calRes3);
    //     userRepository.addCalendarListResource(userId, calRes4);

    //     ArrayList<CalendarListResource> expected = userRepository.findById(userId)
    //     .orElseThrow(() -> new Exception("Erro ao adicionar o usuário")).getCalendarList();

    //     ArrayList<CalendarListResource> retrieved = userRepository.getCalendarList(userId);
    //     assertEquals(objectMapper.writeValueAsString(expected),
    //     objectMapper.writeValueAsString(retrieved));
    // }
}
