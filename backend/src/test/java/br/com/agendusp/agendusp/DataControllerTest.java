package br.com.agendusp.agendusp;

import br.com.agendusp.agendusp.calendar.DataController;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DataControllerTest extends MongoTestContainer {

    @Autowired
    private DataController dataController;

    @Autowired
    private UserRepository userRepository;

    private String userID = "agendusp";
    private String calendarID = "primeiros";

    private CalendarListResource createCalendar() {
        CalendarListResource calendar = new CalendarListResource();
        calendar.setId(calendarID);
        calendar.setSummary("Bolo de Banana");
        calendar.setDescription("Use uma forma untada");
        calendar.setLocation("São Paulo, meu");
        calendar.setTimeZone("Espírito Santo");
        calendar.setAccessRole("writer");
        return calendar;
    }

    @Test
    public void testGet() {
        CalendarListResource calendar = createCalendar();
        dataController.addCalendar(calendar, userID);
        CalendarListResource fetched = dataController.getCalendar(calendarID, userID);
        assertNotNull(fetched);
        assertEquals("Bolo de Banana", fetched.getSummary());
    }

    @Test
    public void testInsert() {
        CalendarListResource calendar = createCalendar();
        dataController.addCalendar(calendar, userID);
        assertTrue(userRepository.existsByCalendarId(userID, calendarID));
    }


    @Test
    public void testList() {
        CalendarListResource calendar = createCalendar();
        dataController.addCalendar(calendar, userID);
        List<CalendarListResource> calendars = dataController.getCalendars(userID);
        assertFalse(calendars.isEmpty());
        assertTrue(calendars.stream().anyMatch(c -> calendarID.equals(c.getId())));
    }

    @Test
    public void testDelete() {
        CalendarListResource calendar = createCalendar();
        dataController.addCalendar(calendar, userID);
        assertTrue(userRepository.existsByCalendarId(userID, calendarID));
        dataController.removeCalendar(calendarID, userID);
        assertFalse(userRepository.existsByCalendarId(userID, calendarID));
    }

    @Test
    public void testUpdate() {
        CalendarListResource calendar = createCalendar();
        dataController.addCalendar(calendar, userID);
        calendar.setSummary("Bolo de Cenoura");
        calendar.setDescription("Caldinha de brigadeiro");
        CalendarListResource updated = dataController.updateCalendar(calendarID, calendar, userID);
        assertEquals("Bolo de Cenoura", updated.getSummary());
        assertEquals("Caldinha de brigadeiro", updated.getDescription());
    }

    @Test
    public void testPatch() {
        CalendarListResource calendar = createCalendar();
        dataController.addCalendar(calendar, userID);
        CalendarListResource patch = new CalendarListResource();
        patch.setId(calendarID);
        patch.setSummary("Bolo de Banana");
        CalendarListResource patched = dataController.patchCalendar(calendarID, patch, userID);
        assertEquals("Bolo de Banana", patched.getSummary());
        assertEquals("Use uma forma untada", patched.getDescription());
    }
}

