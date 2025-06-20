package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;

import br.com.agendusp.agendusp.dataobjects.CalendarPerson;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.UserRepository;

public class UserDataController {

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

        public ArrayList<CalendarListResource> insertCalendarListResource(String userId, CalendarListResource item){
                userRepository.addCalendarListResource(userId, item);
                return userRepository.getCalendarList(userId);
        }
        public CalendarPerson getCalendarPerson(String userId){
                User user = findUser(userId);
                CalendarPerson calPerson = new CalendarPerson();
                calPerson.setId(user.getUserId());
                calPerson.setEmail(user.getEmail());
                calPerson.setDisplayName(user.getDisplayName());
                return calPerson;
        }

        public User findUser(String userId) {
                Optional<User> user = userRepository.findByGoogleId(userId);//   .orElseThrow(() -> new IllegalArgumentException("Usuário com ID '" + userId + "' não encontrado."));
                if (user.isEmpty()){
                     //   gCalController.getUserInfo();
                     //   restClient.get().uri("http://localhost")
                        User newUser = new User();
                        newUser.setGoogleId(userId);
                        newUser.setEmail(userId);
                        newUser.setId(userId);
                        newUser.setUsername(userId);
                        createUser(newUser);
                        return newUser;
                } else {
                        return user.get();
                }
        }

        protected CalendarListResource findCalendarListResource(String userId, String calendarId) {
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
}
