package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;



public class DataController implements AbstractDataController{
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
    public CalendarListResource patchCalendar(String calendarId, CalendarListResource calResource) {
        // Implementação para atualizar parcialmente um calendário
        return null;
    }

    @Override
    public CalendarListResource updateCalendar(String calendarId, CalendarListResource calResource) {
        // Implementação para atualizar um calendário
        return null;
    }

    @Override
    public ArrayList<CalendarListResource> getCalendars() {
        // Implementação para obter todos os calendários
        return new ArrayList<>();
    }

    @Override
    public ArrayList<EventResource> getEvents(String calendarId) {
        // Implementação para obter eventos de um calendário específico
        return new ArrayList<EventResource>();
    }

    @Override
    public EventResource getEvent(String eventId, String calendarId) {
        // Implementação para obter um evento específico de um calendário
        return null;
    }

    @Override
    public void removeCalendar(String calendarId) {
        // Implementação para remover um calendário
    }

    @Override
    public void addEvent(String calendarId, EventResource eventResource) {
        // Implementação para adicionar um evento a um calendário
    }

    @Override
    public void removeEvent(String eventId, String calendarId) {
        // Implementação para remover um evento de um calendário
    }

    @Override
    public void updateEvent(String eventId, EventResource eventResource) {
        // Implementação para atualizar um evento específico de um calendário
    }

}
