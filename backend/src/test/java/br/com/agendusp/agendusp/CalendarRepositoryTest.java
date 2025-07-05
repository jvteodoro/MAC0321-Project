package br.com.agendusp.agendusp;

import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CalendarRepositoryTest {

    @Autowired
    private CalendarRepository calendarRepository;

    @Test
    public void testFindByCalendarId() {
        
        String calendarId = "test-calendar-id";
        CalendarResource calendar = new CalendarResource();
        calendar.setCalendarId(calendarId);
        calendar.setId(calendarId);
        calendar.setSummary("Test Calendar");
        calendarRepository.save(calendar);

    
        Optional<CalendarResource> found = calendarRepository.findByCalendarId(calendarId);

        
        assertTrue(found.isPresent());
        assertEquals(calendarId, found.get().getCalendarId());

        calendarRepository.deleteById(calendarId);
    }

    @Test
    public void testFindById() {

        String calendarId = "test-calendar-id-2";
        CalendarResource calendar = new CalendarResource();
        calendar.setCalendarId(calendarId);
        calendar.setId(calendarId);
        calendar.setSummary("Test Calendar 2");
        calendarRepository.save(calendar);

        Optional<CalendarResource> found = calendarRepository.findById(calendarId);

        assertTrue(found.isPresent());
        assertEquals(calendarId, found.get().getId());

        calendarRepository.deleteById(calendarId);
    }

    /*
    @Test
    public void testInsertCalendarListResource() {
        // CalendarListResource calendarListResource = new CalendarListResource();
        // calendarListResource.setCalendarId("insert-id");
        // CalendarListResource inserted = calendarRepository.insert(calendarListResource);
        // assertEquals("insert-id", inserted.getCalendarId());
    }
    */
}
