package br.com.agendusp.agendusp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.agendusp.agendusp.controller.CalendarDataController;
import br.com.agendusp.agendusp.controller.EventsDataController;
import br.com.agendusp.agendusp.controller.UserDataController;

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
}
