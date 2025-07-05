package br.com.agendusp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agendusp.agendusp.AgendUspApplication;
import br.com.agendusp.agendusp.MongoTestContainer;
import br.com.agendusp.agendusp.controller.FormsController;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;

@SpringBootTest(classes = AgendUspApplication.class)
@AutoConfigureMockMvc
public class FormsControllerTest extends MongoTestContainer {

    @Autowired
    FormsController formsController;
    @Autowired
    UserDataController userDataController;
    @Autowired
    EventsDataController eventsDataController;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

    static String userId = "testUser";
    static String userEmail = "test@gmail.com";
    static String userName = "Usuário de Teste";

    static String attende1Id = "at1";
    static String attende2Id = "at2";
    static String attende3Id = "at3";

    static ArrayList<String> attendeeId;

    static String eventId = "testEvent";

    public void createAtendee(String attendeId) {
        User attendee = new User(attendeId, attendeId, attendeId);
        userDataController.createUser(attendee);
    }
    public void setupDataBase() {
        System.out.println("Setup dataBase");
        User user = new User(userId, userEmail, userName);



        attendeeId = new ArrayList<>();
        createAtendee(attende1Id);
        attendeeId.add(attende1Id);

        createAtendee(attende2Id);
        attendeeId.add(attende2Id);

        createAtendee(attende3Id);
        attendeeId.add(attende3Id);

        userDataController.createUser(user);

        EventsResource event = new EventsResource();
        event.setId(eventId);
        try {
            System.out.println(objectMapper.writeValueAsString(user));
        } catch (Exception e) {
        }
        eventsDataController.createEvent(user.getCalendarList().get(0).getId(), event, user.getId());
        eventsDataController.addAtendeeToEvent(eventId, attende1Id);
        eventsDataController.addAtendeeToEvent(eventId, attende2Id);
        eventsDataController.addAtendeeToEvent(eventId, attende3Id);

    }

    @Test
    @WithMockUser
    public void sendPoolTest() throws Exception {
        setupDataBase();
        EventsResource event = eventsDataController.getEventById(eventId);
        System.out.println(objectMapper.writeValueAsString(userDataController.findUser(userId)));
        System.out.println("Event:\n" + objectMapper.writeValueAsString(event));
        formsController.sendPool(eventId);
        User atendee1 = userDataController.findUser(attende1Id);
        User atendee2 = userDataController.findUser(attende2Id);
        User atendee3 = userDataController.findUser(attende3Id);

        System.err.println(objectMapper.writeValueAsString(atendee1.getEventPoolNotifications()));


    }

    @Test
    @WithMockUser
    public void voteEventPoolTest() throws Exception {
        mockMvc.perform(get("/test")).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser
    public void createEventFromPoolTest() {

    }

    @Test
    @WithMockUser
    public void createPoolTest() throws Exception {
        String startDate = "2025-06-22T12:23:00Z";
        String endDate = "2025-06-25T13:34:00Z";

        EventsResource event = eventsDataController.getEventById(eventId);

        ResultActions result = mockMvc.perform(
                post("/pool/create?startDate=" + startDate + "&endDate=" + endDate)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(get("/secured")).andDo(MockMvcResultHandlers.print());
        //System.out.println("RESULT: "+result.getResponse().getContentAsString());

    }

    @Test
    @WithMockUser
    public void voteTest() {

    }

    @Test
    @WithMockUser
    public void createEventTest() {

    }
}
