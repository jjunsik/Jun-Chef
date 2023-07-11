package main.java.util.parser;

import static main.java.util.parser.constant.ParserConstant.INGREDIENT_RECIPE_REGEX;
import static main.java.util.parser.constant.ParserConstant.RECIPE_NAME_TITLE;
import static main.java.util.parser.constant.ParserConstant.START_COOKING_ORDER_TEXT;
import static main.java.util.parser.constant.ParserConstant.START_INGREDIENT_TEXT;
import static main.java.util.parser.constant.ParserConstant.parserErrorCondition;

import com.google.gson.Gson;

import main.java.model.SearchResult;
import main.java.util.parser.dto.GptResponseChoicesDto;
import main.java.util.parser.dto.GptResponseDto;
import main.java.util.parser.dto.GptResponseMessageDto;

public class GptResponseParser implements ResultParser{
    @Override
    public SearchResult getSearchResultByResponse(String response) {
        StringBuilder ingredients = new StringBuilder(START_INGREDIENT_TEXT);
        StringBuilder cookingOrder = new StringBuilder(START_COOKING_ORDER_TEXT);

        Gson gson = new Gson();

        GptResponseDto responseDto = gson.fromJson(response, GptResponseDto.class);

        if (responseDto == null ||
                responseDto.getChoices() == null ||
                responseDto.getChoices().isEmpty())
            return null;

        GptResponseChoicesDto choiceDto = responseDto.getChoices().get(0);
        if (choiceDto == null)
            return null;

        GptResponseMessageDto messageDto = choiceDto.getMessage();
        if (messageDto == null)
            return null;

        String msgContent = messageDto.getContent().trim();

        String[] splitContent = msgContent.split(INGREDIENT_RECIPE_REGEX);

        if(splitContent.length != 3)
            return null;

        ingredients.append("\n").append(splitContent[1].replace("\n\n", "\n").trim());
        cookingOrder.append("\n").append(splitContent[2].replace("\n\n", "\n").trim());

        if (parserErrorCondition(ingredients, cookingOrder))
            return null;

        return new SearchResult(RECIPE_NAME_TITLE, ingredients.toString(), cookingOrder.toString());
    }
}
