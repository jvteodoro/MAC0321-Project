package br.com.agendusp.agendusp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import br.com.agendusp.agendusp.dataobjects.aiObjects.AIRequest;
import br.com.agendusp.agendusp.dataobjects.aiObjects.AIResponse;
import org.springframework.web.bind.annotation.GetMapping;


@RestController // para poder fazer requisicoes http
public class AIControler {

    @Autowired
    RestClient restClient; // para o spring poder gerar requisicoes http com java de modo facil

    public AIControler(RestClient restClient) {
        this.restClient = restClient;
    }

    public AIControler() {
        // Construtor vazio para o Spring poder instanciar a classe
        // (necessário para o RestController)
    }

    @GetMapping("/aiReport")
    public AIResponse gerarInforme(AIRequest airequest) { // a requisicao da ia precisa mandar isso {"model" :
                                                          // "llama3.2:1b", "prompt": "-prompt para a ia-", "stream":
                                                          // false}
        ResponseEntity<AIResponse> response = restClient.post()
                .uri("http://localhost:11434/api/generate")
                .body(airequest)
                .retrieve()
                .toEntity(AIResponse.class);
        return response.getBody();
    }
}
