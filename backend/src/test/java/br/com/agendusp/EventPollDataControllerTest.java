package br.com.agendusp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.agendusp.agendusp.AgendUspApplication;
import br.com.agendusp.agendusp.MongoTestContainer;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventPollDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPoll;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AgendUspApplication.class)
@AutoConfigureMockMvc
public class EventPollDataControllerTest extends MongoTestContainer {
    @Autowired
    EventPollDataController eventPollDataController;
    @Autowired
    UserDataController userDataController;
    @Autowired
    EventsDataController eventsDataController;

    String userId = "user1";
    String attendee1Id = "at1";
    String attendee2Id = "at2";
    String attendee3Id = "at3";
    String eventId = "eventTest";

    @BeforeEach
    public void setupDatabase() {
        User user = new User(userId, userId, userId);
        User attendee1 = new User(attendee1Id, attendee1Id, attendee1Id);
        User attendee2 = new User(attendee2Id, attendee2Id, attendee2Id);
        User attendee3 = new User(attendee3Id, attendee3Id, attendee3Id);

        EventsResource event = new EventsResource();
        event.setId(eventId);
        event.addAttendee(attendee1);
        event.addAttendee(attendee2);
        event.addAttendee(attendee3);
        //event.setOwner(user); //isso aq funciona?

        userDataController.createUser(user);
        userDataController.createUser(attendee1);
        userDataController.createUser(attendee2);
        userDataController.createUser(attendee3);
        

//         eventsDataController.addEvent(event);
    }

    @Test
    public void createTest() {
        String startDate = "2025-07-01T20:00:00Z";
        String endDate = "2025-07-20T20:00:00Z";
        EventPoll poll = eventPollDataController.create(eventId, startDate, endDate, userId);
        assertNotNull(poll);
        //assertEquals(eventId, poll.getEventId()); implementar essa funcao? acho q nao precisa
    }

    @Test
        public void testCreateEventPoll() {
            String startDate = "2025-07-01T20:00:00Z";
            String endDate = "2025-07-20T20:00:00Z";
            EventPoll poll = eventPollDataController.create(eventId, startDate, endDate, userId);
            assertNotNull(poll);
        }
    
        @Test
        public void testCreateEventPollNonOrganizerThrows() {
            String startDate = "2025-07-01T20:00:00Z";
            String endDate = "2025-07-20T20:00:00Z";
            Exception ex = assertThrows(IllegalArgumentException.class, () -> {
                eventPollDataController.create(eventId, startDate, endDate, attendee1Id);
            });
            assertTrue(ex.getMessage().contains("Apenas o organizador pode criar uma enquete para este evento."));
        }
    
        @Test
        public void testGetAllEventPolls() {
            String startDate = "2025-07-01T20:00:00Z";
            String endDate = "2025-07-20T20:00:00Z";
            String startDate2 = "2025-07-22T20:00:00Z";
            String endDate2 = "2025-07-22T20:00:00Z";
            String organizerId = userId;
            EventPoll poll1 = eventPollDataController.create(eventId, startDate, endDate, userId);
            EventPoll poll2 = eventPollDataController.create(eventId, startDate2, endDate2, organizerId);
    
            ArrayList<String> ids = new ArrayList<>();
            ids.add(poll1.getId());
            ids.add(poll2.getId());
            
            ArrayList<EventPoll> polls = eventPollDataController.getAllEventPolls(ids);
            
            assertEquals(2, polls.size());
            assertTrue(polls.stream().anyMatch(p -> p.getId().equals(poll1.getId())));
            assertTrue(polls.stream().anyMatch(p -> p.getId().equals(poll2.getId())));
        }
    
        @Test
        public void testGetByIdReturnsPoll() {
            String startDate = "2025-07-01T20:00:00Z";
            String endDate = "2025-07-20T20:00:00Z";
            EventPoll poll = eventPollDataController.create(eventId, startDate, endDate, userId);
    
            EventPoll found = eventPollDataController.getById(poll.getId());// mano esse getid funciona?????????
            assertNotNull(found);
            assertEquals(poll.getId(), found.getId());
        }
    
        @Test
        public void testGetByIdReturnsNullIfNotFound() {
            EventPoll found = eventPollDataController.getById("nonexistentId");
            assertNull(found);
        }
    
        @Test
        public void testGetAllEventPollsThrowsOnNullOrEmpty() {
            assertThrows(IllegalArgumentException.class, () -> {
                eventPollDataController.getAllEventPolls(null);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                eventPollDataController.getAllEventPolls(new ArrayList<>());
            });
        }

    }



