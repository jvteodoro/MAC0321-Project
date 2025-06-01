package br.com.agendusp.agendusp.calendar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.services.calendar.Calendar.CalendarList;
import com.google.api.services.calendar.model.Event;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.documents.UserCalendarRelation;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.EventsRepository;
import br.com.agendusp.agendusp.repositories.UserCalendarListResourceAccessRelationRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

public class DataController implements AbstractDataController {

    private final CalendarRepository calendarRepository;
    private final EventsRepository eventsRepository;
    private final UserCalendarListResourceAccessRelationRepository userCalendarListResourceAccessRelationRepository;
    @Autowired
    private UserRepository userRepository;

    public DataController(CalendarRepository calendarRepository, EventsRepository eventsRepository,
            UserCalendarListResourceAccessRelationRepository userCalendarListResourceAccessRelationRepository) {
        this.calendarRepository = calendarRepository;
        this.eventsRepository = eventsRepository;
        this.userCalendarListResourceAccessRelationRepository = userCalendarListResourceAccessRelationRepository;
    }

    // Calendars
    @Override
    public void addCalendarListResource(CalendarListResource calResource, String userId, String accessRole) {
        if (calResource == null || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        else if (userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Calendário com ID '" + calResource.getId() + "' já existe.");
        }
        else {
            userRepository.addUserCalendarList(userId, calResource);
        }
    }
    @Override
    public void addCalendar(CalendarResource calResource, String userId) {
        if (calResource == null || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        if (calendarRepository.existsById(calResource.getId())) {
            throw new IllegalArgumentException("Calendário com ID '" + calResource.getId() + "' já existe.");
        } else {
            calendarRepository.save(calResource);
            CalendarListResource calListResource = new CalendarListResource();
            calListResource.setAccessRole("owner");
            userRepository.addUserCalendarList(userId, calListResource);        }
    }
    @Override
    public CalendarListResource getCalendar(String calendarId, String userId){
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        CalendarListResource calendar = userRepository.findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
        .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado para o usuário de ID '" + userId + "'."));
        return calendar;
    }

    
    @Override
public CalendarListResource updateCalendar(String calendarId, CalendarListResource calResource, String userId) {
    CalendarListResource calendar = calendarRepository
        .findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
        .orElseThrow(() -> new IllegalArgumentException(
            "Calendário com ID '" + calendarId + "' não encontrado para o usuário de ID '" + userId + "'"));

    if (calendar.getAccessRole() == "owner" || calendar.getAccessRole() == "writer") {
        calendarRepository.save(calResource);
        return calendar;
    }
    else {
        throw new IllegalArgumentException(
                "Acesso negado: o usuário não tem permissão para atualizar este calendário.");
    }
}

    @Override
    public CalendarListResource patchCalendar(String calendarId, CalendarListResource calResource, String userId) {
        CalendarListResource calendar = calendarRepository
                .findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Calendário com ID '" + calendarId + "' não encontrado para o usuário de ID '" + userId));

        if (calendar.getAccessRole() == "owner" || calendar.getAccessRole() == "writer") {
            if (calResource.getSummary() != null){
                calendar.setSummary(calResource.getSummary());
            }
            if (calResource.getDescription() != null){
                calendar.setDescription(calResource.getDescription());
            }
            if (calResource.getLocation() != null){
                calendar.setLocation(calResource.getLocation());
            }
            if (calResource.getTimeZone() != null){
                calendar.setTimeZone(calResource.getTimeZone());
            }
            if (calResource.getAccessRole() != null){
                calendar.setAccessRole(calResource.getAccessRole());
            }

            calendarRepository.save(calendar);
            return calendar;
        }

        throw new IllegalArgumentException(
                "Acesso negado: o usuário não tem permissão para atualizar este calendário.");
    }

    @Override
    public ArrayList<CalendarListResource> getCalendars(String userId) throws Exception {

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo ou vazio.");
        }
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Usuário com ID '" + userId + "' não encontrado.");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Usuário com ID '" + userId + "' não encontrado."));
        return user.getCalendarList();
    }

    @Override
    public void removeCalendar(String calendarId, String userId) {

        CalendarListResource calResource = userRepository
        .findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
        .orElseThrow((() -> new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado para o usuário de ID '" + userId + "'.")));

        if (calResource.getAccessRole() == "owner"){
            calendarRepository.deleteById(calendarId);
            userRepository.refreshLinks(calendarId);   
        } else {
            userRepository.deleteCalendarListResourceById(userId, calendarId);
        }
    }

    // Events
    @Override
    public void addEvent(String calendarId, EventsResource eventResource,
            String userId, String accessRole) {
        if (eventResource == null || calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Evento, ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        else if (!calendarListRepository.existsById(calendarId)) {
            throw new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado.");
        } else {
            eventResource.increaseLinks();
            eventsRepository.save(eventResource);
            UserCalendarRelation relation = new UserCalendarRelation(userId, calendarId, accessRole);
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
    public EventsResource updateEvent(String calendarId, String eventId, EventsResource eventResource, String userId) {
        CalendarListResource calendar = calendarListRepository.findById(calendarId).orElse(null);
        EventsResource event = eventsRepository.findById(eventId).orElse(null);
        if (calendar == null) {
            throw new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado.");
        }
        if (event == null) {
            throw new IllegalArgumentException("Evento com ID '" + eventId + "' não encontrado.");
        }
        eventResource.setId(eventId);
        eventResource.setCalendarId(calendarId);
        eventsRepository.save(eventResource);
        return eventResource;
    }    
    

    @Override
    public EventsResource patchEvent(String calendarId, String eventId, EventsResource eventResource, String userId) {
        CalendarListResource calendar = calendarListRepository.findById(calendarId).orElse(null);
        EventsResource event = eventsRepository.findById(eventId).orElse(null);
        if (calendar == null) {
            throw new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado.");
        }
        if (event == null) {
            throw new IllegalArgumentException("Evento com ID '" + eventId + "' não encontrado.");
        }
        if (eventResource.getStart() !=null) {
            event.setStart(eventResource.getStart());
        }
        if (eventResource.getEnd() !=null) {
            event.setEnd(eventResource.getEnd());
        }
        if (eventResource.getSummary() !=null) {
            event.setSummary(eventResource.getSummary());
        }
        if (eventResource.getLocation() !=null) {
            event.setLocation(eventResource.getLocation());
        }
        if (eventResource.getAttendees() !=null) {
            event.setAttendees(eventResource.getAttendees());
        }
        eventsRepository.save(event);
        return event;
    }

    @Override
    public ArrayList<EventsResource> getEvents(String calendarId,
            String userId) {

        CalendarListResource calResource = userRepository.findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
        .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado para o usuário de ID '" + userId + "'."));

        ArrayList<EventsResource> events = eventsRepository.findAllBycalendarId(calResource.getId())
        .orElseThrow(() -> new IllegalArgumentException("Nenhum evento encontrado para o calendário com ID '" + calendarId + "'."));

        return events;
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
