package main.java.util.parser.dto;

import java.util.List;

public class GptResponseDto {
    List<GptResponseChoicesDto> choices;

    public List<GptResponseChoicesDto> getChoices() {
        return choices;
    }
}
