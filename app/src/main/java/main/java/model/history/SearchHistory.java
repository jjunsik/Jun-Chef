package main.java.model.history;

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
}
