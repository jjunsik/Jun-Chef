package main.java.model;

public class SearchResult {
    private String recipeName;
    private final String[] ingredients;
    private final String[] cookingOrder;

    public SearchResult(String recipeName, String[] ingredients, String[] cookingOrder) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.cookingOrder = cookingOrder;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public String[] getCookingOrder() {
        return cookingOrder;
    }

    public void setRecipeName(String recipeName){
        this.recipeName = recipeName;
    }

    // TODO: ADD HISTORY 개발
    public SearchHistory toSearchHistory() {
        return new SearchHistory(getRecipeName());
    }
}
