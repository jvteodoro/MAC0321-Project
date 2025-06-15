package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;

import br.com.agendusp.agendusp.controller.google.GoogleCalendarListController;
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
        @Autowired
        RestClient restClient;
          // Users
        public User createUser(User user) {
                if (user == null ){//|| user.getUserId() == null || user.getUserId().isEmpty()) {
                throw new IllegalArgumentException("Usuário ou ID do usuário não podem ser nulos ou vazios.");
                }
                if (userRepository.existsById(user.getId())) {
                throw new IllegalArgumentException("Usuário com ID '" + user.getId() + "' já existe.");
                }
                return userRepository.save(user);
        }
        public User findUserOrCreate(User userGiven){
                Optional<User> user  = userRepository.findById(userGiven.getId());
                     if (user.isEmpty()){
                     //   gCalController.getUserInfo();
                     //   restClient.get().uri("http://localhost")
                        createUser(userGiven);
                        return userGiven;
                } else {
                        return user.get();
                }
        }
        public User findUserByName(String name) {
                Optional<User> optUser = userRepository.findByName(name);
                User user = optUser.get();
                if (optUser.isEmpty()){
                         user = new User();
                }
                return user;
        }

        protected User findUser(String userId) {
                Optional<User> user = userRepository.findById(userId);//   .orElseThrow(() -> new IllegalArgumentException("Usuário com ID '" + userId + "' não encontrado."));
                if (user.isEmpty()){
                     //   gCalController.getUserInfo();
                     //   restClient.get().uri("http://localhost")
                        User newUser = new User();
                        newUser.setGoogleId(userId);
                        newUser.setEmail(userId);
                        newUser.setUsername(userId);
                        createUser(newUser);
                        return newUser;
                } else {
                        return user.get();
                }
        }

        protected CalendarListUserItem findCalendarListUserItem(String userId, String calendarId) {
                CalendarListUserItem calListUserItemResource = userRepository
                                .findCalendarListUserItemByUserIdAndCalendarId(userId, calendarId)
                                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                                                + "' não encontrado para o usuário de ID '" + userId + "'."));
                return calListUserItemResource;
        }

        protected String getAccessRole(CalendarResource calResource, String userId) {
                if (calResource.getOwner().getId().equals(userId)) 
                        return "owner";
                if (calResource.getWriters().stream().anyMatch(writer -> writer.getId().equals(userId)))
                        return "writer";
                if (calResource.getReaders().stream().anyMatch(reader -> reader.getId().equals(userId)))
                        return "reader";

                return "freeBusyReader";
        }

        // Cabeçalhos para as funções
        protected abstract CalendarListUserItem updateCalendarListUserItem(String calendarId, CalendarListUserItem calListUserItem, String userId);
        protected abstract CalendarResource updateCalendarResource(String calendarId, CalendarResource calResource, String userId, String accessRole);

        public abstract CalendarListResource updateCalendar(String calendarId, CalendarListResource calListResource, String userId);

        //public abstract User createUser(User user);

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
        public abstract ArrayList<EventsResource> getEventsOnInterval(String calendarId, String endDate);
}
