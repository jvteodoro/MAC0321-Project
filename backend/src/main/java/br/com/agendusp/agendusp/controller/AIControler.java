package br.com.agendusp.agendusp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.google.gson.Gson;

import br.com.agendusp.agendusp.dataobjects.AIRequest;
import br.com.agendusp.agendusp.dataobjects.AIResponse;


@RestController //para poder fazer requisicoes http 
public class AIControler {

    @Autowired
    RestClient restClient; //para o spring poder gerar requisicoes http com java de modo facil

    public AIControler(RestClient restClient) {
        this.restClient = restClient;
    }

    public AIResponse gerarInforme(AIRequest airequest){ //a requisicao da ia precisa mandar isso {"model" : "llama3.2:1b", "prompt": "-prompt para a ia-", "stream": false}
        ResponseEntity<Gson> response = restClient.post()
            .uri("http://localhost:11434/api/generate")
            .body(airequest)
            .retrieve()
            .toEntity(Gson.class);
        return new AIResponse(response.getBody());
    }
}
