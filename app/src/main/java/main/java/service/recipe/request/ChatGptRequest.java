package main.java.service.recipe.request;

import main.java.util.http.request.CommonRequest;

public class ChatGptRequest extends CommonRequest {

    private static final String CHAT_GPT_KEY = "API_KEY";
    private static final String CHAT_GPT_URL = "https://api.openai.com/v1/completions";
    private final String model = "text-davinci-002";
    private final String prompt;
    private final Float temperature = 0.3f;
    private final Integer max_tokens = 100;

//    public ChatGptRequest(String model, String prompt, Float temperature, Integer max_tokens) {
//        this.model = model;
//        this.prompt = prompt;
//        this.temperature = temperature;
//        this.max_tokens = max_tokens;
//    }

    public ChatGptRequest(String question) {
        this.prompt = question;
    }

    @Override
    public String toRequestString() {
        return '{' +
                "\"model\":\"" + model + '\"' +
                ",\"prompt\":\"" + prompt + '\"' +
                ",\"temperature\":" + temperature +
                ",\"max_tokens\":" + max_tokens +
                '}';
    }

    @Override
    public String getKey() {
        return CHAT_GPT_KEY;
    }

    @Override
    public String getUrl() {
        return CHAT_GPT_URL;
    }
}
