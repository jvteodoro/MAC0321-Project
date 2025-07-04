package br.com.agendusp.agendusp.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.client.RestClient;

import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.controller.UserDataController;
import br.com.agendusp.agendusp.controller.calendarControllers.CalendarDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventPollDataController;
import br.com.agendusp.agendusp.controller.eventControllers.EventsDataController;
import br.com.agendusp.agendusp.dataobjects.PollNotification;
import br.com.agendusp.agendusp.dataobjects.aiObjects.AIRequest;
import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPoll;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.events.EventPollNotification;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prompt")
public class PromptBuilder {

    @Autowired
    private PromptService promptService;
    @Autowired
    private UserDataController userDataController;
    @Autowired
    private CalendarDataController calendarDataController;
    @Autowired
    private EventsDataController eventsDataController;
    @Autowired
    private EventPollDataController eventPoolDataController;
    @Autowired
    private RestClient restClient;

    @GetMapping("/prompt/semana")
    public String getPromptSemana(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient,
                                  @RequestParam String dataInicial,
                                  @RequestParam(required = false) String calendarId) {
        LocalDateTime startDate = LocalDateTime.parse(dataInicial, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        User user = userDataController.findUser(authorizedClient.getPrincipalName());
        List<CalendarListResource> calendars = user.getCalendarList();

        List<EventsResource> commitments = getEventsForDateRange(user.getId(), calendars, calendarId, startDate, startDate.plusDays(7));
        List<EventsResource> cancelledEvents = commitments.stream().filter(e -> "cancelled".equals(e.getStatus())).collect(Collectors.toList());
        List<EventPoll> createdPolls = eventPoolDataController.getAllEventPools(user.getEventPoolList());
        List<PollNotification> answeredPolls = user.getEventPoolNotifications();

        String prompt = promptService.getPromptParaInformeSemana(user, calendars, commitments, cancelledEvents, createdPolls, answeredPolls, startDate);

        return callLLM(prompt);
    }

    @GetMapping("/prompt/dia")
    public String getPromptDia(@RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient,
                               @RequestParam String dataInicial,
                               @RequestParam(required = false) String calendarId) {
        LocalDateTime startDate = LocalDateTime.parse(dataInicial, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        User user = userDataController.findUser(authorizedClient.getPrincipalName());
        List<CalendarListResource> calendars = user.getCalendarList();

        List<EventsResource> commitments = getEventsForDateRange(user.getId(), calendars, calendarId, startDate, startDate.plusDays(1));
        List<EventsResource> cancelledEvents = commitments.stream().filter(e -> "cancelled".equals(e.getStatus())).collect(Collectors.toList());
        List<EventPoll> createdPolls = eventPoolDataController.getAllEventPools(user.getEventPoolList());
        List<PollNotification> answeredPolls = user.getEventPoolNotifications();


        String prompt = promptService.getPromptParaInformeDia(user, calendars, commitments, cancelledEvents, createdPolls, answeredPolls, startDate);

        return callLLM(prompt);
    }

    private List<EventsResource> getEventsForDateRange(String userId, List<CalendarListResource> calendars, String calendarId,
                                                       LocalDateTime start, LocalDateTime end) {
        if (calendarId != null) {
            return eventsDataController.getEvents(calendarId, userId).stream()
                    .filter(event -> isWithinRange(event, start, end))
                    .collect(Collectors.toList());
        } else {
            return calendars.stream()
                    .flatMap(cal -> eventsDataController.getEvents(cal.getCalendarId(), userId).stream())
                    .filter(event -> isWithinRange(event, start, end))
                    .collect(Collectors.toList());
        }
    }

    private boolean isWithinRange(EventsResource event, LocalDateTime start, LocalDateTime end) {
        return (event.getStart() != null && !event.getStart().isBefore(start) && event.getStart().isBefore(end));
    }

    private String callLLM(String prompt) {
        AIRequest aiRequest = new AIRequest.AiRequestBuilder("llama3.2:1b", prompt).setStream("false").build();
        return restClient.post().uri("http://localhost:11434/generate/api")
                .body(aiRequest)
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }
}