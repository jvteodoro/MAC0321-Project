package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.UserRepository;

public abstract class AbstractDataController {

        @Autowired
        private UserRepository userRepository;

        protected User findUser(String userId) {
                return userRepository.findById(userId)
                                .orElseThrow(() -> new IllegalArgumentException("Usuário com ID '" + userId + "' não encontrado."));
        }

        protected CalendarListResource findCalendarList(String userId, String calendarId) {
                CalendarListResource calListResource = userRepository
                                .findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
                                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                                                + "' não encontrado para o usuário de ID '" + userId + "'."));
                return calListResource;
        }

        protected String getAccessRole(CalendarResource calResource, String userId) {
                if (calResource.getOwner().id().equals(userId)) 
                        return "owner";
                if (calResource.getWriters().stream().anyMatch(writer -> writer.id().equals(userId)))
                        return "writer";
                if (calResource.getReaders().stream().anyMatch(reader -> reader.id().equals(userId)))
                        return "reader";

                return "freeBusyReader";
        }

        public abstract CalendarListResource updateCalendar(String calendarId, CalendarResource calResource,
                        String userId);

        public abstract CalendarListResource patchCalendar(String calendarId, CalendarResource calResource,
                        String userId);

        public abstract ArrayList<CalendarListResource> getCalendars(String userId) throws Exception;

        public abstract void addCalendar(CalendarResource calResource, String userId);

        public abstract CalendarListResource getCalendar(String calendarId, String userId);

        public abstract void removeCalendar(String calendarId, String userId);

        public abstract EventsResource createEvent(String calendarId, EventsResource eventResource,
                        String userId);

        public abstract EventsResource addCalendarToEvent(String calendarId, String eventId, String userId);

        public abstract EventsResource addAtendeeToEvent(String eventId, String calendarId, String userId, String atendeeUserId);

        public abstract EventsResource getEvent(String eventId, String calendarId,
                        String userId);

        public abstract EventsResource updateEvent(String calendarId, String eventId, EventsResource eventResource,
                        String userId);

        public abstract ArrayList<EventsResource> getEvents(String calendarId, String userId);

        public abstract EventsResource patchEvent(String calendarId, String eventId, EventsResource eventResource,
                        String userId);

        public abstract void removeEvent(String eventId, String calendarId, String userId);

        public abstract void cancelEvent(String eventId, String calendarId, String userId);

        public abstract void addCalendarListResource(String calendarId, String userId);
}
