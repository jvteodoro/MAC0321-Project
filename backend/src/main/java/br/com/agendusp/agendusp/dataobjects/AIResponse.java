package br.com.agendusp.agendusp.dataobjects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AIResponse {
    String informe;

    public AIResponse(Gson gson) {
        JsonObject obj = gson.toJsonTree(this).getAsJsonObject();
        this.informe = obj.get("informe").getAsString();
    }
}
