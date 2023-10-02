package main.java.util.error.junchef;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JunChefExceptionParser {
    public JunChefException getJunChefException(String response) {
        JunChefException junChefException = null;

        Gson gson = new Gson();

        try {
            junChefException = gson.fromJson(response, JunChefException.class);
        } catch (JsonSyntaxException j) {
            j.printStackTrace();
        }

        return junChefException;
    }
}
