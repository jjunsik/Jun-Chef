package main.java.service.recipe.request.dto;

public class GptRequestMessageDto {
    String role = "user";
    String content;
    public static final String FIXED_MESSAGE = " 레시피를 다른 말하지 말고 [재료]와 [레시피]만 알려줘. ";

    public GptRequestMessageDto(String content) {
        this.content = content + FIXED_MESSAGE;
    }
}
