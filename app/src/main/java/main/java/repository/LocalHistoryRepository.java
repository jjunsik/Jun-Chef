package main.java.repository;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import main.java.model.SearchHistory;

public class LocalHistoryRepository implements HistoryRepository {
    int search_history_total_count = 0;
    private Context context;
    private final static int MAX_COUNT = 5;

    @Override
    public int getSearchHistoryCount() {
        return context.getSharedPreferences("searchHistoryCount", Context.MODE_PRIVATE).getInt("search_history_total_count",0);
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
