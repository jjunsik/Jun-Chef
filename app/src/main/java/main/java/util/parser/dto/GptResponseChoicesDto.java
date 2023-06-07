package main.java.util.parser.dto;

public class GptResponseChoicesDto {
    private final String text;

    public GptResponseChoicesDto(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
