package main.java.repository;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import main.java.model.SearchHistory;

public class LocalHistoryRepository implements HistoryRepository {
    private final Context context;
    private final static int MAX_COUNT = 5;

    public LocalHistoryRepository(Context context) {
        this.context = context;
    }
    @Override
    public int getSearchHistoryCount() {
        return context.getSharedPreferences("searchHistoryCount", Context.MODE_PRIVATE)
                .getInt("search_history_total_count",0);
    }

    @Override
    public List<SearchHistory> getSearchHistoriesByLimit(int limit) {
        // key: idx (count 기준), string to class
        List<SearchHistory> histories = new LinkedList<>();

        SharedPreferences historyRepository = context.getSharedPreferences("history", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        for (int i = 1; i <= limit; i++) {
            String json = historyRepository.getString(String.valueOf(i), "없음");
            SearchHistory searchHistoryObject = gson.fromJson(json, SearchHistory.class); // JSON 을 SearchHistory 객체로 변환
            histories.add(searchHistoryObject);
        }
        return histories;
    }

    @Override
    public void addHistory(SearchHistory history) {
        SharedPreferences historyRepository = context.getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor historyEditor = historyRepository.edit();

        Gson gson = new Gson();
        String jsonObject = gson.toJson(history); // SearchHistory 객체를 JSON 형태로 변환
        // 새로 추가 되면 1(최신), limit(오래된)
        // 꽉 차면 key 는 그대로고 value 가 한 칸 뒤로 밀려야 하고 첫번째 값에는 최신 검색어 ㅇㅇ

        if(getSearchHistoryCount() == MAX_COUNT) {
            for(int i = 1; i < MAX_COUNT; i++) {
                nextValue = historyRepository.getString(String.valueOf(i), "");
                historyEditor.putString(String.valueOf(i+1), nextValue).apply();
            }
        }

        historyEditor.putString("1", jsonObject).apply(); // JSON 을 SharedPreferences 에 저장
    }

    @Override
    public void addSearchHistoryCount() {
        SharedPreferences searchHistoryCountRepository = context.getSharedPreferences("searchHistoryCount", Context.MODE_PRIVATE);
        SharedPreferences.Editor searchHistoryCountEditor = searchHistoryCountRepository.edit();

        int cnt = searchHistoryCountRepository.getInt("search_history_total_count", 0);

        if (cnt < MAX_COUNT){
            searchHistoryCountEditor.putInt("search_history_total_count", cnt + 1).apply();
        }
    }
}
