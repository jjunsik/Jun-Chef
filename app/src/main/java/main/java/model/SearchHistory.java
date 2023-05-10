package main.java.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SearchHistory {
    private final String recipeName;
    private final String createDateTime;

    public SearchHistory(String recipeName) {
        this.recipeName = recipeName;
        LocalDate time = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd");
        this.createDateTime = time.format(formatter);
    }
    public String getRecipeName() {
        return recipeName;
    }
    public String getCreateDateTime() {
        return createDateTime;
    }
}
