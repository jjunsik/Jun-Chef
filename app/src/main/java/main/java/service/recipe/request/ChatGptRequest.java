package main.java.service.recipe.request;

import java.util.List;

import main.java.util.http.request.CommonRequest;

public class ChatGptRequest extends CommonRequest {

    private static final String CHAT_GPT_KEY = "API_KEY";
    private static final String CHAT_GPT_URL = "https://api.openai.com/v1/chat/completions";
    private final String model = "gpt-3.5-turbo";
    private final List<String> messages;

    public ChatGptRequest(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public String toPostRequestString() {
        return "{\"model\":\"" + model + "\"" + ",\"messages\":" + messages + "}";
    }

    @Override
    public String toGetRequestString() {
        return "?serviceKey=" + getKey() +
                "&model=" + model +
                "&messages=" + messages;
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
