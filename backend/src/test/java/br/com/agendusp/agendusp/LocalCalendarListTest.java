package br.com.agendusp.agendusp;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

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

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testListCalendarsListResources() {
        User user = new User();
        user.setId("testuser");
        user.setEmail("test@email.com");

        CalendarResource calendar1 = new CalendarResource();
        calendar1.setCalendarId("test-calendar-id1");

        CalendarResource calendar2 = new CalendarResource();
        calendar2.setCalendarId("test-calendar-id2");

        CalendarResource calendar3 = new CalendarResource();
        calendar3.setCalendarId("test-calendar-id3");
        
        CalendarListResource calendarListResource1 = calendar1.toCalendarListResource("writer");
        CalendarListResource calendarListResource2 = calendar2.toCalendarListResource("writer");
        CalendarListResource calendarListResource3 = calendar3.toCalendarListResource("writer");

        localCalendarListController.insert(calendarListResource1, authorizedClient);
        localCalendarListController.insert(calendarListResource2, authorizedClient);
        localCalendarListController.insert(calendarListResource3, authorizedClient);

        ArrayList<CalendarListResource> result = localCalendarListController.list(authorizedClient);
        ArrayList<CalendarListResource> expected = new ArrayList<>();
        expected.add(calendarListResource1);
        expected.add(calendarListResource2);
        expected.add(calendarListResource3);

        assertEquals(expected, result);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testGetUser() {
        User user = localCalendarListController.getUser(authorizedClient);
        assertEquals("testuser", user.getId());
        // Dependendo da implementação, pode ser necessário ajustar o campo verificado
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testDeleteCalendarListResource() {
        CalendarResource calendar = new CalendarResource();
        calendar.setCalendarId("calendar-to-delete");
        CalendarListResource calendarListResource = calendar.toCalendarListResource("writer");
        localCalendarListController.insert(calendarListResource, authorizedClient);

        // Certifica-se de que foi inserido
        CalendarListResource fetched = localCalendarListController.get("calendar-to-delete", authorizedClient);
        assertEquals(gson.toJson(calendarListResource), gson.toJson(fetched));

        // Deleta e verifica se foi removido
        localCalendarListController.delete("calendar-to-delete", authorizedClient);
        CalendarListResource deleted = localCalendarListController.get("calendar-to-delete", authorizedClient);
        assertEquals(null, deleted);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testUpdateCalendarListResource() {
        CalendarResource calendar = new CalendarResource();
        calendar.setCalendarId("calendar-to-update");
        calendar.setSummary("Original Summary");
        CalendarListResource calendarListResource = calendar.toCalendarListResource("writer");
        localCalendarListController.insert(calendarListResource, authorizedClient);

        // Atualiza o summary
        calendarListResource.setSummary("Updated Summary");
        CalendarListResource updated = localCalendarListController.update(calendarListResource, authorizedClient);

        assertEquals("Updated Summary", updated.getSummary());
        // Busca novamente para garantir persistência
        CalendarListResource fetched = localCalendarListController.get("calendar-to-update", authorizedClient);
        assertEquals("Updated Summary", fetched.getSummary());
    }


}
