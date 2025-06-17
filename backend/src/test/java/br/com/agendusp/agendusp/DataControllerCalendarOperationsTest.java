package br.com.agendusp.agendusp;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.test.context.support.WithMockUser;

import com.google.gson.Gson;

import br.com.agendusp.agendusp.controller.CalendarDataController;
import br.com.agendusp.agendusp.controller.DataController;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarListUserItem;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.User;

@SpringBootTest
@ComponentScan(basePackages = "br.com.agendusp.agendusp")
public class DataControllerCalendarOperationsTest extends MongoTestContainer {
    @Autowired
    private DataController dataController;
    @Autowired
    private UserDataController userDataController;
    @Autowired
    private CalendarDataController calendarDataController;
    @Autowired
    private Gson gson;

    private String userID = "testuser";

    @BeforeAll
    public static void setup() {
        mongoDBContainer.start();
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testAddCalendar(){
        User user = new User();
        user.setId("testuser");
        user.setEmail("test@email.com");
        userDataController.createUser(user);

        CalendarResource calendar = new CalendarResource();
        calendar.setCalendarId("test-calendar-id");
        calendar.setSummary("Test Calendar");
        calendar.setDescription("This is a test calendar");
        calendar.setLocation("Test Location");
        calendar.setTimeZone("UTC");
        CalendarResource result = this.dataController.addCalendar(calendar, this.userID);
        assertEquals(gson.toJson(calendar),gson.toJson(result));
    }

    @Test
    public void testGetCalendar(){
        User user = new User();
        user.setId("testuser2");
        userDataController.createUser(user);
        CalendarResource calendar = new CalendarResource();
        calendar.setCalendarId("test-calendar-id-2");
        calendar.setSummary("Test Calendar 2");
        calendar.setDescription("This is a test calendar 2");
        calendar.setLocation("Test Location 2");
        calendar.setTimeZone("UTC");
        CalendarResource createdCalendar = calendarDataController.addCalendar(calendar, "testuser2");
        //Necessário mudar para CalendarResource
        CalendarListResource fetchedCalendar =calendarDataController.getCalendarListResource(createdCalendar.getCalendarId(), "testuser2");
        assertEquals(gson.toJson(calendar), gson.toJson(fetchedCalendar));
    }   

    @Test
    public void testUpdateCalendar() {
        User user = new User();
        user.setId("testuser3");
        userDataController.createUser(user);
        CalendarResource calResource = new CalendarResource();
        calResource.setCalendarId("test-calendar-id-3");
        calResource.setSummary("Test Calendar 3");
        calResource.setDescription("This is a test calendar 3");
        calResource.setLocation("Test Location 3");
        calResource.setTimeZone("UTC");
        calResource.setOwner(user.getAsCalendarPerson());
        CalendarResource createdCalendar =calendarDataController.addCalendar(calResource, "testuser3");

        CalendarListUserItem calListUserItem = new CalendarListUserItem(calResource.getCalendarId(), "", "", "", false, false, "owner");

        boolean primary = calResource.getOwner().getId().equals("testuser3");
        CalendarListResource createdCalListResource = new CalendarListResource(calResource.getCalendarId(), primary, calListUserItem, calResource);

        createdCalendar.setSummary("Updated Test Calendar 3");
        CalendarListResource updatedCalendar =calendarDataController.updateCalendar(createdCalendar.getCalendarId(), createdCalListResource, "testuser3");
        
        assertEquals("Updated Test Calendar 3", updatedCalendar.getSummary());
    }
    @Test
    public void testGetCalendars() throws Exception {
        User user = new User();
        user.setId("testuser5");
        userDataController.createUser(user);
        CalendarResource calendar1 = new CalendarResource();
        calendar1.setCalendarId("test-calendar-id-5-1");
        calendar1.setSummary("Test Calendar 5-1");
        calendar1.setDescription("This is a test calendar 5-1");
        calendar1.setLocation("Test Location 5-1");
        calendar1.setTimeZone("UTC");
       calendarDataController.addCalendar(calendar1, "testuser5");

        CalendarResource calendar2 = new CalendarResource();
        calendar2.setCalendarId("test-calendar-id-5-2");
        calendar2.setSummary("Test Calendar 5-2");
        calendar2.setDescription("This is a test calendar 5-2");
        calendar2.setLocation("Test Location 5-2");
        calendar2.setTimeZone("UTC");
       calendarDataController.addCalendar(calendar2, "testuser5");

        ArrayList<CalendarListResource> calendars =calendarDataController.getCalendars("testuser5");

        assertEquals(2, calendars.size());
    }


    @Test
    public void testRemoveCalendar() throws Exception {
        User user = new User();
        user.setId("testuser4");
        userDataController.createUser(user);
        CalendarResource calendar = new CalendarResource();
        calendar.setCalendarId("test-calendar-id-4");
        calendar.setSummary("Test Calendar 4");
        calendar.setDescription("This is a test calendar 4");
        calendar.setLocation("Test Location 4");
        calendar.setTimeZone("UTC");
        CalendarResource createdCalendar =calendarDataController.addCalendar(calendar, "testuser4");

       calendarDataController.removeCalendar(createdCalendar.getCalendarId(), "testuser4");

        ArrayList<CalendarListResource> removedCalendar =calendarDataController.getCalendars("testuser4");
        
        assertEquals(Optional.empty(), removedCalendar);
    }
    

}
