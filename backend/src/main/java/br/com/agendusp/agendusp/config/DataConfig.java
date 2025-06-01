package br.com.agendusp.agendusp.config;

import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.EventsRepository;
import br.com.agendusp.agendusp.repositories.UserCalendarListResourceAccessRelationRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import br.com.agendusp.agendusp.calendar.DataController;

@Configuration
public class DataConfig {

    private final UserCalendarListResourceAccessRelationRepository userCalendarListResourceAccessRelationRepository;
    private final EventsRepository eventsRepository;
    private final CalendarRepository calendarListRepository;

    DataConfig(CalendarRepository calendarListRepository, 
            EventsRepository eventsRepository, 
            UserCalendarListResourceAccessRelationRepository userCalendarListResourceAccessRelationRepository) {
        this.calendarListRepository = calendarListRepository;
        this.eventsRepository = eventsRepository;
        this.userCalendarListResourceAccessRelationRepository = userCalendarListResourceAccessRelationRepository;
    }

    @Bean
    public DataController dataController() {
        return new DataController(calendarListRepository, 
            eventsRepository, 
            userCalendarListResourceAccessRelationRepository);
    }
}
