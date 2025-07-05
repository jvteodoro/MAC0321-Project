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
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.documents.eventDocuments.EventPollResource;

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
    String atendee1Id = "at1";
    String atendee2Id = "at2";
    String atendee3Id = "at3";
    String eventId = "eventTest";

    @BeforeEach
    public void setupDatabase() {
        User user = new User(userId, userId, userId);
        User atendee1 = new User(atendee1Id, atendee1Id, atendee1Id);
        User atendee2 = new User(atendee2Id, atendee2Id, atendee2Id);
        User atendee3 = new User(atendee3Id, atendee3Id, atendee3Id);

        EventsResource event = new EventsResource();
        event.setId(eventId);
        event.addAttendee(atendee1);
        event.addAttendee(atendee2);
        event.addAttendee(atendee3);

        userDataController.createUser(user);
        userDataController.createUser(atendee1);
        userDataController.createUser(atendee2);
        userDataController.createUser(atendee3);

        eventsDataController.addEvent(event);
    }

    @Test
    public void createTest() {
        String startDate = "2025-07-01T20:00:00Z";
        String endDate = "2025-07-20T20:00:00Z";
        EventPollResource poll = eventPollDataController.create(eventId, startDate, endDate);
        assertNotNull(poll);
        assertEquals(eventId, poll.getEventId());
    }

    @Test
    public void testAddOptionsAndVote() {
        String startDate = "2025-07-01T20:00:00Z";
        String endDate = "2025-07-20T20:00:00Z";
        EventPollResource poll = eventPollDataController.create(eventId, startDate, endDate);

        List<String> options = Arrays.asList("2025-07-10T20:00:00Z", "2025-07-15T20:00:00Z");
        eventPollDataController.addOptions(poll.getId(), options);

        // Vote for an option
        eventPollDataController.vote(poll.getId(), atendee1Id, "2025-07-10T20:00:00Z");
        eventPollDataController.vote(poll.getId(), atendee2Id, "2025-07-10T20:00:00Z");
        eventPollDataController.vote(poll.getId(), atendee3Id, "2025-07-15T20:00:00Z");

        // Check votes
        EventPollResource updatedPoll = eventPollDataController.getPoll(poll.getId());
        assertEquals(2, updatedPoll.getVotes().get("2025-07-10T20:00:00Z").size());
        assertEquals(1, updatedPoll.getVotes().get("2025-07-15T20:00:00Z").size());
    }

    @Test
    public void testDuplicateVoteThrowsException() {
        String startDate = "2025-07-01T20:00:00Z";
        String endDate = "2025-07-20T20:00:00Z";
        EventPollResource poll = eventPollDataController.create(eventId, startDate, endDate);

        List<String> options = Arrays.asList("2025-07-10T20:00:00Z");
        eventPollDataController.addOptions(poll.getId(), options);

        eventPollDataController.vote(poll.getId(), atendee1Id, "2025-07-10T20:00:00Z");
        assertThrows(RuntimeException.class, () -> {
            eventPollDataController.vote(poll.getId(), atendee1Id, "2025-07-10T20:00:00Z");
        });
    }

    @Test
    public void testClosePoll() {
        String startDate = "2025-07-01T20:00:00Z";
        String endDate = "2025-07-20T20:00:00Z";
        EventPollResource poll = eventPollDataController.create(eventId, startDate, endDate);

        eventPollDataController.closePoll(poll.getId());
        EventPollResource closedPoll = eventPollDataController.getPoll(poll.getId());
        assertTrue(closedPoll.isClosed());
    }

    @Test
    public void testVoteOnClosedPollThrowsException() {
        String startDate = "2025-07-01T20:00:00Z";
        String endDate = "2025-07-20T20:00:00Z";
        EventPollResource poll = eventPollDataController.create(eventId, startDate, endDate);

        List<String> options = Arrays.asList("2025-07-10T20:00:00Z");
        eventPollDataController.addOptions(poll.getId(), options);

        eventPollDataController.closePoll(poll.getId());
        assertThrows(RuntimeException.class, () -> {
            eventPollDataController.vote(poll.getId(), atendee1Id, "2025-07-10T20:00:00Z");
        });
    }

    @Test
    public void testCreatePollWithNoOptions() {
        String startDate = "2025-07-01T20:00:00Z";
        String endDate = "2025-07-20T20:00:00Z";
        EventPollResource poll = eventPollDataController.create(eventId, startDate, endDate);
        assertNotNull(poll);
        assertTrue(poll.getOptions().isEmpty());
    }

    @Test
    public void testGetNonExistentPollThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            eventPollDataController.getPoll("nonexistentPollId");
        });
    }

    @Test
    public void testDeletePoll() {
        String startDate = "2025-07-01T20:00:00Z";
        String endDate = "2025-07-20T20:00:00Z";
        EventPollResource poll = eventPollDataController.create(eventId, startDate, endDate);
        eventPollDataController.deletePoll(poll.getId());
        assertThrows(RuntimeException.class, () -> {
            eventPollDataController.getPoll(poll.getId());
        });
    }
}


