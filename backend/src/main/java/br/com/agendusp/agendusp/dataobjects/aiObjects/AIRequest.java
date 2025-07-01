package br.com.agendusp.agendusp.dataobjects.aiObjects;

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
    public AIRequest(AiRequestBuilder builder){
        this.model = builder.builderModel;
        this.prompt = builder.builderPrompt;
        this.stream = builder.builderStream;
    }

    public AIRequest(Gson gson){
        JsonObject obj = gson.toJsonTree(this).getAsJsonObject();
        this.model = obj.get("model").getAsString();
        this.prompt = obj.get("prompt").getAsString();
        this.stream = obj.get("stream").getAsString();
    }

    public static class AiRequestBuilder {
        private String builderModel;
        private String builderPrompt;
        private String builderStream;
        public AiRequestBuilder(String builderModel, String builderPrompt){
            this.builderModel = builderModel;
            this.builderPrompt = builderPrompt;
        }

        public AiRequestBuilder setModel(String model){
            builderModel = model;
            return this;
        }
        public AiRequestBuilder setPrompt(String prompt){
            builderPrompt = prompt;
            return this;
        }
        public AiRequestBuilder setStream(String stream){
            builderStream = stream;
            return this;
        }
        
        public AIRequest build(){
            return new AIRequest(this);
        }
    }
}
