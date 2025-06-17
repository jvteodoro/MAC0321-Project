package br.com.agendusp.agendusp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;

import br.com.agendusp.agendusp.documents.CalendarListUserItem;
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
}
