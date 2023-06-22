package main.java.repository;


import static main.java.repository.constant.RepositoryConstant.MAX_COUNT;
import static main.java.repository.constant.RepositoryConstant.SUBSTRING_START_INDEX;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import main.java.model.SearchHistory;
import main.java.service.history.HistoryService;
import main.java.service.history.HistoryServiceImpl;

public class LocalHistoryRepository implements HistoryRepository {
    private final Context context;

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
        HistoryService historyService = new HistoryServiceImpl(new LocalHistoryRepository(context));

        SharedPreferences historyRepository = context.getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor historyEditor = historyRepository.edit();

        Gson gson = new Gson();
        String historyObject = gson.toJson(history); // SearchHistory 객체를 JSON 형태로 변환

        // 중복 확인
        String check;
        String checkSplit;
        String historyReplace =history.getRecipeName().replace(" ", "");

        for(int i = 1; i < getSearchHistoryCount() + 1; i ++){
            check = historyRepository.getString(String.valueOf(i), "");
            checkSplit = check.substring(SUBSTRING_START_INDEX, check.indexOf("\"}")).replace(" ", "");
            if(checkSplit.equals(historyReplace)){
                historyService.removeHistory(i);
                break;
            }
        }

        if(getSearchHistoryCount() == MAX_COUNT) {
            for(int i = MAX_COUNT - 1; i > 0; i--) {
                String nextValue = historyRepository.getString(String.valueOf(i), "");
                historyEditor.putString(String.valueOf(i+1), nextValue).apply();
            }
        }

        if(getSearchHistoryCount() >= 1 && getSearchHistoryCount() < MAX_COUNT){
            for(int i = getSearchHistoryCount(); i > 0; i--){
                String nextValue = historyRepository.getString(String.valueOf(i), "");
                historyEditor.putString(String.valueOf(i+1), nextValue).apply();
            }
        }

        historyEditor.putString("1", historyObject).apply(); // JSON 을 SharedPreferences 에 저장
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

    @Override
    public void removeHistory(int index) {

        SharedPreferences historyRepository = context.getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor historyEditor = historyRepository.edit();

        if (getSearchHistoryCount() < MAX_COUNT && getSearchHistoryCount() > 1){
            for(int i = index; i <= getSearchHistoryCount(); i++){
                String nextValue = historyRepository.getString(String.valueOf(i+1), "");
                historyEditor.putString(String.valueOf(i), nextValue).apply();
            }
        } else if (getSearchHistoryCount() == MAX_COUNT && index < getSearchHistoryCount()) {
            for(int i = index; i < getSearchHistoryCount(); i++){
                String nextValue = historyRepository.getString(String.valueOf(i+1), "");
                historyEditor.putString(String.valueOf(i), nextValue).apply();
            }
        }

        historyEditor.remove(String.valueOf(getSearchHistoryCount())).apply();
    }

    @Override
    public void subtractHistoryCount(){
        SharedPreferences searchHistoryCountRepository = context.getSharedPreferences("searchHistoryCount", Context.MODE_PRIVATE);
        SharedPreferences.Editor searchHistoryCountEditor = searchHistoryCountRepository.edit();

        int cnt = searchHistoryCountRepository.getInt("search_history_total_count", 0);

        searchHistoryCountEditor.putInt("search_history_total_count", cnt-1).apply();
    }
}
