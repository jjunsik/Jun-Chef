package main.java.service.history;

import java.util.List;

import main.java.model.SearchHistory;

public interface HistoryService {
    List<SearchHistory> getSearchHistories(int limit);
    void addHistory(SearchHistory searchHistory);
}