package br.com.agendusp.agendusp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.google.api.services.calendar.Calendar.CalendarList;

import br.com.agendusp.agendusp.controller.calendarControllers.CalendarListController;
import br.com.agendusp.agendusp.dataobjects.aiObjects.AIRequest;
import br.com.agendusp.agendusp.dataobjects.aiObjects.AIResponse;
import br.com.agendusp.agendusp.services.PromptBuilder;

import org.springframework.web.bind.annotation.GetMapping;


@RestController // para poder fazer requisicoes http
public class AIControler {

    @Autowired
    RestClient restClient; // para o spring poder gerar requisicoes http com java de modo facil
    @Autowired
    PromptBuilder promptBuilder;

    public AIControler(RestClient restClient) {
        this.restClient = restClient;
    }

    public AIControler() {
        // Construtor vazio para o Spring poder instanciar a classe
        // (necessário para o RestController)
    }

    @GetMapping("/aiReport")
    public String gerarInforme(@RequestParam String initialDate, @RequestParam String calendarID, @RegisteredOAuth2AuthorizedClient("Google") OAuth2AuthorizedClient authorizedClient) { // a requisicao da ia precisa mandar isso {"model" :
                                                          // "llama3.2:1b", "prompt": "-prompt para a ia-", "stream":
                                                          // false}
        String prompt = promptBuilder.getPromptSemana(authorizedClient, initialDate, calendarID); // chama o promptBuilder para gerar o prompt
        AIRequest airequest = new AIRequest.AiRequestBuilder("llama3.2:1b", prompt, "false")
                .build();
        ResponseEntity<String> response = restClient.post()
                .uri("http://localhost:11434/api/generate")
                .body(airequest)
                .retrieve()
                .toEntity(String.class);
        return response.getBody();
    }
}
