package main.java.util.parser.dto;

import java.util.List;

public class GptResponseDto {
    private final List<GptResponseChoicesDto> choices;

    public GptResponseDto(List<GptResponseChoicesDto> choices) {
        this.choices = choices;
    }

    public List<GptResponseChoicesDto> getChoices() {
        return choices;
    }
}
