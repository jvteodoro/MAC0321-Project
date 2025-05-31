package br.com.agendusp.agendusp.config;

import br.com.agendusp.agendusp.repositories.CalendarListRepository;
import br.com.agendusp.agendusp.repositories.EventsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import br.com.agendusp.agendusp.calendar.DataController;

@Configuration
public class DataConfig {

    private final EventsRepository eventsRepository;
    private final CalendarListRepository calendarListRepository;

    DataConfig(CalendarListRepository calendarListRepository, EventsRepository eventsRepository) {
        this.calendarListRepository = calendarListRepository;
        this.eventsRepository = eventsRepository;
    }

    @Bean
    public DataController dataController() {
        return new DataController(calendarListRepository, eventsRepository);
    }
}
