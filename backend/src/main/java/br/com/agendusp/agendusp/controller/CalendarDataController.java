package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.repositories.CalendarRepository;
import br.com.agendusp.agendusp.repositories.UserRepository;

public class CalendarDataController {
      // Calendar
    @Autowired
    UserRepository userRepository;
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    UserDataController userDataController;
    @Autowired
    ObjectMapper objectMapper;


    public CalendarListResource addCalendarListResource(CalendarListResource calResource, String userId) {
        if (calResource == null || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        userDataController.findUser(userId);

        CalendarListResource calListResource = new CalendarListResource(); // Inicialmente, o usuário é o dono do calendário
        calListResource.setId(calResource.getCalendarId());
        calListResource.setOwner(userDataController.getCalendarPerson(userId));
        userRepository.insertCalendarListResourceByUserId(userId, calListResource);

        return calListResource;
    }

    public CalendarListResource addCalendarListResourceFromCalendar(String userId, String calendarId) throws Exception{
        Optional<CalendarResource> cal = calendarRepository.findByCalendarId(calendarId);
        if (cal.isEmpty()){
            throw new Exception("Calendário não existe");
        }
         userRepository.addCalendarListResource(userId, cal.get().toCalendarListResource());
         return cal.get().toCalendarListResource();
    
    }

    public CalendarResource addCalendar(CalendarResource calResource, String userId) {
        if (calResource == null ) {
            throw new IllegalArgumentException("Calendário não pode ser nulo");
        } else if (userId == null){
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");

        } else if (userId.isEmpty()){
            throw new IllegalArgumentException("ID do usuário não pode ser vazio.");

        }
        String calendarId = calResource.getCalendarId();
        User user = userDataController.findUser(userId);
        
        if (calendarRepository.existsById(calendarId)) {
            throw new IllegalArgumentException("Calendário com ID '" + calResource.getCalendarId() + "' já existe.");
        }
        //Como o usuário está criando um calendário, ele é o owner
        calResource.setOwner(user.getAsCalendarPerson());
        
        this.addCalendarListResource(calResource.toCalendarListResource(), userId);
        return calendarRepository.save(calResource);
    }

    public CalendarListResource getCalendarListResource(String calendarId, String userId) {
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        userDataController.findUser(userId);

        CalendarListResource calListResource = userRepository.findCalendarListResourceByIdAndCalendarId(userId, calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                        + "' não encontrado para o usuário de ID '" + userId + "'."));

        return calListResource;
    }

    protected CalendarListResource updateCalendarListResource(String calendarId, CalendarListResource calListUserItem, String userId) {
        if (calListUserItem.getAccessRole() == "owner" || calListUserItem.getAccessRole() == "writer") {
            userRepository.insertCalendarListResourceByUserId(userId, calListUserItem);
            return calListUserItem;
        }
        throw new IllegalArgumentException(
                "Acesso negado: o usuário não tem permissão para atualizar este calendário.");

    }

    protected CalendarResource updateCalendarResource(String calendarId, CalendarResource calResource, String userId, String accessRole) {

        if (accessRole == "owner" || accessRole == "writer") {
            calendarRepository.save(calResource);
            return calResource;
        }
        throw new IllegalArgumentException(
                "Acesso negado: o usuário não tem permissão para atualizar este calendário.");

    }

    public CalendarListResource updateCalendar(String calendarId, CalendarListResource calListResource, String userId) {
        CalendarListResource registeredCalListUserItem = userRepository
                .findCalendarListResourceByIdAndCalendarId(userId, calendarId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "CalendarListResource com ID '" + calendarId + "' não encontrado para o usuário de ID '"
                                + userId + "'"));

        CalendarListResource calListUserItem = calListResource;
        CalendarResource calResource = calListResource.extractCalendarResource();

        if (registeredCalListUserItem.getAccessRole() == "owner" || registeredCalListUserItem.getAccessRole() == "writer") {
            updateCalendarListResource(calendarId, calListUserItem, userId);
            updateCalendarResource(calendarId, calResource, userId, userId);
        }
        throw new IllegalArgumentException(
                "Acesso negado: o usuário não tem permissão para atualizar este calendário.");

    }

    // @Override
    // public CalendarListResource patchCalendar(String calendarId, CalendarResource calResource, String userId) {
    //     CalendarListResource calListResource = userRepository
    //             .findCalendarListResourceByUserIdAndCalendarId(userId, calendarId)
    //             .orElseThrow(() -> new IllegalArgumentException(
    //                     "CalendarListResource com ID '" + calendarId + "' não encontrado para o usuário de ID '"
    //                             + userId));

    //     if (calListResource.getAccessRole() == "owner" || calListResource.getAccessRole() == "writer") {
    //         if (calListResource.getSummary() != null) {
    //             calListResource.setSummary(calListResource.getSummary());
    //         }
    //         if (calListResource.getDescription() != null) {
    //             calListResource.setDescription(calListResource.getDescription());
    //         }
    //         if (calListResource.getLocation() != null) {
    //             calListResource.setLocation(calListResource.getLocation());
    //         }
    //         if (calListResource.getTimeZone() != null) {
    //             calListResource.setTimeZone(calListResource.getTimeZone());
    //         }
    //         if (calListResource.getAccessRole() != null) {
    //             calListResource.setAccessRole(calListResource.getAccessRole());
    //         }

    //         calendarRepository.save(calResource);
    //         return calListResource;
    //     }

    //     throw new IllegalArgumentException(
    //             "Acesso negado: o usuário não tem permissão para atualizar este calendário.");
    // }
    public ArrayList<CalendarListResource> getCalendarList(String userId){
        User user = userDataController.findUser(userId);
        return user.getCalendarList();
    }

    public ArrayList<CalendarResource> getCalendars(String userId) throws Exception {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo ou vazio.");
        }

        User user = userDataController.findUser(userId);
        ArrayList<CalendarResource> outputList = new ArrayList<>();

        ArrayList<CalendarListResource> calendarList = user.getCalendarList();
        for (CalendarListResource calListUserItem : calendarList) {
            CalendarResource calResource = calendarRepository
                .findByCalendarId(calListUserItem.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calListUserItem.getCalendarId()
                        + "' não encontrado."));
            outputList.add(calResource);
        }

        return outputList;
    }

    public CalendarResource getCalendarResource (String calendarId) {
        return calendarRepository.findById(calendarId)
        .orElseThrow(() -> new NoSuchElementException("Calendário de id: "+calendarId+" não encontrado"));    
    }

    
    public void removeCalendar(String calendarId, String userId) {
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        User user = userDataController.findUser(userId);
        try {
            CalendarResource calR = getCalendarResource(calendarId);
            if (calR.getOwner() == user.getAsCalendarPerson()){
                calendarRepository.deleteById(calendarId);
                userRepository.refreshLinks(calendarId);
            } else {
                userRepository.deleteCalendarListResourceById(userId, calendarId);
            }
            // Se o usuário tentar deletar um calendário que não existe, não precisamos de fazer nada
        } catch (NoSuchElementException e) {
            System.err.println("Calendário não existe");
        }
    }
}
