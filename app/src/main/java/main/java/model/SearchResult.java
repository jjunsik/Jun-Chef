package main.java.model;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SearchResult {
    private final String recipeName;
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

    // TODO: ADD HISTORY 개발
    public SearchHistory toSearchHistory() {
        return new SearchHistory(getRecipeName());
    }
}
