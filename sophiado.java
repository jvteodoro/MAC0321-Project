package br.com.agendusp.agendusp.calendar;

import java.util.ArrayList;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.google.api.client.json.Json;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class GoogleCalendarListController implements CalendarListController {

    private final RestClient restClient;
    private final OAuth2AuthorizedClient auhtorizedClient;

    public GoogleCalendarListController(OAuth2AuthorizedClient authorizedClient, RestClient restClient ) {
        this.restClient = restClient;
        this.auhtorizedClient = authorizedClient;


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

    @GetMapping("/google/calendarList/list")
    public CalendarList list(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) {
          ResponseEntity<Json> calList = restClient.get()
                .uri("/calendarList")
                .headers(headers -> headers.setBearerAuth(auhtorizedClient.getAccessToken().getTokenValue()))
                .retrieve().toEntity(Json.class);
        CalendarListResource calendarListResource = new CalendarListResource();
    }
    // patch nao mexe: id, primary, etag, kind, accessRole, deleted (atualizar no get e no list)
    //quem fez esse acho legal verificar eses campos
    
    
    @PatchMapping("/calendar/{calendarId}")
    public CalendarList patch(@PathVariable String calendarId, @RequestBody Calendar calendar) {
        calendar.setId(calendarId);
        Calendar calendarAtual = get(calendar).stream() //pega o calendario def pelo rest controller
                .filter(c -> c.getId().equals(calendarId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("O calendário " + calendarId + " não foi encontrado."));

        // atualiza o que não está sem nada
        // patch nao mexe: id, primary, etag, kind, accessRole, deleted (atualizar no get e no list)
        if (calendar.getSummary() != null) {
            calendarAtual.setSummary(calendar.getSummary()); //nome
        }
        if (calendar.getDescription() != null) {
            calendarAtual.setDescription(calendar.getDescription()); 
        }
        if (calendar.getLocation() != null) {
            calendarAtual.setLocation(calendar.getLocation()); //localização vinculada a agenda (verificar loc de eventos)
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
        if (calendar.getHidden() != null) {
            calendarAtual.setHidden(calendar.getHidden()); //indica se a agenda esta oculta
        }
        if (calendar.getSelected() != null) {
            calendarAtual.setSelected(calendar.getSelected()); //praticamente igual ao hidden
        }
        if (calendar.getDefaultReminders() != null) {
            calendarAtual.setDefaultReminders(calendar.getDefaultReminders()); //lista de lembretes (acho que é array entao verificar a implementaçao)
        }
        if (calendar.getConferenceProperties() != null) {
            calendarAtual.setConferenceProperties(calendar.getConferenceProperties()); //google meet
        }
        return update(calendarAtual); 
        //update conforme orientação do google api
    }



    /* 
    public CalendarList update(Calendar calendar){}
    public WatchResponse watch(WatchRequest watchRequest){}*/
}
