package br.com.agendusp.agendusp.dataobjects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AIRequest { //contem as informações necessarias para que o AIControler gere a requisição http
    String model;
    String prompt;
    String stream;

    public AIRequest(String model, String prompt, String stream) {
        this.model = model;
        this.prompt = prompt;
        this.stream = stream;
    }

    public AIRequest(Gson gson){
        JsonObject obj = gson.toJsonTree(this).getAsJsonObject();
        this.model = obj.get("model").getAsString();
        this.prompt = obj.get("prompt").getAsString();
        this.stream = obj.get("stream").getAsString();
    }
}
