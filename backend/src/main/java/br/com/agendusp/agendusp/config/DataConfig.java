package br.com.agendusp.agendusp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.agendusp.agendusp.controller.CalendarDataController;
import br.com.agendusp.agendusp.controller.EventsDataController;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.google.GoogleCalendarListController;
import br.com.agendusp.agendusp.controller.google.GoogleCalendarsController;
import br.com.agendusp.agendusp.controller.google.GoogleEventsController;

@Configuration
public class DataConfig {

    // @Autowired
    // private EventsRepository eventsRepository;
    // @Autowired
    // private CalendarRepository calendarListRepository;

    DataConfig() {}
    @Bean
    public CalendarDataController calendarDataController(){
        return new CalendarDataController();
    }
    @Bean
    public EventsDataController eventsDataController(){
        return new EventsDataController();
    }
    @Bean
    public UserDataController userDataController(){
        return new UserDataController();
    }
    @Bean
    public GoogleCalendarListController googleCalendarListController(){
        return new GoogleCalendarListController();
    }
    @Bean
    public GoogleEventsController googleEventsController(){
        return new GoogleEventsController();
    }
    @Bean
    public GoogleCalendarsController googleCalendarsController(){
        return new GoogleCalendarsController();
    }
}
