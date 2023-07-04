package main.java.util.parser;

import main.java.model.SearchResult;
import main.java.util.error.exception.SearchErrorException;

public interface ResultParser {
    SearchResult getSearchResultByResponse(String response) throws SearchErrorException;
}