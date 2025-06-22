package br.com.agendusp.agendusp.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RestController;

import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.controller.CalendarDataController;
import br.com.agendusp.agendusp.controller.EventsDataController;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.dataobjects.EventPool;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.User;

@RestController
public class PromptBuilder {

    @Autowired
    UserDataController userDataController;
    @Autowired
    CalendarDataController calendarDataController;
    @Autowired
    EventsDataController eventsDataController;

    public String getPromptParaInformeSemana(
            @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient, String dataInicial, String calendarId) {

        StringBuilder prompt = new StringBuilder();
        User user = userDataController.findUser(authorizedClient.getPrincipalName());
        String personName = user.getName();
        String userId = user.getId();
        ArrayList<CalendarListResource> calendars = user.getCalendarList();
        ArrayList<EventsResource> compromissos = new ArrayList<>();
        ArrayList<EventsResource> eventosCancelados = new ArrayList<>();
        ArrayList<EventsResource> allTheEvents = new ArrayList<>();
        ArrayList<EventPool> enquetesCriadas = new ArrayList<>();
        ArrayList<EventPool> enquetesRespondidas = new ArrayList<>();

        //quebra a string recebida
        String ano = dataInicial.substring(0, 4);
        String mes = dataInicial.substring(5, 7);
        String dia = dataInicial.substring(8, 10);
        String hora = dataInicial.substring(13, 15);
        String minuto = dataInicial.substring(16, 18);  
        String segundo = dataInicial.substring(19, 21);
        LocalDateTime dataInicialDate = LocalDateTime.of(Integer.parseInt(ano), Integer.parseInt(mes),
                Integer.parseInt(dia), Integer.parseInt(hora), Integer.parseInt(minuto), Integer.parseInt(segundo));
        
        //MOSTRA COMPROMISSOS
        // recebe o calendarId como parâmetro, mas se calendarId for nulo, mostra todos os calendarios
        if (calendarId == null){
            for (CalendarListResource calendar : calendars) {
                calendarId = calendar.getCalendarId();
                compromissos.addAll(eventsDataController.getEvents(calendarId, userId));
        }
        }
        else {
            compromissos = eventsDataController.getEvents(calendarId, userId);
        }

        //PEGA EVENTOS CANCELADOS
        if (calendarId == null){
            for (CalendarListResource calendar : calendars) {
                calendarId = calendar.getCalendarId();
                allTheEvents.addAll(eventsDataController.getEvents(calendarId, userId));
            }
        }
        else {
            allTheEvents = eventsDataController.getEvents(calendarId, userId);
        }

        for (EventsResource event : allTheEvents){
            if (event.getStatus().equals("cancelled")) {
                eventosCancelados.add(event);
            }
        }

        
        //PEGA ENQUETES QUE EU CRIEI
        enquetesCriadas = user.getEventPoolList();

        //PEGA ENQUETES QUE EU RESPONDI
        enquetesRespondidas = user.getEventPoolNotifications();

    

        prompt.append("Você é uma inteligência artificial que gera relatórios de compromissos semanais.\n");
        prompt.append("Sua tarefa é criar um informe formal e claro com base nos compromissos da semana.\n");
        prompt.append("Considere o seguinte contexto:\n\n");

        prompt.append("- Nome da pessoa: ").append(personName).append("\n");
        prompt.append("- Contexto: A pessoa deseja receber um resumo dos compromissos agendados para a semana do dia "
                + dataInicial + ".\n");
        prompt.append("\nCompromissos:\n");
        if (compromissos == null || compromissos.isEmpty()) {
            prompt.append("Não há compromissos!\n");
        } else {
            for (EventsResource compromisso : compromissos) {
                prompt.append("- ").append(compromisso).append("\n");
            }
        }
        prompt.append("\nResultado de enquetes criadas pelo usuário:\n");
        if (enquetesCriadas == null || enquetesCriadas.isEmpty()) {
            prompt.append("Não há enquetes criadas pelo usuário!\n");
        } else {
            for (EventPool enquete : enquetesCriadas) {
                prompt.append("- ").append(enquete).append("\n");
            }
        }
        prompt.append("\nResultado de enquetes respondidas pelo usuário:\n");
        if (enquetesRespondidas == null || enquetesRespondidas.isEmpty()) {
            prompt.append("Não há enquetes respondidas pelo usuário!\n");
        } else {
            for (EventPool enquete : enquetesRespondidas) {
                prompt.append("- ").append(enquete).append("\n");
            }
        }
        prompt.append("\nEventos cancelados:\n");
        if (eventosCancelados == null || eventosCancelados.isEmpty()) {
            prompt.append("Não há enquetes!\n");
        } else {
            for (EventsResource evento : eventosCancelados) {
                prompt.append("- ").append(evento).append("\n");
            }
        }
        prompt.append("- Linguagem: Seja objetivo, use um tom profissional e evite repetições desnecessárias.\n");

        prompt.append(
                "Por favor, enfatize informações relevantes como sobreposição de horários, eventos recentemente cancelados e "
                        +
                        "enquetes recentes de horário para reunião.\n");

        return prompt.toString();
    }

    public String getPromptParaInformeDia(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient, String dataInicial, String calendarId) {

        StringBuilder prompt = new StringBuilder();
        User user = userDataController.findUser(authorizedClient.getPrincipalName());
        String personName = user.getName();
        String userId = user.getId();
        ArrayList<CalendarListResource> calendars = user.getCalendarList();
        ArrayList<EventsResource> compromissos = new ArrayList<>();
        ArrayList<EventsResource> eventosCancelados = new ArrayList<>();
        ArrayList<EventsResource> allTheEvents = new ArrayList<>();
        ArrayList<EventPool> enquetesCriadas = new ArrayList<>();
        ArrayList<EventPool> enquetesRespondidas = new ArrayList<>();

        //quebra a string recebida
        String ano = dataInicial.substring(0, 4);
        String mes = dataInicial.substring(5, 7);
        String dia = dataInicial.substring(8, 10);
        String hora = dataInicial.substring(13, 15);
        String minuto = dataInicial.substring(16, 18);  
        String segundo = dataInicial.substring(19, 21);
        LocalDateTime dataInicialDate = LocalDateTime.of(Integer.parseInt(ano), Integer.parseInt(mes),
                Integer.parseInt(dia), Integer.parseInt(hora), Integer.parseInt(minuto), Integer.parseInt(segundo));
        
        //MOSTRA COMPROMISSOS
        // recebe o calendarId como parâmetro, mas se calendarId for nulo, mostra todos os calendarios
        if (calendarId == null){
            for (CalendarListResource calendar : calendars) {
                calendarId = calendar.getCalendarId();
                compromissos.addAll(eventsDataController.getEvents(calendarId, userId));
        }
        }
        else {
            compromissos = eventsDataController.getEvents(calendarId, userId);
        }

        //PEGA EVENTOS CANCELADOS
        if (calendarId == null){
            for (CalendarListResource calendar : calendars) {
                calendarId = calendar.getCalendarId();
                allTheEvents.addAll(eventsDataController.getEvents(calendarId, userId));
            }
        }
        else {
            allTheEvents = eventsDataController.getEvents(calendarId, userId);
        }

        for (EventsResource event : allTheEvents){
            if (event.getStatus().equals("cancelled")) {
                eventosCancelados.add(event);
            }
        }

        
        //PEGA ENQUETES QUE EU CRIEI
        enquetesCriadas = user.getEventPoolList();

        //PEGA ENQUETES QUE EU RESPONDI
        enquetesRespondidas = user.getEventPoolNotifications();

        prompt.append("Você é uma inteligência artificial que gera relatórios de compromissos diários.\n");
        prompt.append("Sua tarefa é criar um informe formal e claro com base nos compromissos do dia " + dia + ".\n");
        prompt.append("Considere o seguinte contexto:\n\n");

        prompt.append("- Nome da pessoa: ").append(personName).append("\n");
        prompt.append(
                "- Contexto: A pessoa deseja receber um resumo das informações e dos compromissos agendados para o dia "
                        + dia + ".\n");
        prompt.append("\nCompromissos:\n");
        if (compromissos == null || compromissos.isEmpty()) {
            prompt.append("Não há compromissos!\n");
        } else {
            for (EventsResource compromisso : compromissos) {
                prompt.append("- ").append(compromisso).append("\n");
            }
        }
        prompt.append("\nResultado de enquetes criadas pelo usuário:\n");
        if (enquetesCriadas == null || enquetesCriadas.isEmpty()) {
            prompt.append("Não há enquetes criadas pelo usuário!\n");
        } else {
            for (EventPool enquete : enquetesCriadas) {
                prompt.append("- ").append(enquete).append("\n");
            }
        }
        prompt.append("\nResultado de enquetes respondidas pelo usuário:\n");
        if (enquetesRespondidas == null || enquetesRespondidas.isEmpty()) {
            prompt.append("Não há enquetes respondidas pelo usuário!\n");
        } else {
            for (EventPool enquete : enquetesRespondidas) {
                prompt.append("- ").append(enquete).append("\n");
            }
        }
        prompt.append("\nEventos cancelados:\n");
        if (eventosCancelados == null || eventosCancelados.isEmpty()) {
            prompt.append("Não há enquetes!\n");
        } else {
            for (EventsResource evento : eventosCancelados) {
                prompt.append("- ").append(evento).append("\n");
            }
        }

        prompt.append("\n- Linguagem: Seja objetivo, use um tom profissional e evite repetições desnecessárias.\n");

        prompt.append(
                "Por favor, enfatize informações relevantes como sobreposição de horários, eventos recentemente cancelados e "
                        +
                        "enquetes recentes de horário para reunião.\n");

        return prompt.toString();
    }
}

