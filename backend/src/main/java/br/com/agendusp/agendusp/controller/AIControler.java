package br.com.agendusp.agendusp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController //para poder fazer requisicoes http 
public class AIControler {

    @Autowired
    RestClient restClient; //coisa do spring para gerar requisicoes http com java de modo facil

    public String gerarInforme(/*requisicao http*/){
        ResponseEntity<String> response = restClient.post()
            .uri("http://localhost:11434/api/generate")
            .body(/*alguma coisa ou a requisicao*/);



        return response.getBody();
    }
}
