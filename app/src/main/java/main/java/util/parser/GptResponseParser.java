package main.java.util.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.java.model.SearchResult;

public class GptResponseParser implements ResultParser{
    @Override
    public SearchResult getSearchResultByResponse(String response) {
        // TODO: 파싱!
        String ingredients = "재료";
        String cookingOrder = "만드는 방법";

        try {
            JSONObject responseJSON = new JSONObject(response);
            JSONArray responseArray = responseJSON.getJSONArray("choices");

            JSONObject responseArrayJSON = responseArray.getJSONObject(0);
            String responseText = responseArrayJSON.getString("text").trim();
            Log.d("TAG", "getSearchResultByResponse: " + responseText);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return new SearchResult("음식 이름", ingredients, cookingOrder);
    }
}
