package main.java.util.parser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import main.java.model.SearchResult;
import main.java.util.parser.dto.GptResponseChoicesDto;
import main.java.util.parser.dto.GptResponseDto;
import main.java.util.parser.dto.GptResponseMessageDto;

public class GptResponseParser implements ResultParser{
    @Override
    public SearchResult getSearchResultByResponse(String response) {
        // TODO: 파싱!
        StringBuilder ingredients = new StringBuilder(START_INGREDIENT_TEXT);
        StringBuilder cookingOrder = new StringBuilder(START_COOKING_ORDER_TEXT);

        Gson gson = new Gson();
        try {
            // 받은 response 를 GptResponseDto 클래스 형식으로 파싱
            GptResponseDto responseDto = gson.fromJson(response, GptResponseDto.class);

            // responseDto, choiceDto, splitText 등의 객체가 null 인지 확인
            if (responseDto == null ||
                    responseDto.getChoices() == null ||
                    responseDto.getChoices().isEmpty())
                return null;

            // responseDto에서 첫 번째 choice를 가져옴
            GptResponseChoicesDto choiceDto = responseDto.getChoices().get(0);

            if (choiceDto == null)
                return null;

            // choiceDto에서 message를 가져옴
            GptResponseMessageDto messageDto = choiceDto.getMessage();

            if (messageDto == null)
                return null;

            // messageDto에서 content 값을 가져옴
            String msgContent = messageDto.getContent().trim();

            // 정규식을 사용하여 [재료]와 [레시피]로 문자열을 분할
            String[] splitContent = msgContent.split(INGREDIENT_RECIPE_REGEX);

            // 분할된 문자열의 길이가 3인 경우(0번째 -> \n, 1번째 -> [재료], 2번째 -> [레시피]), [재료]와 [레시피]의 값을 추출
            if (splitContent.length == 3) {
                ingredients.append("\n").append(splitContent[1].replace("\n\n", "\n").trim());
                cookingOrder.append("\n").append(splitContent[2].replace("\n\n", "\n").trim());
            }
        } catch (JsonSyntaxException e) {
            // JSON 파싱 오류 처리
            // 예외를 기록하거나 오류 메시지를 출력
            e.printStackTrace();

            return null;
        }

        return new SearchResult(RECIPE_NAME_TITLE, ingredients.toString(), cookingOrder.toString());
    }
}
