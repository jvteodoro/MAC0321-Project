package br.com.agendusp.agendusp.dataobjects.aiObjects;

public class AIRequest { // contem as informacoes necessarias para que o AIControler gere a requisicao
    public String model;
    public String prompt;
    public boolean stream;

    public AIRequest(String model, String prompt, boolean stream) {
        this.model = model;
        this.prompt = prompt;
        this.stream = stream;
    }

    public AIRequest(AiRequestBuilder builder) {
        this.model = builder.builderModel;
        this.prompt = builder.builderPrompt;
        this.stream = builder.builderStream;
    }

    public static class AiRequestBuilder {
        private String builderModel;
        private String builderPrompt;
        private boolean builderStream;

        public AiRequestBuilder(String builderModel, String builderPrompt, boolean builderStream) {
            this.builderModel = builderModel;
            this.builderPrompt = builderPrompt;
            this.builderStream = builderStream;
        }

        public AiRequestBuilder setModel(String model) {
            builderModel = model;
            return this;
        }

        public AiRequestBuilder setPrompt(String prompt) {
            builderPrompt = prompt;
            return this;
        }

        public AiRequestBuilder setStream(boolean stream) {
            builderStream = stream;
            return this;
        }

        public AIRequest build() {
            return new AIRequest(this);
        }
    }
}
