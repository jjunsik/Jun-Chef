package main.java.util.parser;

import com.google.gson.Gson;

import main.java.model.SearchResult;

public class GptResponseParser implements ResultParser{
    @Override
    public SearchResult getSearchResultByResponse(String response) {
        // TODO: 파싱!
        StringBuilder ingredients = new StringBuilder("[재료]");
        StringBuilder cookingOrder = new StringBuilder("[만드는 방법]");

        Gson gson = new Gson();
        GptResponseDto responseDto = gson.fromJson(response, GptResponseDto.class);

            JSONObject responseArrayJSON = responseArray.getJSONObject(0);
            String responseText = responseArrayJSON.getString("text").trim();
            Log.d("TAG", "getSearchResultByResponse: " + responseText);

            String[] splitText = responseText.split("재료");

            ingredients += splitText[1].split("레시피")[0];
            cookingOrder += splitText[1].split("레시피")[1];

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return new SearchResult("음식 이름", ingredients.toString(), cookingOrder.toString());
    }
}
