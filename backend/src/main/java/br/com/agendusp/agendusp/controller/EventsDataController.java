package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.agendusp.agendusp.dataobjects.Attendee;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.EventsRepository;

public class EventsDataController {
  // Events
    @Autowired
    EventsRepository eventsRepository;
    @Autowired
    UserDataController userDataController;
    @Autowired
    CalendarDataController calendarDataController;
    @Autowired
    CalendarRepository calendarRepository;

    public ArrayList<EventsResource> getEventsOnInterval(String calendarId, String endDate) {
            return eventsRepository.findEventsByEndDate(calendarId, endDate)
            .orElse(new ArrayList<EventsResource>());
    }

    public EventsResource createEvent(String calendarId, EventsResource eventResource, String userId) {
        if (eventResource == null || eventResource.getEventId() == null || eventResource.getEventId().isEmpty()) {
            throw new IllegalArgumentException("Evento não pode ser nulo e deve ter um ID.");
        }
        User user = userDataController.findUser(userId);

        CalendarListResource calResource = userDataController.findCalendarListResource(userId, calendarId);

        String accessRole = calResource.getAccessRole();

        if (accessRole == null || (!accessRole.equals("owner") && !accessRole.equals("writer"))) {
            throw new IllegalArgumentException(
                    "Acesso negado: o usuário não tem permissão para adicionar eventos a este calendário.");
        }

        if (eventsRepository.existsById(eventResource.getEventId())) {
            throw new IllegalArgumentException("Evento com ID '" + eventResource.getEventId() + "' já existe.");
        }

        eventResource.setMainCalendarId(calendarId);
        eventResource.addCalendarId(calendarId);
        eventResource.setCreator(user.getAsCalendarPerson());
        eventResource.setOrganizer(user.getAsCalendarPerson()); // Inicialmente, o criador é o organizador
        eventResource.addAttendee(new Attendee(user.getAsCalendarPerson(), true)); // Adicionando o criador como
                                                                                   // participante
        eventResource.setStatus("confirmed"); // Definindo o status do evento como confirmado

        return eventsRepository.insert(eventResource);
    }

    public EventsResource addCalendarToEvent(String calendarId, String eventId, String userId) {
        if (calendarId == null || calendarId.isEmpty() || eventId == null || eventId.isEmpty() || userId == null
                || userId.isEmpty()) {
            throw new IllegalArgumentException(
                    "ID do calendário, ID do evento ou ID do usuário não podem ser nulos ou vazios.");
        }
        userDataController.findUser(userId);

        CalendarListResource calendarListUserItem = userDataController.findCalendarListResource(userId, calendarId);

        String accessRole = calendarListUserItem.getAccessRole();
        if (accessRole == null || (!accessRole.equals("owner") && !accessRole.equals("writer"))) {
            throw new IllegalArgumentException(
                    "Acesso negado: o usuário não tem permissão para adicionar este calendário ao evento.");
        }

        EventsResource event = eventsRepository
                .findEventByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId
                        + "' não encontrado para o usuário de ID '" + userId + "'."));

        event.addCalendarId(calendarId); // Adiciona o ID do calendário ao evento
        event.increaseLinks();

        return eventsRepository.save(event);
    }

    
    public EventsResource addAtendeeToEvent(String eventId, String calendarId, String userId, String atendeeUserId) {
        if (eventId == null || eventId.isEmpty() || calendarId == null || calendarId.isEmpty() || userId == null
                || userId.isEmpty() || atendeeUserId == null) {
            throw new IllegalArgumentException(
                    "ID do evento, ID do calendário, ID do usuário ou do participante não podem ser nulos ou vazios.");
        }
        userDataController.findUser(userId);

        CalendarListResource calListResource = userDataController.findCalendarListResource(userId, calendarId);

        EventsResource event = eventsRepository
                .findEventsResourceByEventIdAndCalendarId(eventId, calListResource.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId
                        + "' não encontrado para o calendário com ID '" + calendarId + "'."));

        Attendee[] attendees = event.getAttendees();
        for (Attendee attendee : attendees) {
            if (attendee.getCalendarPerson().getId().equals(atendeeUserId)) {
                throw new IllegalArgumentException("Pessoa já está convidada para este evento.");
            }
        }

        User person = userDataController.findUser(atendeeUserId);

        Attendee newAttendee = new Attendee(person.getAsCalendarPerson(), false);
        event.addAttendee(newAttendee);
        event.addCalendarId(person.getCalendarList().get(0).getCalendarId());
        event.increaseLinks();
        return eventsRepository.save(event);
    }

    
    public EventsResource getEvent(String eventId, String calendarId,
            String userId) {
        if (eventId == null || eventId.isEmpty() || calendarId == null || calendarId.isEmpty() || userId == null
                || userId.isEmpty()) {
            throw new IllegalArgumentException(
                    "ID do evento, ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        userDataController.findUser(userId);

        CalendarListResource calListUserItem = userDataController.findCalendarListResource(userId, calendarId);

        EventsResource event = eventsRepository
                .findEventsResourceByEventIdAndCalendarId(eventId, calListUserItem.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId
                        + "' não encontrado para o calendário com ID '" + calendarId + "'."));

        return event;
    }

    
    public EventsResource updateEvent(String calendarId, String eventId, EventsResource eventResource, String userId) {
        CalendarResource calendar = calendarRepository.findByCalendarId(calendarId).orElse(null);
        EventsResource event = eventsRepository.findByEventId(eventId).orElse(null);
        if (calendar == null) {
            throw new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado.");
        }
        if (event == null) {
            throw new IllegalArgumentException("Evento com ID '" + eventId + "' não encontrado.");
        }
        eventResource.setId(eventId);
        eventResource.setMainCalendarId(calendarId); // FIX
        eventsRepository.save(eventResource);
        return eventResource;
    }

    
    public EventsResource patchEvent(String calendarId, String eventId, EventsResource eventResource, String userId) {
        CalendarResource calendar = calendarRepository.findByCalendarId(calendarId).orElse(null);
        EventsResource event = eventsRepository.findByEventId(eventId).orElse(null);
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

    
    public ArrayList<EventsResource> getEvents(String calendarId,
            String userId) {
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        userDataController.findUser(userId);

        CalendarListResource calListUserItem = userDataController.findCalendarListResource(userId, calendarId);

        ArrayList<EventsResource> events = eventsRepository.findAllByCalendarId(calListUserItem.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nenhum evento encontrado para o calendário com ID '" + calendarId + "'."));

        return events;
    }

    
    public void removeEvent(String eventId, String calendarId, String userId) {
        if (eventId == null || eventId.isEmpty() || calendarId == null || calendarId.isEmpty() || userId == null
                || userId.isEmpty()) {
            throw new IllegalArgumentException(
                    "ID do evento, ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        userDataController.findUser(userId);

        CalendarListResource calListUserItem = userDataController.findCalendarListResource(userId, calendarId);

        String accessRole = calListUserItem.getAccessRole();
        if (accessRole == null || (!accessRole.equals("owner") && !accessRole.equals("writer"))) {
            throw new IllegalArgumentException(
                    "Acesso negado: o usuário não tem permissão para remover eventos deste calendário.");
        }

        EventsResource event = eventsRepository
                .findEventsResourceByEventIdAndCalendarId(eventId, calListUserItem.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId
                        + "' não encontrado para o calendário com ID '" + calendarId + "'."));

        eventsRepository.delete(event);
    }

    
    public void cancelEvent(String eventId, String calendarId, String userId) {
        if (eventId == null || eventId.isEmpty() || calendarId == null || calendarId.isEmpty() || userId == null
                || userId.isEmpty()) {
            throw new IllegalArgumentException(
                    "ID do evento, ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        userDataController.findUser(userId);

        CalendarListResource calListUserItem = userDataController.findCalendarListResource(userId, calendarId);

        EventsResource event = eventsRepository
                .findEventsResourceByEventIdAndCalendarId(eventId, calListUserItem.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId
                        + "' não encontrado para o calendário com ID '" + calendarId + "'."));

        if (event.getOrganizer().getId() != userId) {
            throw new IllegalArgumentException("Acesso negado: o usuário não é o organizador deste evento.");
        }

        event.setStatus("cancelled");
        eventsRepository.save(event);
    }
}
