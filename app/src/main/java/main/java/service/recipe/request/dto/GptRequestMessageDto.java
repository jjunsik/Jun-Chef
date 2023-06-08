package main.java.service.recipe.request.dto;

public class GptRequestMessageDto {
    String role = "user";
    String content;

    public GptRequestMessageDto(String content) {
        this.content = content + " [재료]와 [레시피]";
    }
}
