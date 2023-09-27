package main.java.util.parser.junchef.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import main.java.model.recipe.SearchResult;
import main.java.util.parser.ResultParser;
import main.java.util.parser.junchef.recipe.dto.GetRecipeResponseDto;

public class RecipeResponseParser implements ResultParser {
    @Override
    public SearchResult getSearchResultByResponse(String response) {
        String recipeName;
        StringBuilder ingredients = new StringBuilder();
        StringBuilder cookingOrder = new StringBuilder();

        Gson gson = new Gson();

        try {
            GetRecipeResponseDto getRecipeResponseDto = gson.fromJson(response, GetRecipeResponseDto.class);

            recipeName = getRecipeResponseDto.getRecipeName();
            ingredients.append("\n").append(getRecipeResponseDto.getIngredients().trim());
            cookingOrder.append("\n").append(getRecipeResponseDto.getCookingOrder().trim());

        } catch (JsonSyntaxException j) {
            j.printStackTrace();
            throw j;
        }

        return new SearchResult(recipeName, ingredients.toString(), cookingOrder.toString());
    }
}
