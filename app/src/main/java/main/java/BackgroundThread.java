package main.java;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import main.java.service.recipe.request.ChatGptRequest;
import main.java.service.recipe.request.dto.GptRequestMessageDto;
import main.java.util.http.HttpService;

public class BackgroundThread implements Runnable {
    String response;
    String word;

    public BackgroundThread(String word) {
        this.word = word;
    }

    @Override
    public void run() {

        List<String> message = new ArrayList<>();
        GptRequestMessageDto requestMessage = new GptRequestMessageDto(word);

        Gson gson = new Gson();
        message.add(gson.toJson(requestMessage));

        //http
        response = new HttpService().post(new ChatGptRequest(word + " 레시피"));
    }

    public String getResponse() {
        return response;
    }
}
