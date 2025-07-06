package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;

import br.com.agendusp.agendusp.dataobjects.PollNotification;
import br.com.agendusp.agendusp.dataobjects.calendarObjects.CalendarPerson;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

public class UserDataController {

        // Funçoes gerais para manipular dados de usuários e calendários
        @Autowired
        private UserRepository userRepository;
        @Autowired
        CalendarRepository calendarRepository;
        @Autowired
        RestClient restClient;

        // Users
        public User createUser(User user) {
                if (user == null) {// || user.getUserId() == null || user.getUserId().isEmpty()) {
                        throw new IllegalArgumentException("Usuário ou ID do usuário não podem ser nulos ou vazios.");
                }
                if (userRepository.existsById(user.getId())) {
                        throw new IllegalArgumentException("Usuário com ID '" + user.getId() + "' já existe.");
                }
                CalendarResource mainCalendar = new CalendarResource();
                mainCalendar.setId(user.getEmail());
                mainCalendar.setCalendarId(user.getEmail());
                mainCalendar.setOwner(user.getAsCalendarPerson());

                calendarRepository.save(mainCalendar);
                user.addCalendarListResource(mainCalendar.toCalendarListResource("owner"));
                userRepository.save(user);
                return userRepository.findById(user.getId()).orElse(user);
        }

        public void deleteUser(String id) {
                Optional<User> user = userRepository.findById(id);
                if (user.isPresent()) {
                        userRepository.delete(user.get());
                }
        }

        public void deleteUser(User user) {
                userRepository.delete(user);
        }
        

        public User findUserOrCreate(User userGiven) {
                Optional<User> user = userRepository.findById(userGiven.getId());
                if (user.isEmpty()) {
                        // gCalController.getUserInfo();
                        // restClient.get().uri("http://localhost")
                        createUser(userGiven);
                        return userGiven;
                } else {
                        return user.get();
                }
        }

        public User findUserByName(String name) {
                User user;
                try {
                        Optional<User> optUser = userRepository.findByName(name);
                        user = optUser.get();
                        if (optUser.isEmpty()) {
                                user = new User();
                        }
                } catch (Exception e) {
                        System.err.println(e);
                        user = new User();
                }

                return user;
        }

        public ArrayList<CalendarListResource> insertCalendarListResource(String userId, CalendarListResource item) {
                Optional<CalendarListResource> calListR = userRepository
                                .findCalendarListResourceByIdAndCalendarId(userId, item.getCalendarId());
                if (calListR.isEmpty()) {
                        userRepository.addCalendarListResource(userId, item);
                }
                return userRepository.getCalendarList(userId);
        }

        public CalendarPerson getCalendarPerson(String userId) {
                User user = findUser(userId);
                CalendarPerson calPerson = new CalendarPerson();
                calPerson.setId(user.getId());
                calPerson.setEmail(user.getEmail());
                calPerson.setDisplayName(user.getDisplayName());
                return calPerson;
        }

        public User findUser(String id) {
                return userRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));// .orElseThrow(()
                                                                                                         // -> new
                                                                                                         // IllegalArgumentException("Usuário
                                                                                                         // com ID '" +
                                                                                                         // id + "' não
                                                                                                         // encontrado."));

        }

        public CalendarListResource findCalendarListResource(String userId, String calendarId) {
                CalendarListResource calListResource = userRepository
                                .findCalendarListResourceByIdAndCalendarId(userId, calendarId)
                                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                                                + "' não encontrado para o usuário de ID '" + userId + "'."));
                return calListResource;
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

        /**
         * Checks if the user has a calendar with the given calendarId.
         */
        public boolean userHasCalendar(String userId, String calendarId) {
                try {
                        userRepository.findCalendarListResourceByIdAndCalendarId(userId, calendarId)
                                        .orElseThrow(() -> new IllegalArgumentException());
                        return true;
                } catch (Exception e) {
                        return false;
                }
        }

        public void addEventPollNotification(String userId, PollNotification evPollNotification){
                User user = findUser(userId);
                user.addEventPollNotifications(evPollNotification);
                userRepository.save(user);
        }
}
