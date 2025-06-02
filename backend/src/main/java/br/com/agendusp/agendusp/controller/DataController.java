package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.agendusp.agendusp.dataobjects.Attendee;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarListUserItem;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.EventsRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

@Component
public class DataController extends AbstractDataController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CalendarRepository calendarRepository;
    @Autowired
    private EventsRepository eventsRepository;

    public DataController() {
    }

    // Users
    @Override
    public User createUser(User user) {
        if (user == null || user.getId() == null || user.getId().isEmpty()) {
            throw new IllegalArgumentException("Usuário ou ID do usuário não podem ser nulos ou vazios.");
        }
        if (userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("Usuário com ID '" + user.getId() + "' já existe.");
        }
        return userRepository.save(user);
    }

    // Calendars
    @Override
    public CalendarListUserItem addCalendarListUserItem(CalendarResource calResource, String userId) {
        if (calResource == null || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListUserItem calListUserItem = new CalendarListUserItem(calResource.getCalendarId(), "", "", "", false, true, getAccessRole(calResource, userId)); // Inicialmente, o usuário é o dono do calendário

        userRepository.updateOneByUserId(userId, calListUserItem);

        return calListUserItem;
    }

    @Override
    public CalendarResource addCalendar(CalendarResource calResource, String userId) {
        if (calResource == null || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        if (calendarRepository.existsById(calResource.getCalendarId())) {
            throw new IllegalArgumentException("Calendário com ID '" + calResource.getCalendarId() + "' já existe.");
        }

        this.addCalendarListUserItem(calResource, userId);
        return calendarRepository.save(calResource);
    }

    @Override
    public CalendarListResource getCalendarListResource(String calendarId, String userId) {
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListUserItem calListUserItem = userRepository.findCalendarListUserItemByUserIdAndCalendarId(userId, calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                        + "' não encontrado para o usuário de ID '" + userId + "'."));

        CalendarResource calResource = calendarRepository.findByCalendarId(calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado."));
        boolean primary = calResource.getOwner().id().equals(userId);

        return new CalendarListResource(calendarId, primary, calListUserItem, calResource);
    }

    @Override
    protected CalendarListUserItem updateCalendarListUserItem(String calendarId, CalendarListUserItem calListUserItem, String userId) {
        if (calListUserItem.getAccessRole() == "owner" || calListUserItem.getAccessRole() == "writer") {
            userRepository.updateOneByUserId(userId, calListUserItem);
            return calListUserItem;
        }
        throw new IllegalArgumentException(
                "Acesso negado: o usuário não tem permissão para atualizar este calendário.");

    }

    @Override
    protected CalendarResource updateCalendarResource(String calendarId, CalendarResource calResource, String userId, String accessRole) {

        if (accessRole == "owner" || accessRole == "writer") {
            calendarRepository.save(calResource);
            return calResource;
        }
        throw new IllegalArgumentException(
                "Acesso negado: o usuário não tem permissão para atualizar este calendário.");

    }

    @Override
    public CalendarListResource updateCalendar(String calendarId, CalendarListResource calListResource, String userId) {
        CalendarListUserItem registeredCalListUserItem = userRepository
                .findCalendarListUserItemByUserIdAndCalendarId(userId, calendarId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "CalendarListResource com ID '" + calendarId + "' não encontrado para o usuário de ID '"
                                + userId + "'"));

        CalendarListUserItem calListUserItem = calListResource.extractCalendarListUserItem();
        CalendarResource calResource = calListResource.extractCalendarResource();

        if (registeredCalListUserItem.getAccessRole() == "owner" || registeredCalListUserItem.getAccessRole() == "writer") {
            updateCalendarListUserItem(calendarId, calListUserItem, userId);
            updateCalendarResource(calendarId, calResource, userId, userId);
        }
        throw new IllegalArgumentException(
                "Acesso negado: o usuário não tem permissão para atualizar este calendário.");

    }

    // @Override
    // public CalendarListResource patchCalendar(String calendarId, CalendarResource calResource, String userId) {
    //     CalendarListResource calListResource = userRepository
    //             .findCalendarListUserItemByUserIdAndCalendarId(userId, calendarId)
    //             .orElseThrow(() -> new IllegalArgumentException(
    //                     "CalendarListResource com ID '" + calendarId + "' não encontrado para o usuário de ID '"
    //                             + userId));

    //     if (calListResource.getAccessRole() == "owner" || calListResource.getAccessRole() == "writer") {
    //         if (calListResource.getSummary() != null) {
    //             calListResource.setSummary(calListResource.getSummary());
    //         }
    //         if (calListResource.getDescription() != null) {
    //             calListResource.setDescription(calListResource.getDescription());
    //         }
    //         if (calListResource.getLocation() != null) {
    //             calListResource.setLocation(calListResource.getLocation());
    //         }
    //         if (calListResource.getTimeZone() != null) {
    //             calListResource.setTimeZone(calListResource.getTimeZone());
    //         }
    //         if (calListResource.getAccessRole() != null) {
    //             calListResource.setAccessRole(calListResource.getAccessRole());
    //         }

    //         calendarRepository.save(calResource);
    //         return calListResource;
    //     }

    //     throw new IllegalArgumentException(
    //             "Acesso negado: o usuário não tem permissão para atualizar este calendário.");
    // }

    @Override
    public ArrayList<CalendarListResource> getCalendars(String userId) throws Exception {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo ou vazio.");
        }
        User user = findUser(userId);
        ArrayList<CalendarListResource> outputList = new ArrayList<>();
        boolean primary;

        ArrayList<CalendarListUserItem> calendarList = user.getCalendarList();
        for (CalendarListUserItem calListUserItem : calendarList) {
            CalendarResource calResource = calendarRepository
                .findByCalendarId(calListUserItem.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calListUserItem.getCalendarId()
                        + "' não encontrado."));
            primary = calResource.getOwner().id().equals(userId);
            outputList.add(new CalendarListResource(calListUserItem.getCalendarId(), primary, calListUserItem, calResource));
        }

        return outputList;
    }

    @Override
    public void removeCalendar(String calendarId, String userId) {
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListUserItem calListResource = findCalendarListUserItem(userId, calendarId);

        if (calListResource.getAccessRole() == "owner") {
            calendarRepository.deleteById(calendarId);
            userRepository.refreshLinks(calendarId);
        } else {
            userRepository.deleteCalendarListResourceById(userId, calendarId);
        }
    }

    // Events
    @Override
    public EventsResource createEvent(String calendarId, EventsResource eventResource, String userId) {
        if (eventResource == null || eventResource.getEventId() == null || eventResource.getEventId().isEmpty()) {
            throw new IllegalArgumentException("Evento não pode ser nulo e deve ter um ID.");
        }
        User user = findUser(userId);

        CalendarListUserItem calResource = findCalendarListUserItem(userId, calendarId);

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

    @Override
    public EventsResource addCalendarToEvent(String calendarId, String eventId, String userId) {
        if (calendarId == null || calendarId.isEmpty() || eventId == null || eventId.isEmpty() || userId == null
                || userId.isEmpty()) {
            throw new IllegalArgumentException(
                    "ID do calendário, ID do evento ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListUserItem calendarListUserItem = findCalendarListUserItem(userId, calendarId);

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

    @Override
    public EventsResource addAtendeeToEvent(String eventId, String calendarId, String userId, String atendeeUserId) {
        if (eventId == null || eventId.isEmpty() || calendarId == null || calendarId.isEmpty() || userId == null
                || userId.isEmpty() || atendeeUserId == null) {
            throw new IllegalArgumentException(
                    "ID do evento, ID do calendário, ID do usuário ou do participante não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListUserItem calListResource = findCalendarListUserItem(userId, calendarId);

        EventsResource event = eventsRepository
                .findEventsResourceByEventIdAndCalendarId(eventId, calListResource.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId
                        + "' não encontrado para o calendário com ID '" + calendarId + "'."));

        Attendee[] attendees = event.getAttendees();
        for (Attendee attendee : attendees) {
            if (attendee.getCalendarPerson().id().equals(atendeeUserId)) {
                throw new IllegalArgumentException("Pessoa já está convidada para este evento.");
            }
        }

        User person = findUser(atendeeUserId);

        Attendee newAttendee = new Attendee(person.getAsCalendarPerson(), false);
        event.addAttendee(newAttendee);
        event.addCalendarId(person.getCalendarList().get(0).getCalendarId());
        event.increaseLinks();
        return eventsRepository.save(event);
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

        CalendarListUserItem calListUserItem = findCalendarListUserItem(userId, calendarId);

        EventsResource event = eventsRepository
                .findEventsResourceByEventIdAndCalendarId(eventId, calListUserItem.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId
                        + "' não encontrado para o calendário com ID '" + calendarId + "'."));

        return event;
    }

    @Override
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

    @Override
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

    @Override
    public ArrayList<EventsResource> getEvents(String calendarId,
            String userId) {
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListUserItem calListUserItem = findCalendarListUserItem(userId, calendarId);

        ArrayList<EventsResource> events = eventsRepository.findAllByCalendarId(calListUserItem.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nenhum evento encontrado para o calendário com ID '" + calendarId + "'."));

        return events;
    }

    @Override
    public void removeEvent(String eventId, String calendarId, String userId) {
        if (eventId == null || eventId.isEmpty() || calendarId == null || calendarId.isEmpty() || userId == null
                || userId.isEmpty()) {
            throw new IllegalArgumentException(
                    "ID do evento, ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListUserItem calListUserItem = findCalendarListUserItem(userId, calendarId);

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

    @Override
    public void cancelEvent(String eventId, String calendarId, String userId) {
        if (eventId == null || eventId.isEmpty() || calendarId == null || calendarId.isEmpty() || userId == null
                || userId.isEmpty()) {
            throw new IllegalArgumentException(
                    "ID do evento, ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        findUser(userId);

        CalendarListUserItem calListUserItem = findCalendarListUserItem(userId, calendarId);

        EventsResource event = eventsRepository
                .findEventsResourceByEventIdAndCalendarId(eventId, calListUserItem.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId
                        + "' não encontrado para o calendário com ID '" + calendarId + "'."));

        if (event.getOrganizer().id() != userId) {
            throw new IllegalArgumentException("Acesso negado: o usuário não é o organizador deste evento.");
        }

        event.setStatus("cancelled");
        eventsRepository.save(event);
    }
}
