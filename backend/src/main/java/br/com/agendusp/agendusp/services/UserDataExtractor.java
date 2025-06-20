package br.com.agendusp.agendusp.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.agendusp.agendusp.documents.CalendarListUserItem;
import br.com.agendusp.agendusp.documents.User;

import br.com.agendusp.agendusp.repositories.EventsRepository;
import br.com.agendusp.agendusp.documents.EventsResource;
import java.util.Optional;

import java.util.ArrayList;


@Service
public class UserDataExtractor {


    @Autowired
    private EventsRepository eventsRepository;

    public ArrayList<String> extraiCompromissos(User user) {
        ArrayList<String> compromissos = new ArrayList<>();

        ArrayList<CalendarListUserItem> calendars = user.getCalendarList();
        if (calendars == null || calendars.isEmpty()) {
            return compromissos;  // Retorna lista vazia
        }

        for (CalendarListUserItem calendar : calendars) {
            Optional<ArrayList<EventsResource>> optionalEvents = eventsRepository.findAllByCalendarId(calendar.getCalendarId());

            if (optionalEvents.isPresent()) {
                ArrayList<EventsResource> events = optionalEvents.get();
                for (EventsResource event : events) {
                    compromissos.add(eventsResourceToString(event));
                }
            }
        }
        return compromissos;
    }
    //public ArrayList<String> extraiEnquetes(User user) {}
    //public ArrayList<String> extraiEventosCancelados(User user) {}

    private String eventsResourceToString(EventsResource event) {
        StringBuilder eventString = new StringBuilder();
        eventString.append(event.getKind() + " no horário " + event.getStart().dateTime() + " do dia " + event.getStart().date());
        //pode ser modificado
        return eventString.toString();
    }
    private String enquetesToString() {
        return "a ser implementada";
    }
    private String cancelledEventsToString(EventsResource event) {
        return "a ser implementada";
    }
}
