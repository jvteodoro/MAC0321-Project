package br.com.agendusp.agendusp;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.test.context.support.WithMockUser;

import com.google.gson.Gson;

import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.calendarControllers.CalendarDataController;
import br.com.agendusp.agendusp.controller.calendarControllers.LocalCalendarListController;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.User;

@SpringBootTest
//@ComponentScan(basePackages = "br.com.agendusp.agendusp")
public class LocalCalendarListTest extends MongoTestContainer {
    @Autowired
    LocalCalendarListController localCalendarListController;
    @Autowired
    OAuth2AuthorizedClient authorizedClient;
    @Autowired
    private Gson gson;

    @BeforeAll
    public static void setup() {
        mongoDBContainer.start();
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testAddCalendar() {
        User user = new User();
        user.setId("testuser");
        user.setEmail("test@email.com");

        CalendarResource calendar = new CalendarResource();
        calendar.setCalendarId("test-calendar-id");
        calendar.setSummary("Test Calendar");
        calendar.setDescription("This is a test calendar");
        calendar.setLocation("Test Location");
        calendar.setTimeZone("UTC");
        CalendarListResource calendarListResource = calendar.toCalendarListResource("writer");
        localCalendarListController.insert(calendarListResource, authorizedClient);
        CalendarListResource result = localCalendarListController.get("test-calendar-id", authorizedClient);
        assertEquals(gson.toJson(calendarListResource), gson.toJson(result));
    }
}
