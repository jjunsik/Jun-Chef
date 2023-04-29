package main.java.service;

import main.java.model.SearchResult;

public interface GPTRequest {
    SearchResult SearchWord(String word);
}
