package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;
import java.util.List;

import br.com.agendusp.agendusp.repositories.CalendarListRepository;
import br.com.agendusp.agendusp.repositories.EventsRepository;
import br.com.agendusp.agendusp.repositories.UserCalendarListResourceAccessRelationRepository;

public class DataController implements AbstractDataController {

    private final CalendarListRepository calendarListRepository;
    private final EventsRepository eventsRepository;
    private final UserCalendarListResourceAccessRelationRepository userCalendarListResourceAccessRelationRepository;

    public DataController(CalendarListRepository calendarListRepository, EventsRepository eventsRepository,
            UserCalendarListResourceAccessRelationRepository userCalendarListResourceAccessRelationRepository) {
        this.calendarListRepository = calendarListRepository;
        this.eventsRepository = eventsRepository;
        this.userCalendarListResourceAccessRelationRepository = userCalendarListResourceAccessRelationRepository;
    }

    // Calendars
    @Override
    public void addCalendar(CalendarListResource calResource, String userId) {
        // Implementação para adicionar um calendário
        calendarListRepository.insert(calResource);
    }

    @Override
    public CalendarListResource getCalendar(String calendarId, String userId) {
        // Implementação para obter um calendário por ID
        return null;
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
        // Implementação para adicionar um evento a um calendário
    }

    @Override
    public EventsResource getEvent(String eventId, String calendarId,
            String userId) {
        // Implementação para obter um evento específico de um calendário
        return null;
    }

    @Override
    public EventsResource updateEvent(String calendarId, String eventId, EventsResource eventResource,
            String userId) {
        // Implementação para atualizar um evento específico de um calendário
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
}
