package main.java.util.parser;

import com.google.gson.Gson;

import main.java.model.SearchResult;
import main.java.util.parser.dto.GptResponseChoicesDto;
import main.java.util.parser.dto.GptResponseDto;

public class GptResponseParser implements ResultParser{
    @Override
    public SearchResult getSearchResultByResponse(String response) {
        // TODO: 파싱!
        StringBuilder ingredients = new StringBuilder("[재료]");
        StringBuilder cookingOrder = new StringBuilder("[만드는 방법]");

        Gson gson = new Gson();
        try {
        // 받은 response를 GptResponseDto 클래스 형식으로 파싱
            GptResponseDto responseDto = gson.fromJson(response, GptResponseDto.class);

            // responseDto, choiceDto, splitText 등의 객체가 null인지 확인
            if (responseDto != null && responseDto.getChoices() != null && !responseDto.getChoices().isEmpty()) {

                // responseDto에서 첫 번째 choice를 가져옴
                GptResponseChoicesDto choiceDto = responseDto.getChoices().get(0);

                if (choiceDto != null) {
                    // choiceDto에서 message를 가져옴
                    GptResponseMessageDto messageDto = choiceDto.getMessage();

                    if (messageDto != null) {
                        // messageDto에서 content 값을 가져옴
                        String msgContent = messageDto.getContent().trim();

                        // 정규식을 사용하여 [재료]와 [레시피]로 문자열을 분할
                        String regex = "\\[(재료|레시피)\\]";
                        String[] splitContent = msgContent.split(regex);

                if (splitText.length > 1) {
                    ingredients.append("\n").append(splitText[1].split("[레시피]")[0].replace("\n\n", "\n").trim());
                    cookingOrder.append("\n").append(splitText[1].split("[레시피]")[1].replace("\n\n", "\n").trim());
                }
            }

        } catch (Exception e) {
            // 기타 예외 처리
            e.printStackTrace();
        }

        return new SearchResult("음식 이름", ingredients.toString(), cookingOrder.toString());
    }
}
