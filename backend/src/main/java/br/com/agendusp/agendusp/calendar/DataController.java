package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;

public class DataController implements AbstractDataController {
    // Calendars
    @Override
    public void addCalendar(CalendarListResource calResource) {
        // Implementação para adicionar um calendário
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
        // Implementação para remover um calendário
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
    public void updateEvent(String eventId, EventsResource eventResource) {
        // Implementação para atualizar um evento específico de um calendário
    }

    @Override
    public void patchEvent(String eventId, EventsResource eventResource) {
        // Implementação para atualizar parcialmente
    }

    @Override
    public ArrayList<EventsResource> getEvents(String calendarId) {
        // Implementação para obter eventos de um calendário específico
        return new ArrayList<EventsResource>();
    }

    @Override
    public void removeEvent(String eventId, String calendarId) {
        // Implementação para remover um evento de um calendário
    }

}
