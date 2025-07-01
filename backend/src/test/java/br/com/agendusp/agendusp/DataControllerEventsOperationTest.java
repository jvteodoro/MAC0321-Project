// package br.com.agendusp.agendusp;

// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import br.com.agendusp.agendusp.controller.DataController;
// import br.com.agendusp.agendusp.documents.CalendarResource;
// import br.com.agendusp.agendusp.documents.EventsResource;
// import br.com.agendusp.agendusp.documents.User;

// @SpringBootTest
// public class DataControllerEventsOperationTest extends MongoTestContainer {

// @Autowired
// private DataController dataController;

// @BeforeAll
// public static void setup() {
// mongoDBContainer.start();
// }
// @BeforeEach
// public void init(){
// CalendarResource calendar = new CalendarResource();
// calendar.setCalendarId("test-calendar-id");
// calendar.setSummary("Test Calendar");
// calendar.setDescription("This is a test calendar");
// calendar.setLocation("Test Location");
// calendar.setTimeZone("UTC");
// dataController.addCalendar(calendar, "testuser");
// }

// @Test
// public void testCreatEvent(){
// User user = new User();
// user.setId("testuser");
// user.setEmail("test@mail.com");
// dataController.createUser(user);
// EventsResource event = new EventsResource();
// event.setId("test-event-id");
// event.setSummary("Test Event");
// event.setDescription("This is a test event");
// event.setLocation("Test Location");
// dataController.createEvent("test-calendar-id", event, "testuser");
// }

// }
