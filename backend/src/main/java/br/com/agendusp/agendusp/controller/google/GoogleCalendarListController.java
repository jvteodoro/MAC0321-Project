package br.com.agendusp.agendusp.controller.google;
// package br.com.agendusp.agendusp.calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.dataobjects.WatchRequest;
import br.com.agendusp.agendusp.dataobjects.WatchResponse;
import br.com.agendusp.agendusp.dataobjects.calendarObjects.CalendarListList;
import br.com.agendusp.agendusp.documents.CalendarListResource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class GoogleCalendarListController {

    @Autowired
    private RestClient restClient;
    @Autowired
    private  Gson gson;
    
    @Autowired
    private UserDataController userDataController;
    @Autowired
    ObjectMapper objMapper;
    @Autowired
    GoogleCalendarsController googleCalendarsController;

   

    /**
     * Remove uma agenda da lista de agendas do usuário.
     *
     * @param calendarId o ID da agenda a ser removida (ex.: "primary" ou o ID
     *                   customizado)
     * @return 204 No Content em caso de sucesso, ou 500 em caso de erro
     */
    @DeleteMapping("/google/calendarList/{calendarId}")   
    public ResponseEntity<Void> delete(@PathVariable String calendarId, OAuth2AuthorizedClient authorizedClient) {
        try {
            restClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/calendar/v3/users/me/calendarList/{id}")
                            .build(calendarId))
                    .headers(headers -> headers.setBearerAuth(
                            authorizedClient.getAccessToken().getTokenValue()))
                    .retrieve()
                    .toBodilessEntity(); // esperamos sem corpo de resposta
                    //.block(); // bloqueia até completar a chamada
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).build();
        }
    }
  
    @GetMapping("/google/test")
    public boolean[] test(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient){
        //return authorizedClient.getPrincipalName();
        boolean[] resp = {false, true, false};
        return resp;
    }

     @GetMapping("/google/calendarList/get")
    public CalendarListResource get(
        @RequestParam String calendarId, 
        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        return restClient.get()

                .uri("https://www.googleapis.com/calendar/v3/users/me/calendarList/"+calendarId)
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(CalendarListResource.class).getBody();
    }
    public String insert(CalendarListResource calendar,
        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        ResponseEntity<String> response = restClient.post()
                .uri("https://www.googleapis.com/calendar/v3/calendars")
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .body(calendar)
                .retrieve()
                .toEntity(String.class);
        return response.getBody();
}

    @GetMapping("/google/calendarList/list")
    public CalendarListList list(
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        CalendarListList calList = restClient.get()
                .uri("/calendarList")
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(CalendarListList.class).getBody();
        for (CalendarListResource resource: calList.getItems()){
            try {
                userDataController.insertCalendarListResource(authorizedClient.getPrincipalName(), resource);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        return calList;
    }

    public WatchResponse watch(WatchRequest watchRequest,
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        ResponseEntity<Gson> response = restClient.post()
                .uri("https://www.googleapis.com/calendar/v3/users/me/calendarList/watch")
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .body(watchRequest)
                .retrieve()
                .toEntity(Gson.class);
        return new WatchResponse(response.getBody());
    }
    

    public CalendarListResource update(CalendarListResource calendar) {
        if (calendar == null || calendar.getId() == null || calendar.getId().isEmpty()) {
            throw new IllegalArgumentException("Calendar ou ID do calendar não pode ser nulo/vazio.");
        }

        try {
            String calendarJson = gson.toJson(calendar);

            // Requisição para API
            ResponseEntity<String> response = restClient.put()
                    .uri("https://www.googleapis.com/calendar/v3/users/me/calendarList/" + calendar.getId())
               //     .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                    .body(calendarJson)
                    .retrieve()
                    .toEntity(String.class);

            CalendarListResource updatedCalendarListResource = gson.fromJson(response.getBody(),
                    CalendarListResource.class);
            return updatedCalendarListResource;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar o calendário: " + e.getMessage(), e);
        }
    }
}
