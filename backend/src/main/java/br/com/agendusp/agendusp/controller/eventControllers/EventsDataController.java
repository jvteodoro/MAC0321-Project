package br.com.agendusp.agendusp.controller.eventControllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.calendarControllers.CalendarDataController;
import br.com.agendusp.agendusp.dataobjects.DateTimeInterval;
import br.com.agendusp.agendusp.dataobjects.eventObjects.Attendee;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.EventsRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

public class EventsDataController {
    // Events
    @Autowired
    EventsRepository eventsRepository;
    @Autowired
    UserDataController userDataController;
    @Autowired
    CalendarDataController calendarDataController;
    @Autowired
    ObjectMapper objMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CalendarRepository calendarRepository;

    public ArrayList<EventsResource> getEventsOnInterval(String calendarId, LocalDateTime endDate) {
        return eventsRepository.findEventsByEndDate(calendarId, endDate)
                .orElse(new ArrayList<EventsResource>());
    }
    public ArrayList<EventsResource> getEventsOnInterval(DateTimeInterval interval){
        return eventsRepository.findEventosDentroDoIntervalo(interval.getStart(), interval.getEnd()).orElse(null);
    }

    public EventsResource addEvent(EventsResource eventResource) {
        eventsRepository.save(eventResource);
        return eventResource;
    }

    public EventsResource createEvent(String calendarId, EventsResource eventResource, String userId) {
        // Generate random 26-character id if not present
        if (eventResource == null) {
            throw new IllegalArgumentException("Evento não pode ser nulo.");
        }
        if (eventResource.getId() == null || eventResource.getId().isEmpty()) {
            eventResource.setId(generateRandomId(26));
        }
        User user = userDataController.findUser(userId);

        CalendarListResource calResource = userDataController.findCalendarListResource(userId, calendarId);

        String accessRole = calResource.getAccessRole();

        if (accessRole == null || (!accessRole.equals("owner") && !accessRole.equals("writer"))) {
            throw new IllegalArgumentException(
                    "Acesso negado: o usuário não tem permissão para adicionar eventos a este calendário.");
        }

        if (eventsRepository.existsById(eventResource.getId())) {
            throw new IllegalArgumentException("Evento com ID '" + eventResource.getId() + "' já existe.");
        }

        eventResource.setMainCalendarId(calendarId);
        eventResource.addCalendarId(calendarId);
        eventResource.setCreator(user.getAsCalendarPerson());
        eventResource.setOrganizer(user.getAsCalendarPerson()); // Inicialmente, o criador é o organizador
        eventResource.addAttendee(new Attendee(user.getAsCalendarPerson(), true)); // Adicionando o criador como participante
        eventResource.setStatus("confirmed"); // Definindo o status do evento como confirmado

        // Atualiza os attendees e calendarIds conforme solicitado
        updateAttendeesAndCalendars(eventResource);

        return eventsRepository.insert(eventResource);
    }

    public EventsResource cancelEvent(String eventId){
        EventsResource event = eventsRepository.findById(eventId).orElse(new EventsResource());
        event.setStatus("cancelled");
        eventsRepository.save(event);
        return event;

    }

    // Helper to generate a random 26-character id (lowercase letters and numbers)
    private String generateRandomId(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
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

        ArrayList<Attendee> attendees = event.getAttendees();
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

    public EventsResource addAtendeeToEvent(String eventId, String atendeeUserId) {
        if (eventId == null || atendeeUserId == null) {
            throw new IllegalArgumentException(
                    "ID do evento, ID do calendário, ID do usuário ou do participante não podem ser nulos ou vazios.");
        }
        EventsResource event = eventsRepository
                .findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId
                        + "' não encontrado"));

        ArrayList<Attendee> attendees = event.getAttendees();
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


    public EventsResource getEventById(String eventId){
        if (eventId == null || eventId.isEmpty()) {
            throw new IllegalArgumentException("ID do evento não pode ser nulo ou vazio.");
        }
        Optional<EventsResource> evRoptional = eventsRepository.findById(eventId);
        if (evRoptional.isEmpty()){
            return new EventsResource();
        }
        return evRoptional.get();
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
    public EventsResource updateByObject(EventsResource eventsResource){
        String id = eventsResource.getId();
        eventsRepository.save(eventsResource);
        return eventsRepository.findById(id).orElse(new EventsResource()); 
    }

    public EventsResource updateEvent(String calendarId, String eventId, EventsResource eventResource, String userId) {
        EventsResource event = eventsRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId + "' não encontrado."));

        try {
            System.out.println(objMapper.writeValueAsString(event));
        } catch (Exception e) {
            System.err.println(e);
        }
        eventResource.setId(eventId);
        eventResource.setMainCalendarId(calendarId); // TO-DO FIX

        // Atualiza os attendees e calendarIds conforme solicitado
        updateAttendeesAndCalendars(eventResource);

        eventsRepository.save(eventResource);
        return eventResource;
    }

    public EventsResource patchEvent(String calendarId, String eventId, EventsResource eventResource, String userId) {
        CalendarResource calendar = calendarRepository.findById(calendarId).orElse(null);
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

    public ArrayList<EventsResource> getEvents(String calendarId,
            String userId) {
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        userDataController.findUser(userId);

        // apenas tenta encontrar o calendário se o usuario tem acesso a ele
        // se não tiver, retorna lista vazia ao invés de lançar exceção
        if (!userDataController.userHasCalendar(userId, calendarId)) {
            return new ArrayList<>();
        }

        CalendarListResource calListResource = userDataController.findCalendarListResource(userId, calendarId);

        ArrayList<EventsResource> events = eventsRepository.findAllByCalendarId(calListResource.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nenhum evento encontrado para o calendário com ID '" + calendarId + "'."));

        // try {
        //     System.out.println("[DEGUB] Lista de eventos:\n     " + objMapper.writeValueAsString(events));
        // } catch(Exception e) {

        // }
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

    /**
     * Atualiza os attendees do evento: se algum attendee tiver id ou displayName nulo,
     * tenta buscar o usuário pelo email e preencher os dados. Também adiciona o mainCalendar
     * do usuário à lista de calendarIds do evento. Se o usuário não for encontrado, remove o attendee.
     */
    private void updateAttendeesAndCalendars(EventsResource eventResource) {
        if (eventResource.getAttendees() == null) return;
        ArrayList<Attendee> updatedAttendees = new ArrayList<>();
        ArrayList<String> calendarIds = eventResource.getCalendarIds();
        if (calendarIds == null) {
            calendarIds = new ArrayList<>();
        }
        for (Attendee attendee : eventResource.getAttendees()) {
            System.out.println("[DEBUG] Attendee: " + attendee.getCalendarPerson().getId() + " | " + attendee.getCalendarPerson().getEmail() + " | " + attendee
                    .getCalendarPerson().getDisplayName());
            if (attendee == null || attendee.getCalendarPerson() == null) continue;
            String email = attendee.getCalendarPerson().getEmail();
            String id = attendee.getCalendarPerson().getId();
            String displayName = attendee.getCalendarPerson().getDisplayName();
            if (id == null || id.isEmpty() || displayName == null || displayName.isEmpty()) {
                // Try to find user by email
                try {
                    User user = userRepository.findByEmail(email).orElse(null);
                    if (user != null && user.getId() != null && user.getEmail() != null) {
                        attendee.getCalendarPerson().setId(user.getId());
                        attendee.getCalendarPerson().setDisplayName(
                            user.getDisplayName() != null ? user.getDisplayName() : user.getName()
                        );
                        // Add user's main calendar to calendarIds
                        if (user.getCalendarList() != null && !user.getCalendarList().isEmpty()) {
                            // String mainCalendarId = user.getCalendarList().get(0).getCalendarId();
                            String mainCalendarId = email;
                            if (mainCalendarId != null && !calendarIds.contains(mainCalendarId)) {
                                calendarIds.add(mainCalendarId);
                            }
                        }
                        updatedAttendees.add(attendee);
                        continue;
                    }
                } catch (Exception e) {
                    // User not found, will remove attendee
                }
                // If user not found, skip adding this attendee
            } else {
                // Already has id and displayName, just add
                updatedAttendees.add(attendee);
            }
        }
        eventResource.setAttendees(updatedAttendees);
        eventResource.setCalendarIds(calendarIds);
    }
}
