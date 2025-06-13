package br.com.agendusp.agendusp.controller.google;
// package br.com.agendusp.agendusp.calendar;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.checkerframework.checker.units.qual.s;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.agendusp.agendusp.CustomOAuth2User;
import br.com.agendusp.agendusp.controller.CalendarListController;
import br.com.agendusp.agendusp.controller.DataController;
import br.com.agendusp.agendusp.dataobjects.UserInfo;
import br.com.agendusp.agendusp.dataobjects.WatchRequest;
import br.com.agendusp.agendusp.dataobjects.WatchResponse;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.CalendarResource;
import br.com.agendusp.agendusp.documents.User;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class GoogleCalendarListController {

    private final RestClient restClient;
    private final Gson gson;
    @Autowired
    private DataController dataController;
    @Autowired
    ObjectMapper objMapper;
    public GoogleCalendarListController(RestClient restClient, Gson gson) {
        
        this.restClient = restClient;
        //authToken.getAcess
    //     this.userInfo = restClient.get()
    //     .uri("https://www.googleapis.com/oauth2/v2/userinfo")
    //   //  .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
    //     .retrieve().toEntity(String.class).getBody();
        this.gson = gson;

    }

    // @PostMapping()
    // public ResponseEntity<Void> fetch(OAuth2AuthorizedClient authorizedClient){
    //     authorizedClient.getPrincipalName()
    // }

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
    /*
     * public ArrayList<Calendar> get(Calendar calendar){}
     * public CalendarList insert(Calendar calendar){}
     */

    @GetMapping("/google/userInfo")
    public String getUserInfo(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) throws Exception{
        ResponseEntity<UserInfo> response = restClient.get()
        .uri("https://www.googleapis.com/oauth2/v2/userinfo")
        .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
        .retrieve().toEntity(UserInfo.class);
        UserInfo inf = response.getBody();
        User user = new User();
        user.setEmail(inf.getEmail());
        user.setGoogleId(inf.getId());
        user.setId(inf.getId());
        user.setUsername(inf.getName());
        //user.setId(inf.getId());
        dataController.createUser(user);

        return objMapper.writeValueAsString(user);
    }

     @GetMapping("/google/calendarList/get")
    public CalendarListResource get(
        @RequestParam String calendarId, 
        @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        ResponseEntity<String> calendarResponse = restClient.get()
                .uri("https://www.googleapis.com/calendar/v3/users/me/calendarList/joao.v.teodoro@usp.br")
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(String.class);
        CalendarListResource calRes = gson.fromJson(calendarResponse.getBody(), CalendarListResource.class);
        return calRes;
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
    public String list(
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
        ResponseEntity<String> calList = restClient.get()
                .uri("/calendarList")
                .headers(headers -> headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(String.class);
        
        System.out.println("RUNNING, response:"+calList.getBody().toString());
        return calList.getBody();
    }
    // public CalendarListResourceve().toEntity(Json.class);
    // CalendarListResource calendarListResource = new CalendarListResource();
    // }

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
    // .retrieve().toEntity(Json.class);
    // CalendarListResource calendarListResource = new CalendarListResource();

    // quemz esse acho legal verificar eses campos

    @PatchMapping("/calendar/{calendarId}")
    public CalendarListResource patch(@PathVariable String calendarId, @RequestBody CalendarListResource calendar) {
        calendar.setId(calendarId);
        CalendarListResource calendarAtual = calendar;
        // CalendarListResource calendarAtual = get(calendar).stream() // pega o calendario def pelo rest controller
        //         .filter(c -> c.getId().equals(calendarId))
        //         .findFirst()
        //         .orElseThrow(() -> new IllegalArgumentException("O calendário " + calendarId + " não foi encontrado."));

        // atualiza o que não está sem nada
        // patch nao mexe: id, primary, etag, kind, accessRole, deleted (atualizar no
        // get e no list)
        if (calendar.getSummary() != null) {
            calendarAtual.setSummary(calendar.getSummary()); // nome
        }
        if (calendar.getDescription() != null) {
            calendarAtual.setDescription(calendar.getDescription());
        }
        if (calendar.getLocation() != null) {
            calendarAtual.setLocation(calendar.getLocation()); // localização vinculada a agenda (verificar loc de
                                                               // eventos)
        }
        if (calendar.getTimeZone() != null) {
            calendarAtual.setTimeZone(calendar.getTimeZone());
        }
        if (calendar.getColorId() != null) {
            calendarAtual.setColorId(calendar.getColorId());
        }
        if (calendar.getBackgroundColor() != null) {
            calendarAtual.setBackgroundColor(calendar.getBackgroundColor());
        }
        if (calendar.getForegroundColor() != null) {
            calendarAtual.setForegroundColor(calendar.getForegroundColor());
        }
        // Opções ainda não implementadas
        // if (calendar.getHidden() != null) {
        //     calendarAtual.setHidden(calendar.getHidden()); // indica se a agenda esta oculta
        // }
        // if (calendar.getSelected() != null) {
        //     calendarAtual.setSelected(calendar.getSelected()); // praticamente igual ao hidden
        // }
        // if (calendar.getDefaultReminders() != null) {
        //     calendarAtual.setDefaultReminders(calendar.getDefaultReminders()); // lista de lembretes (acho que é array
        //                                                                        // entao verificar a implementaçao)
        // }
        // if (calendar.getConferenceProperties() != null) {
        //     calendarAtual.setConferenceProperties(calendar.getConferenceProperties()); // google meet
        // }
        // return update(calendarAtual);
        return update(calendar);
        // update conforme orientação do google api
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
