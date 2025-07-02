package br.com.agendusp.agendusp.dataobjects.aiObjects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AIResponse { // retornada pelo AIControler, contem o informe, ou seja, o que a IA retornou
    String informe;

    public AIResponse(Gson gson) {
        JsonObject obj = gson.toJsonTree(this).getAsJsonObject();
        this.informe = obj.get("informe").getAsString();
    }
}
