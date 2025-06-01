package br.com.agendusp.agendusp.calendar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.services.calendar.Calendar.Events;
import com.google.api.services.calendar.Calendar.CalendarList;
import com.google.api.services.calendar.model.Event;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.documents.UserCalendarRelation;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.EventsRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

public class DataController extends AbstractDataController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CalendarRepository calendarRepository;
    @Autowired
    private EventsRepository eventsRepository;

    public DataController() {
    }

    // Calendars
    @Override
    public void addCalendarListResource(String calendarId, String userId) {
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListResource calListResource = new CalendarListResource();
        calListResource.setCalendarId(calendarId);

        CalendarResource calResource = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado."));
        String accessRole = getAccessRole(calResource, userId);

        calListResource.setAccessRole(accessRole);
        userRepository.addUserCalendarList(userId, calListResource);
    }

    @Override
    public void addCalendar(CalendarResource calResource, String userId) {
        if (calResource == null || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        if (calendarRepository.existsById(calResource.getCalendarId())) {
            throw new IllegalArgumentException("Calendário com ID '" + calResource.getCalendarId() + "' já existe.");
        }

        calendarRepository.save(calResource);
        this.addCalendarListResource(calResource.getCalendarId(), userId);
    }

    @Override
    public CalendarListResource getCalendar(String calendarId, String userId) {
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListResource calendar = userRepository.findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                        + "' não encontrado para o usuário de ID '" + userId + "'."));
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
        User user = findUser(userId);

        return user.getCalendarList();
    }

    @Override
    public void removeCalendar(String calendarId, String userId) {
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListResource calResource = userRepository
                .findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
                .orElseThrow((() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                        + "' não encontrado para o usuário de ID '" + userId + "'.")));

        if (calResource.getAccessRole() == "owner") {
            calendarRepository.deleteById(calendarId);
            userRepository.refreshLinks(calendarId);
        } else {
            userRepository.deleteCalendarListResourceById(userId, calendarId);
        }
    }

    // Events
    @Override
    public EventsResource addEvent(String calendarId, EventsResource eventResource, String userId) {
        if (eventResource == null || eventResource.getEventId() == null || eventResource.getEventId().isEmpty()) {
            throw new IllegalArgumentException("Evento não pode ser nulo e deve ter um ID.");
        }
        User user = findUser(userId);

        CalendarListResource calResource = userRepository
                .findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                        + "' não encontrado para o usuário de ID '" + userId + "'."));

        String accessRole = calResource.getAccessRole();

        if (accessRole == null || (!accessRole.equals("owner") && !accessRole.equals("writer"))) {
            throw new IllegalArgumentException(
                    "Acesso negado: o usuário não tem permissão para adicionar eventos a este calendário.");
        }

        if (eventsRepository.existsById(eventResource.getEventId())) {
            throw new IllegalArgumentException("Evento com ID '" + eventResource.getEventId() + "' já existe.");
        }

        eventResource.setCalendarId(calendarId);
        eventResource.setCreator(user.getAsCalendarPerson());
        eventResource.setOrganizer(user.getAsCalendarPerson()); // Inicialmente, o criador é o organizador
        eventResource.setStatus("confirmed"); // Definindo o status do evento como confirmado

        return eventsRepository.insert(eventResource);
    }

    @Override
    public EventsResource getEvent(String eventId, String calendarId,
            String userId) {
        if (eventId == null || eventId.isEmpty() || calendarId == null || calendarId.isEmpty() || userId == null
                || userId.isEmpty()) {
            throw new IllegalArgumentException(
                    "ID do evento, ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListResource calResource = userRepository
                .findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                        + "' não encontrado para o usuário de ID '" + userId + "'."));

        EventsResource event = eventsRepository
                .findEventsResourceByEventIdAndCalendarId(eventId, calResource.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId
                        + "' não encontrado para o calendário com ID '" + calendarId + "'."));

        return event;
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
        if (eventResource.getStart() != null) {
            event.setStart(eventResource.getStart());
        }
        if (eventResource.getEnd() != null) {
            event.setEnd(eventResource.getEnd());
        }
        if (eventResource.getSummary() != null) {
            event.setSummary(eventResource.getSummary());
        }
        if (eventResource.getLocation() != null) {
            event.setLocation(eventResource.getLocation());
        }
        if (eventResource.getAttendees() != null) {
            event.setAttendees(eventResource.getAttendees());
        }
        eventsRepository.save(event);
        return event;
    }

    @Override
    public ArrayList<EventsResource> getEvents(String calendarId,
            String userId) {
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListResource calResource = userRepository
                .findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                        + "' não encontrado para o usuário de ID '" + userId + "'."));

        ArrayList<EventsResource> events = eventsRepository.findAllByCalendarId(calResource.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nenhum evento encontrado para o calendário com ID '" + calendarId + "'."));

        return events;
    }

    @Override
    public void removeEvent(String eventId, String calendarId, String userId) {
        if (eventId == null || eventId.isEmpty() || calendarId == null || calendarId.isEmpty() || userId == null
                || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do evento, ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListResource calResource = userRepository
                .findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                        + "' não encontrado para o usuário de ID '" + userId + "'."));

        String accessRole = calResource.getAccessRole();
        if (accessRole == null || (!accessRole.equals("owner") && !accessRole.equals("writer"))) {
            throw new IllegalArgumentException(
                    "Acesso negado: o usuário não tem permissão para remover eventos deste calendário.");
        }

        EventsResource event = eventsRepository
                .findEventsResourceByEventIdAndCalendarId(eventId, calResource.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId
                        + "' não encontrado para o calendário com ID '" + calendarId + "'."));

        eventsRepository.delete(event);
    }

    @Override
    public void cancelEvent(String eventId, String calendarId, String userId) {
        if (eventId == null || eventId.isEmpty() || calendarId == null || calendarId.isEmpty() || userId == null
                || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do evento, ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListResource calResource = userRepository
                .findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                        + "' não encontrado para o usuário de ID '" + userId + "'."));

        EventsResource event = eventsRepository
                .findEventsResourceByEventIdAndCalendarId(eventId, calResource.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId
                        + "' não encontrado para o calendário com ID '" + calendarId + "'."));

        if (event.getOrganizer().id() != userId) {
            throw new IllegalArgumentException("Acesso negado: o usuário não é o organizador deste evento.");
        }

        event.setStatus("cancelled");
        eventsRepository.save(event);
    }
}
