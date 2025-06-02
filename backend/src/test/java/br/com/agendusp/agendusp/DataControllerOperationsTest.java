package br.com.agendusp.agendusp;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import com.google.gson.Gson;

import br.com.agendusp.agendusp.controller.DataController;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.User;

@SpringBootTest
@ComponentScan(basePackages = "br.com.agendusp.agendusp")
public class DataControllerOperationsTest extends MongoTestContainer {
    @Autowired
    private DataController dataController;
    @Autowired
    private Gson gson;

    private String userID = "testuser";

    @BeforeAll
    public void setup() {
        mongoDBContainer.start();
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testAddCalendar(){
        User user = new User();
        user.setId("testuser");
        user.setEmail("test@email.com");
        dataController.createUser(user);

        CalendarResource calendar = new CalendarResource();
        calendar.setCalendarId("test-calendar-id");
        calendar.setSummary("Test Calendar");
        calendar.setDescription("This is a test calendar");
        calendar.setLocation("Test Location");
        calendar.setTimeZone("UTC");
        CalendarResource result = this.dataController.addCalendar(calendar, this.userID);
        assertEquals(gson.toJson(calendar),gson.toJson(result));
    }
    

}
