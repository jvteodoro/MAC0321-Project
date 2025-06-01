package br.com.agendusp.agendusp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.repositories.CalendarRepository;


@SpringBootTest
public class MongoTest extends MongoTestContainer {
  
    @Autowired
    private CalendarRepository calendarListRepository;

    @Test
    public void test(){
        CalendarListResource calendar = new CalendarListResource();
        calendar.setId("test-calendar-id");
        calendarListRepository.insert(calendar);
        CalendarListResource fetchedCalendar = calendarListRepository.findById("test-calendar-id").orElse(null);
        System.err.println("Fetched Calendar: " + (fetchedCalendar != null ? fetchedCalendar.getId() : "Not Found"));

        }
}
