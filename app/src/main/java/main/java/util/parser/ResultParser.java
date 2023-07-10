package main.java.util.parser;

import main.java.model.SearchResult;

public interface ResultParser {
    SearchResult getSearchResultByResponse(String response);
}