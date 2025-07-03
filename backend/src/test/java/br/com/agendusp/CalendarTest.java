package br.com.agendusp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agendusp.agendusp.MongoTestContainer;
import br.com.agendusp.agendusp.controller.FormsController;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.calendarControllers.CalendarDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.EventsRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

public class CalendarTest extends MongoTestContainer {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    EventsRepository eventsRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CalendarDataController calendarDataController;
    @Autowired
    EventsDataController eventsDataController;
    @Autowired
    UserDataController userDataController;
    @Autowired
    FormsController formsController;

    @Test
    @WithMockUser
    public void addCalendarListResourceFromCalendarTest() throws Exception {
        User user1 = new User();
        user1.setId("afadsfsf@ajodjf.com");
        user1.setGoogleId(user1.getId());
        userRepository.delete(user1);
        userRepository.insert(user1);
        System.out.println(objectMapper.writeValueAsString(user1));
        CalendarResource calR = new CalendarResource();
        calR.setCalendarId("1231233@gmail.com");
        calR.setId(calR.getCalendarId());
        System.out.println("Usuario: " + user1.getId());
        System.out.println("\n\nCalendário passado para addCalendar: " + objectMapper.writeValueAsString(calR));
        calendarDataController.removeCalendar(calR.getCalendarId(), user1.getId());
        calendarDataController.addCalendar(calR, user1.getId());

        // TODO pois nunca é usado
        CalendarListResource response = calendarDataController.addCalendarListResourceFromCalendar(user1.getId(),
                calR.getCalendarId());
        CalendarListResource fetched = calendarDataController.getCalendarListResource(calR.getCalendarId(),
                user1.getId());
        assertEquals(objectMapper.writeValueAsString(calR.toCalendarListResource()),
                objectMapper.writeValueAsString(fetched));
    }

    @Test
    @WithMockUser
    public void testCalendarDataController() throws Exception {
        User user1 = new User();
        user1.setUsername("user");
        user1.setId("12");

        CalendarListResource calItem = new CalendarListResource();
        calItem.setCalendarId("teste@gmail.com");
        calItem.setId("1");

        CalendarResource calResource = new CalendarResource();
        calResource.setId("1");

        CalendarListResource calListResource = new CalendarListResource();
        calListResource.setId("1");

        String userId = user1.getId();
        System.out.println("USer ID: " + userId);
        String calendarId = calItem.getCalendarId();
        // TODO pois nunca é usado
        String calendarResourceId = calResource.getId();
        String calendarListResourceId = calListResource.getId();

        userRepository.insertCalendarListResourceByUserId(user1.getId(), calItem);

        // adiciona o calendarlistusaritem
        CalendarListResource findedCalItem = calendarDataController.addCalendarListResource(calListResource, userId);
        System.out.println(objectMapper.writeValueAsString(findedCalItem));

        // adiciona o calendar
        CalendarResource calRes = calendarDataController.addCalendar(calResource, userId);
        System.out.println(objectMapper.writeValueAsString(calRes));

        // pega lista de calendarios

        CalendarListResource calListResource1 = calendarDataController.getCalendarListResource(calendarListResourceId,
                userId);

        System.out.println(objectMapper.writeValueAsString(calListResource1));

        // atualiza o calendar

        CalendarListResource calListResource2 = calendarDataController.updateCalendar(calendarId, calListResource,
                userId);
        System.out.println(objectMapper.writeValueAsString(calListResource2));

        // pega lista de calendarios do usuario
        ArrayList<CalendarListResource> calListResource3 = calendarDataController.getCalendarList(userId);

        System.out.println(objectMapper.writeValueAsString(calListResource3));

        // deleta e vê se apagou
        calendarDataController.removeCalendar(calendarId, userId);

        // TODO pois nunca é usado
        CalendarListResource calListDelete = calendarDataController.getCalendarListResource(calendarId, userId);

        // System.out.println("DELETADO COM SUCESSO");

        userRepository.deleteById("12");
        // CalendarListResource.deleteById("1");
        // CalendarResource.deleteById("1");
        // CalendarListResource.deleteById("1");

    }

    @Test
    @WithMockUser
    public void testCalendarListInsert() {

    }

    @Test
    @WithMockUser
    public void testAddCalendarListResource() {
        User user = new User();
        String userId = "testUser";
        user.setId(userId);
        user.setGoogleId(userId);
        user.setDisplayName("Usuário de Test");
        userDataController.createUser(user);

        CalendarListResource calListR = new CalendarListResource();
        String calendarId = "calendario@teste.com";
        calListR.setId(calendarId);
        calListR.setCalendarId(calendarId);
        calListR.setOwner(user.getAsCalendarPerson());

        userRepository.addCalendarListResource(userId, calListR);
    }

    @Test
    @WithMockUser
    public void testAddCalendar() throws Exception {
        User user1 = new User();
        user1.setUsername("user");
        user1.setId("12");
        userDataController.deleteUser(user1.getId());
        userDataController.createUser(user1);
        String userId = user1.getId();
        String calendarId = "teste4@gmail.com";

        CalendarResource calR = new CalendarResource();
        calR.setCalendarId(calendarId);
        calR.setId(calendarId);
        calR.setOwner(user1.getAsCalendarPerson());
        calendarDataController.removeCalendar(calendarId, userId);
        calendarDataController.addCalendar(calR, userId);

        User user2 = userDataController.findUser(userId);
        System.out.println(objectMapper.writeValueAsString(user2));

    }

}
