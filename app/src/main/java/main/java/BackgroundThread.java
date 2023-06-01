package main.java;

import main.java.service.recipe.request.ChatGptRequest;
import main.java.util.http.HttpService;

public class BackgroundThread implements Runnable {
    String response;
    String word;

    public BackgroundThread(String word) {
        this.word = word;
    }

    @Override
    public void run() {
        //http
        response = new HttpService().post(new ChatGptRequest(word + " 레시피"));
    }

    public String getResponse() {
        return response;
    }
}
