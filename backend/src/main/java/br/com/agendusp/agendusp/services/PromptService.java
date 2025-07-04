package br.com.agendusp.agendusp.services;

import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.events.EventPollNotification;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.dataobjects.PollNotification;
import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPoll;

import java.time.LocalDateTime;
import java.util.List;

public interface PromptService {
    String getPromptParaInformeDia(User user, List<CalendarListResource> calendars, List<EventsResource> commitments, 
                             List<EventsResource> cancelledEvents, List<EventPoll> createdPolls, 
                             List<PollNotification> answeredPolls, LocalDateTime startDate);

    String getPromptParaInformeSemana(User user, List<CalendarListResource> calendars, List<EventsResource> commitments, 
                            List<EventsResource> cancelledEvents, List<EventPoll> createdPolls, 
                            List<PollNotification> answeredPolls, LocalDateTime startDate);
}
