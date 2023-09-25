package main.java.model.history;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SearchHistory {
    private final String recipeName;

    private final String createDateTime;

    public SearchHistory(String recipeName, String createDateTime) {
        this.recipeName = recipeName;
        this.createDateTime = createDateTime;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

//    private final String recipeName;
//    private final String createDateTime;
//    private static final String CREATED_DATE_TIME_FORMAT = "MM.dd";
//
//    public SearchHistory(String recipeName) {
//        this.recipeName = recipeName;
//        LocalDate time = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CREATED_DATE_TIME_FORMAT);
//        this.createDateTime = time.format(formatter);
//    }
//    public String getRecipeName() {
//        return recipeName;
//    }
//    public String getCreateDateTime() {
//        return createDateTime;
//    }
}
