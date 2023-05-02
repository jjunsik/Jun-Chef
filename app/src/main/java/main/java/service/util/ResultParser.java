package main.java.service.util;

import main.java.model.SearchResult;

public interface ResultParser {
    SearchResult getSearchResultByResponse(String response);
}