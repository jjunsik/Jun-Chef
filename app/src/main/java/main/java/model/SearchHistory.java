package main.java.model;

import java.util.Date;

public class SearchHistory {
    private final String recipeName;
    private final Date createDateTime;

    public SearchHistory(String recipeName, Date createDateTime) {
        this.recipeName = recipeName;
        this.createDateTime = createDateTime;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public Date getCreateDateTime() {
        return createDateTime;
    }
}
