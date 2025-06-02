package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarListUserItem;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.UserRepository;

public abstract class AbstractDataController {

        //Funçoes gerais para manipular dados de usuários e calendários
        @Autowired
        private UserRepository userRepository;

        protected User findUser(String userId) {
                return userRepository.findById(userId)
                                .orElseThrow(() -> new IllegalArgumentException("Usuário com ID '" + userId + "' não encontrado."));
        }

        protected CalendarListUserItem findCalendarListUserItem(String userId, String calendarId) {
                CalendarListUserItem calListUserItemResource = userRepository
                                .findCalendarListUserItemByUserIdAndCalendarId(userId, calendarId)
                                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                                                + "' não encontrado para o usuário de ID '" + userId + "'."));
                return calListUserItemResource;
        }

        public abstract User createUser(User user);

        protected String getAccessRole(CalendarResource calResource, String userId) {
                if (calResource.getOwner().id().equals(userId)) 
                        return "owner";
                if (calResource.getWriters().stream().anyMatch(writer -> writer.id().equals(userId)))
                        return "writer";
                if (calResource.getReaders().stream().anyMatch(reader -> reader.id().equals(userId)))
                        return "reader";

                return "freeBusyReader";
        }

        // Cabeçalhos para as funções
        protected abstract CalendarListUserItem updateCalendarListUserItem(String calendarId, CalendarListUserItem calListUserItem, String userId);
        protected abstract CalendarResource updateCalendarResource(String calendarId, CalendarResource calResource, String userId, String accessRole);

        public abstract CalendarListResource updateCalendar(String calendarId, CalendarListResource calListResource,
                        String userId);

        // public abstract CalendarListResource patchCalendar(String calendarId, CalendarResource calResource,
        //                 String userId);

        public abstract ArrayList<CalendarListResource> getCalendars(String userId) throws Exception;

        public abstract CalendarResource addCalendar(CalendarResource calResource, String userId);

        public abstract CalendarListResource getCalendarListResource(String calendarId, String userId);

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

        public abstract CalendarListUserItem addCalendarListUserItem(CalendarResource calResource, String userId);
}
