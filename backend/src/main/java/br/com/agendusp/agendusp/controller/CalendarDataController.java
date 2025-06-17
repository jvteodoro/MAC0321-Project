package br.com.agendusp.agendusp.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarListUserItem;
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

    public CalendarListUserItem addCalendarListUserItem(CalendarResource calResource, String userId) {
        if (calResource == null || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        userDataController.findUser(userId);

        CalendarListUserItem calListUserItem = new CalendarListUserItem(calResource.getCalendarId(), 
        "",
        "", 
        "", 
        false, 
        true, 
        userDataController.getAccessRole(calResource, userId)); // Inicialmente, o usuário é o dono do calendário

        userRepository.updateOneByUserId(userId, calListUserItem);

        return calListUserItem;
    }

    public CalendarResource addCalendar(CalendarResource calResource, String userId) {
        if (calResource == null || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        userDataController.findUser(userId);

        if (calendarRepository.existsById(calResource.getCalendarId())) {
            throw new IllegalArgumentException("Calendário com ID '" + calResource.getCalendarId() + "' já existe.");
        }

        this.addCalendarListUserItem(calResource, userId);
        return calendarRepository.save(calResource);
    }

    public CalendarListResource getCalendarListResource(String calendarId, String userId) {
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        userDataController.findUser(userId);

        CalendarListUserItem calListUserItem = userRepository.findCalendarListUserItemByUserIdAndCalendarId(userId, calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId
                        + "' não encontrado para o usuário de ID '" + userId + "'."));

        CalendarResource calResource = calendarRepository.findByCalendarId(calendarId)
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calendarId + "' não encontrado."));
        boolean primary = calResource.getOwner().getId().equals(userId);

        return new CalendarListResource(calendarId, primary, calListUserItem, calResource);
    }

    protected CalendarListUserItem updateCalendarListUserItem(String calendarId, CalendarListUserItem calListUserItem, String userId) {
        if (calListUserItem.getAccessRole() == "owner" || calListUserItem.getAccessRole() == "writer") {
            userRepository.updateOneByUserId(userId, calListUserItem);
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
        CalendarListUserItem registeredCalListUserItem = userRepository
                .findCalendarListUserItemByUserIdAndCalendarId(userId, calendarId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "CalendarListResource com ID '" + calendarId + "' não encontrado para o usuário de ID '"
                                + userId + "'"));

        CalendarListUserItem calListUserItem = calListResource.extractCalendarListUserItem();
        CalendarResource calResource = calListResource.extractCalendarResource();

        if (registeredCalListUserItem.getAccessRole() == "owner" || registeredCalListUserItem.getAccessRole() == "writer") {
            updateCalendarListUserItem(calendarId, calListUserItem, userId);
            updateCalendarResource(calendarId, calResource, userId, userId);
        }
        throw new IllegalArgumentException(
                "Acesso negado: o usuário não tem permissão para atualizar este calendário.");

    }

    // @Override
    // public CalendarListResource patchCalendar(String calendarId, CalendarResource calResource, String userId) {
    //     CalendarListResource calListResource = userRepository
    //             .findCalendarListUserItemByUserIdAndCalendarId(userId, calendarId)
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

    public ArrayList<CalendarListResource> getCalendars(String userId) throws Exception {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo ou vazio.");
        }

        User user = userDataController.findUser(userId);
        ArrayList<CalendarListResource> outputList = new ArrayList<>();
        boolean primary;

        ArrayList<CalendarListUserItem> calendarList = user.getCalendarList();
        for (CalendarListUserItem calListUserItem : calendarList) {
            CalendarResource calResource = calendarRepository
                .findByCalendarId(calListUserItem.getCalendarId())
                .orElseThrow(() -> new IllegalArgumentException("Calendário com ID '" + calListUserItem.getCalendarId()
                        + "' não encontrado."));
            primary = calResource.getOwner().getId().equals(userId);
            outputList.add(new CalendarListResource(calListUserItem.getCalendarId(), primary, calListUserItem, calResource));
        }

        return outputList;
    }

    
    public void removeCalendar(String calendarId, String userId) {
        if (calendarId == null || calendarId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        userDataController.findUser(userId);

        CalendarListUserItem calListResource = userDataController.findCalendarListUserItem(userId, calendarId);

        if (calListResource.getAccessRole() == "owner") {
            calendarRepository.deleteById(calendarId);
            userRepository.refreshLinks(calendarId);
        } else {
            userRepository.deleteCalendarListResourceById(userId, calendarId);
        }
    }
}
