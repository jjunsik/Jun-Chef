package main.java.util.parser;

import main.java.model.recipe.SearchResult;

public interface ResultParser {
    SearchResult getSearchResultByResponse(String response);
}