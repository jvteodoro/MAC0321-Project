package br.com.agendusp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.agendusp.agendusp.AgendUspApplication;
import br.com.agendusp.agendusp.MongoTestContainer;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventPollDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.dataobjects.eventObjects.Attendee;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;

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

    public void setupDatabase(){

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
    public void createTest(){
        setupDatabase();
         String startDate = "2025-07-01T20:00:0000Z";
         String endDate = "2025-07-20T20:00:0000Z";
        eventPollDataController.create(eventId, startDate, endDate);

        
    }
}
