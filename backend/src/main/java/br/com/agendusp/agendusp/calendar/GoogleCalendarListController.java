package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.google.api.client.json.Json;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class GoogleCalendarListController implements CalendarListController {

    private final RestClient restClient;
    private final OAuth2AuthorizedClient auhtorizedClient;
    private final Gson gson;

    public GoogleCalendarListController(OAuth2AuthorizedClient authorizedClient, RestClient restClient, Gson gson) {
        this.restClient = restClient;
        this.auhtorizedClient = authorizedClient;
        this.gson = gson;


    }

    /**
     * Remove uma agenda da lista de agendas do usuário.
     *
     * @param calendarId o ID da agenda a ser removida (ex.: "primary" ou o ID customizado)
     * @return 204 No Content em caso de sucesso, ou 500 em caso de erro
     */
    @Override
    @DeleteMapping("/google/calendarList/{calendarId}")
    public ResponseEntity<Void> delete(@PathVariable String calendarId, OAuth2AuthorizedClient authorizedClient) {
        try {
            restClient.delete()
                .uri(uriBuilder -> uriBuilder
                    .path("/calendar/v3/users/me/calendarList/{id}")
                    .build(calendarId))
                .headers(headers -> 
                    headers.setBearerAuth(
                        authorizedClient.getAccessToken().getTokenValue()))
                .retrieve()
                .toBodilessEntity()   // esperamos sem corpo de resposta
                .block();             // bloqueia até completar a chamada

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).build();
        }
    }
/*
    public ArrayList<Calendar> get(Calendar calendar){}
    public CalendarList insert(Calendar calendar){}*/
    public HttpStatusCode delete(){}
    public CalendarListResource get(Calendar calendar){

        ResponseEntity<Json> calendarResponse = restClient.get()
                .uri("https://www.googleapis.com/calendar/v3/users/me/calendarList/"+calendar.getId())
                .headers(headers -> headers.setBearerAuth(auhtorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(Json.class);
        CalendarListResource calRes = new Gson.fromJson(calendarResponse.getBody(), CalendarListResource.class);
        return new CalendarListResource(gson);
    }
    
    CalendarListResource insert(Calendar calendar){}

    @GetMapping("/google/calendarList/list")
    public CalendarListResource list(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
          ResponseEntity<Gson> calList = restClient.get()
                .uri("/calendarList")
                .headers(headers -> headers.setBearerAuth(auhtorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(Gson.class);
        CalendarListResource calendarListResource = new CalendarListResource(calList.getBody());
        return calendarListResource;
    }
    //public CalendarListResource patch(Calendar calendar){}
    //public CalendarListResourceve().toEntity(Json.class);
    //    CalendarListResource calendarListResource = new CalendarListResource();
    //}
    public CalendarList patch(Calendar calendar){return new CalendarList()}
    public CalendarList update(Calendar calendar){return new CalendarList()}
    public WatchResponse watch(WatchRequest watchRequest){return new WatchResponse()}*/
}
