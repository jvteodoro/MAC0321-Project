package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.agendusp.agendusp.repositories.CalendarListRepository;
import br.com.agendusp.agendusp.repositories.EventsRepository;

public class DataController implements AbstractDataController {
    
    private final CalendarListRepository calendarListRepository;
    private final EventsRepository eventsRepository;

    @Autowired
    public DataController(CalendarListRepository calendarListRepository, EventsRepository eventsRepository){
        this.calendarListRepository = calendarListRepository;
        this.eventsRepository = eventsRepository;
    }

    // Calendars
    @Override
    public void addCalendar(CalendarListResource calResource) {
        // Implementação para adicionar um calendário
        calendarListRepository.insert(calResource);
    }

    @Override
    public CalendarListResource getCalendar(String calendarId) {
        // Implementação para obter um calendário por ID
        return null;
    }

    @Override
    public CalendarListResource updateCalendar(String calendarId, CalendarListResource calResource) {
        // Implementação para atualizar um calendário
        return null;
    }

    @Override
    public CalendarListResource patchCalendar(String calendarId, CalendarListResource calResource) {
        // Implementação para atualizar parcialmente um calendário
        return null;
    }
    

    @Override
    public ArrayList<CalendarListResource> getCalendars() {
        // Implementação para obter todos os calendários
        return new ArrayList<>();
    }

    @Override
    public void removeCalendar(String calendarId) {
        if (calendarListRepository.existsById(calendarId)) {
            calendarListRepository.deleteById(calendarId);
        } else {
            throw new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado.");
        }
    }

    // Events
    @Override
    public void addEvent(String calendarId, EventsResource eventResource) {
        // Implementação para adicionar um evento a um calendário
    }

    @Override
    public EventsResource getEvent(String eventId, String calendarId) {
        // Implementação para obter um evento específico de um calendário
        return null;
    }

    @Override
    public EventsResource updateEvent(String calendarId, String eventId, EventsResource eventResource) {
        // Implementação para atualizar um evento específico de um calendário
        return null;
    }
    
    @Override
    public EventsResource patchEvent(String calendarId, String eventId, EventsResource eventResource) {
        // Implementação para atualizar parcialmente
        return null;
    }

    @Override
    public ArrayList<EventsResource> getEvents(String calendarId) {
        // Implementação para obter eventos de um calendário específico
        return new ArrayList<EventsResource>();
    }

    @Override
    public void removeEvent(String eventId, String calendarId) {
        EventsResource event = eventsRepository.findById(eventId)
        .orElseThrow(() -> new IllegalArgumentException("Evento com ID '" + eventId + "' não encontrado."));

        if (!event.getCalendarId().equals(calendarId)) {
            throw new IllegalArgumentException("Evento não pertence ao calendário informado.");
        }
        
        eventsRepository.deleteById(eventId);
    }
}
