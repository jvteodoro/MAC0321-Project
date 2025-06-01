package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.CalendarListRepository;
import br.com.agendusp.agendusp.repositories.EventsRepository;
import br.com.agendusp.agendusp.repositories.UserCalendarListResourceAccessRelationRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

public class DataController implements AbstractDataController {

    private final CalendarListRepository calendarListRepository;
    private final EventsRepository eventsRepository;
    private final UserCalendarListResourceAccessRelationRepository userCalendarListResourceAccessRelationRepository;
    @Autowired
    private UserRepository userRepository;

    public DataController(CalendarListRepository calendarListRepository, EventsRepository eventsRepository,
            UserCalendarListResourceAccessRelationRepository userCalendarListResourceAccessRelationRepository) {
        this.calendarListRepository = calendarListRepository;
        this.eventsRepository = eventsRepository;
        this.userCalendarListResourceAccessRelationRepository = userCalendarListResourceAccessRelationRepository;
    }

    // Calendars
    @Override
    public void addCalendar(CalendarListResource calResource, String userId) {
        if (calResource == null || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        else if (calendarListRepository.existsById(calResource.getId())) {
            throw new IllegalArgumentException("Calendário com ID '" + calResource.getId() + "' já existe.");
        }
        else {
            UserCalendarListRelation relation = new UserCalendarListRelation(userId, calResource.getId(), calResource.getAccessRole())
            calendarListRepository.save(calResource);
            if (userRepository.existsById(userId)) {
                userRepository.updateUserCalendarList(userId, relation);
            } else {
                User user = new User();
            }
        }
    }

    @Override
    public CalendarListResource getCalendar(String calendarId, String userId) {
        List<UserCalendarListRelation> relations = userCalendarListResourceAccessRelationRepository
                .findAllByUserId(userId) //coloca em relations todas asa relaçoes do usurio com algum calendarion
                .orElseThrow(() -> new IllegalArgumentException("O usuário não possui calendários"));
        for (UserCalendarListRelation relation : relations) {
            CalendarListResource calendar = calendarListRepository.findById(relation.getCalendarId()).orElse(null);
            if (calendar.getId().equals(calendarId)) {
                return calendar; // Retorna o calendário se encontrado
            }
        }
        throw new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado.");
    }

    @Override
    public CalendarListResource updateCalendar(String calendarId, CalendarListResource calResource,
            String userId) {
        // Implementação para atualizar um calendário
        return null;
    }

    @Override
    public CalendarListResource patchCalendar(String calendarId, CalendarListResource calResource,
            String userId) {
        // Implementação para atualizar parcialmente um calendário
        return null;
    }

    @Override
    public CalendarListResource[] getCalendars(String userId) throws Exception {
        List<UserCalendarListRelation> relations = userCalendarListResourceAccessRelationRepository
                .findAllByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("O usuário não possui calendários"));
        List<CalendarListResource> calendars = new ArrayList<>();
        for (UserCalendarListRelation relation : relations) {
            CalendarListResource calendar = calendarListRepository.findById(relation.getCalendarId()).orElse(null);
            if (calendar != null) {
                calendars.add(calendar);
            }
        }
        return calendars.toArray(new CalendarListResource[0]);
    }

    @Override
    public void removeCalendar(String calendarId, String userId) {
        if (calendarListRepository.existsById(calendarId)) {
            calendarListRepository.deleteById(calendarId);
        } else {
            throw new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado.");
        }
    }

    // Events
    @Override
    public void addEvent(String calendarId, EventsResource eventResource,
            String userId) {
        if (eventResource == null || calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Evento, ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        else if (!calendarListRepository.existsById(calendarId)) {
            throw new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado.");
        } else {
            eventResource.setCalendarId(calendarId);
            eventsRepository.save(eventResource);
            UserCalendarListRelation relation = new UserCalendarListRelation(userId, calendarId);
            userCalendarListResourceAccessRelationRepository.save(relation);
        }
    }

    @Override
    public EventsResource getEvent(String eventId, String calendarId,
            String userId) {
            EventsResource event = eventsRepository.findById(eventId)
            .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId + "' não encontrado."));
                for (Attendee attendee : event.getAttendees()) {
                    if (attendee.calendarPerson.id() == userId) {
                        return event; // Retorna o evento se encontrado
                    }
                }  
            throw new IllegalArgumentException("Evento com ID '" + eventId + "' não encontrado para o usuário de ID '" + calendarId + "'.");
    }

    @Override
    public EventsResource updateEvent(String calendarId, String eventId, EventsResource eventResource,
            String userId) {
        //implementação para atualizar um evento
        return null;
        
    
    }
    

    @Override
    public EventsResource patchEvent(String calendarId, String eventId, EventsResource eventResource,
            String userId) {
        // Implementação para atualizar parcialmente
        return null;
    }

    @Override
    public EventsResource[] getEvents(String calendarId,
            String userId) {
        List<EventsResource> allEvents = eventsRepository.findAll();
        List<EventsResource> userEvents = new ArrayList<>();

        for (EventsResource event : allEvents)
            for (Attendee attendee : event.getAttendees())
                if (attendee.calendarPerson.id() == userId)
                    userEvents.add(event);
        return userEvents.toArray(new EventsResource[0]);
    }

    @Override
    public void removeEvent(String eventId, String calendarId, String userId) {
        EventsResource event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId + "' não encontrado."));

        // if (!event.getCalendarId().equals(calendarId)) { // Alterar checagem de posse
        // throw new IllegalArgumentException("Evento não pertence ao calendário
        // informado.");
        // }

        eventsRepository.deleteById(eventId);
    }

    @Override
    public void cancelEvent(String eventId, String calendarId, String userId) {

        EventsResource event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId + "' não encontrado."));
        event.setStatus("cancelled");

        eventsRepository.save(event);
   
    }
}
