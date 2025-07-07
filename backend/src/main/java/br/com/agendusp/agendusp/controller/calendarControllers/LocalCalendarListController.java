package br.com.agendusp.agendusp.controller.calendarControllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RestController;

import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
public class LocalCalendarListController implements CalendarListController {

    @Autowired
    CalendarDataController calendarDataController;
    @Autowired
    UserDataController userDataController;
    @Autowired
    EventsDataController eventsDataController;

    public LocalCalendarListController() {
    }

    @DeleteMapping("/calendarList/delete") // Endpoint para deletar um CalendarListResource
    // O calendarId deve ser o ID do CalendarListResource que se deseja deletar
    public ResponseEntity<Void> delete(@RequestParam String calendarId,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        String userId = authorizedClient.getPrincipalName();
        calendarDataController.removeCalendar(calendarId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/calendarList/getUser") // Endpoint para obter o usuário autenticado
    public User getUser(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        // Obtém o usuário autenticado a partir do OAuth2AuthorizedClient
        if (authorizedClient == null || authorizedClient.getPrincipalName() == null) {
            throw new IllegalArgumentException("Usuário não autenticado ou principalName não disponível.");
        }
        return userDataController.findUser(authorizedClient.getPrincipalName());
    }

    @GetMapping("/calendarList/get") // Endpoint para obter um CalendarListResource específico
    public CalendarListResource get(@RequestParam String calendarId,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        String userId = authorizedClient.getPrincipalName();
        return calendarDataController.getCalendarListResource(calendarId, userId);
    }

    @PostMapping("/calendarList/insert") // Endpoint para inserir um novo CalendarListResource
    // O CalendarListResource deve conter o calendarId do CalendarResource que ele
    // representa
    public CalendarListResource insert(@RequestBody CalendarListResource calendar,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        String userId = authorizedClient.getPrincipalName();
        userDataController.insertCalendarListResource(userId, calendar);
        return calendar;
    }

    @GetMapping("/calendarList/list") // Endpoint para listar todos os CalendarListResource do usuário autenticado
    public ArrayList<CalendarListResource> list(
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        String userName = authorizedClient.getPrincipalName();
        User user = userDataController.findUser(userName);
        String userId = user.getGoogleId();
        return calendarDataController.getCalendarList(userId);
        // try {
        // return gson.toJson(dataController.getCalendars(userId));
        // } catch (Exception e) {
        // return gson.toJson("Erro ao buscar calendários: " + e.getMessage());
        // }
    }

    @PutMapping("/calendarList/update") // Endpoint para atualizar um CalendarListResource
    // O CalendarListResource deve conter o calendarId do CalendarResource que ele
    // representa
    public CalendarListResource update(@RequestBody CalendarListResource calendar,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        String userId = authorizedClient.getPrincipalName();
        // Verifica se o calendarId e userId são válidos
        if (calendar.getCalendarId() == null || calendar.getCalendarId().isEmpty() || userId == null
                || userId.isEmpty()) {
            throw new IllegalArgumentException("ID do calendário ou ID do usuário não podem ser nulos ou vazios.");
        }
        return calendarDataController.updateCalendarListResource(calendar.getId(), calendar, userId);
    }
    // @PostMapping("/calendarList/watch") // Endpoint para assistir a um
    // CalendarListResource
    // public String watch(@RequestBody WatchRequest watchRequest) {
    // return gson.toJson(dataController.watch(watchRequest));
    // }

    // Método comentado pois não é necessário no momento

    // @PatchMapping("/calendarList/patch")
    // public String patch(@RequestBody CalendarListResource calListResource,
    // @AuthenticationPrincipal CustomOAuth2User customUser) {
    // String userId = customUser.getUser().getId();
    // JsonObject body = new JsonObject(); // atualização parcial (dos atributos nao
    // nulos)
    // if (calListResource.getDescription() != null) {
    // body.addProperty("description", calListResource.getDescription());
    // }
    // if (calListResource.getLocation() != null) {
    // body.addProperty("location", calListResource.getLocation());
    // }
    // if (calListResource.getSummary() != null) {
    // body.addProperty("summary", calListResource.getSummary());
    // }
    // if (calListResource.getTimeZone() != null) {
    // body.addProperty("timeZone", calListResource.getTimeZone());
    // }
    // if (calListResource.getAccessRole() != null) {
    // body.addProperty("acessRole", calListResource.getAccessRole());
    // }

    // return gson.toJson(null);
    // // return
    // gson.toJson(dataController.patchCalendar(calListResource.getCalendarId(),
    // // gson.fromJson(body, CalendarListResource.class), userId));
    // }
}
