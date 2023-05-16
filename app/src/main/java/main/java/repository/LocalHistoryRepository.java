package main.java.repository;

import java.util.List;

import main.java.model.SearchHistory;

public class LocalHistoryRepository implements HistoryRepository {
    @Override
    public int getSearchHistoryCount() {
        // key: search_history_total_count
        return 0;
    }

    @Override
    public List<SearchHistory> getSearchHistoriesByLimit(int limit) {
        // key: idx (count 기준), string to class
        return null;
    }

    @Override
    public boolean addHistory(SearchHistory history) {
        // history 저장하는 로직
        // key: idx (count 기준) , value: class 자체 | class to string
        // 2개 조회 ㄱㄴ? 1, 2 조회
        return false;
    }

    @Override
    public void addSearchHistoryCount() {
        // key: search_history_total_count 녀석 + 1
    }

}
