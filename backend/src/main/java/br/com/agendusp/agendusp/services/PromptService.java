package br.com.agendusp.agendusp.services;

import br.com.agendusp.agendusp.documents.EventsResource;
import br.com.agendusp.agendusp.documents.User;
import br.com.agendusp.agendusp.documents.CalendarListResource;
import br.com.agendusp.agendusp.dataobjects.PollNotification;
import br.com.agendusp.agendusp.dataobjects.eventObjects.EventPoll;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromptService {

    private final ObjectMapper objectMapper;

    public PromptService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getPromptParaInformeSemana(User user, List<CalendarListResource> calendars, List<EventsResource> commitments,
                                    List<EventsResource> cancelledEvents, List<EventPoll> createdPolls,
                                    List<PollNotification> answeredPolls, LocalDateTime startDate) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Você é uma inteligência artificial que gera relatórios de compromissos semanais.\n");
        prompt.append("Sua tarefa é criar um informe formal e claro com base nos compromissos da semana.\n");
        prompt.append("Considere o seguinte contexto:\n\n");
        prompt.append("- Nome da pessoa: ").append(user.getName()).append("\n");
        prompt.append("- Contexto: Resumo dos compromissos da semana a partir de ").append(startDate).append(".\n\n");

        appendCommitments(prompt, commitments);
        appendPolls(prompt, createdPolls, answeredPolls);
        appendCancelledEvents(prompt, cancelledEvents);

        prompt.append("- Linguagem: Seja objetivo, use um tom profissional e evite repetições desnecessárias.\n");
        prompt.append("Enfatize sobreposição de horários, eventos cancelados e enquetes recentes para reuniões.\n");
        prompt.append("Não invente informações sobre os compromissos.\n");
        return prompt.toString();
    }

    public String getPromptParaInformeDia(User user, List<CalendarListResource> calendars, List<EventsResource> commitments,
                                   List<EventsResource> cancelledEvents, List<EventPoll> createdPolls,
                                   List<PollNotification> answeredPolls, LocalDateTime startDate) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Você é uma inteligência artificial que gera relatórios de compromissos diários.\n");
        prompt.append("Sua tarefa é criar um informe formal e claro com base nos compromissos do dia ")
                .append(startDate.toLocalDate()).append(".\n");
        prompt.append("Considere o seguinte contexto:\n\n");
        prompt.append("- Nome da pessoa: ").append(user.getName()).append("\n");
        prompt.append("- Contexto: Resumo dos compromissos de ").append(startDate.toLocalDate()).append(".\n\n");

        appendCommitments(prompt, commitments);
        appendPolls(prompt, createdPolls, answeredPolls);
        appendCancelledEvents(prompt, cancelledEvents);

        prompt.append("- Linguagem: Seja objetivo, use um tom profissional e evite repetições desnecessárias.\n");
        prompt.append("Enfatize sobreposição de horários, eventos cancelados e enquetes recentes para reuniões.\n");

        return prompt.toString();
    }

    private void appendCommitments(StringBuilder prompt, List<EventsResource> commitments) {
        prompt.append("Compromissos:\n");
        if (commitments == null || commitments.isEmpty()) {
            prompt.append("Não há compromissos registrados.\n");
        } else {
            commitments.forEach(event -> prompt.append("- ").append(serializeObject(event)).append("\n"));
        }
    }

    private void appendPolls(StringBuilder prompt, List<EventPoll> createdPolls, List<PollNotification> answeredPolls) {
        prompt.append("\nEnquetes criadas pelo usuário:\n");
        if (createdPolls == null || createdPolls.isEmpty()) {
            prompt.append("Não há enquetes criadas pelo usuário.\n");
        } else {
            System.out.print("[DEBUG] " + createdPolls);
            createdPolls.forEach(poll -> prompt.append("- ").append(serializeObject(poll)).append("\n"));
        }

        prompt.append("\nEnquetes respondidas pelo usuário:\n");
        if (answeredPolls == null || answeredPolls.isEmpty()) {
            prompt.append("Não há enquetes respondidas pelo usuário.\n");
        } else {
            answeredPolls.forEach(poll -> prompt.append("- ").append(serializeObject(poll)).append("\n"));
        }
    }

    private void appendCancelledEvents(StringBuilder prompt, List<EventsResource> cancelledEvents) {
        prompt.append("\nEventos cancelados recentemente:\n");
        if (cancelledEvents == null || cancelledEvents.isEmpty()) {
            prompt.append("Não há eventos cancelados recentemente.\n");
        } else {
            cancelledEvents.forEach(event -> prompt.append("- ").append(serializeObject(event)).append("\n"));
        }
    }

    private String serializeObject(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "[Erro ao serializar objeto]";
        }
    }
}
