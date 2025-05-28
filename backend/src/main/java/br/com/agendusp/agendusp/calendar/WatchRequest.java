package br.com.agendusp.agendusp.calendar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class WatchRequest {
    String id;
    String token;
    String type;
    String address;
    String params;
    // Corpo da Solicitação de Watch
    //{
    //    "id": string,
    //    "token": string,
    //    "type": string,
    //    "address": string,
    //    "params": {
    //      "ttl": string
    //    }
    //  }

    public WatchRequest(String id, String token, String type, String address, String params) {
        this.id = id;
        this.token = token;
        this.type = type;
        this.address = address;
        this.params = params;
    }

    public WatchRequest(Gson gson) {
        JsonObject obj = gson.toJsonTree(this).getAsJsonObject();
        this.id = obj.get("id").getAsString();
        this.token = obj.get("token").getAsString();
        this.type = obj.get("type").getAsString();
        this.address = obj.get("address").getAsString();
        this.params = obj.get("params").getAsString();
    }
   

}
