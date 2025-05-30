package br.com.agendusp.agendusp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import br.com.agendusp.agendusp.calendar.DataController;

@Configuration
public class DataConfig {

    @Bean
    public DataController dataController() {
        return new DataController();
    }
}
