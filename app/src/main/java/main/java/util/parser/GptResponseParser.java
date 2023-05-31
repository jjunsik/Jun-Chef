package main.java.util.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.java.model.SearchResult;

public class GptResponseParser implements ResultParser{
    @Override
    public SearchResult getSearchResultByResponse(String response) {
        // TODO: 파싱!

        try {
            JSONObject responseJSON = new JSONObject(response);
            JSONArray responseArray = responseJSON.getJSONArray("choices");

            JSONObject responseArrayJSON = responseArray.getJSONObject(0);
            String responseText = responseArrayJSON.getString("text").trim();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
