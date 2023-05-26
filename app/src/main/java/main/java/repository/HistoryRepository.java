package main.java.repository;

import java.util.List;

import main.java.model.SearchHistory;

public interface HistoryRepository {
    int getSearchHistoryCount();
    List<SearchHistory> getSearchHistoriesByLimit(int limit);
    void addHistory(SearchHistory history);
    void addSearchHistoryCount();

    void removeHistory(int index);

    void subtractHistoryCount();
}
