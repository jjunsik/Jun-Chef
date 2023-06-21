package main.java.service.recipe.request.dto;

public class GptRequestMessageDto {
    String role = "user";
    String content;
    public static final String FIXED_MESSAGE = " [재료]와 [레시피]";

    public GptRequestMessageDto(String content) {
        this.content = content + FIXED_MESSAGE;
    }
}
