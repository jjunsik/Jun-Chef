package main.java.util.parser.junchef.history;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.model.history.SearchHistory;
import main.java.util.parser.junchef.history.dto.HistoryResponseDto;

public class HistoryResponseParser {
    public List<SearchHistory> getSearchHistoriesByResponse(String response) {
        List<SearchHistory> histories;
        Log.d("최근 검색 기록 응답", "getSearchHistoriesByResponse: " + response);

        Gson gson = new Gson();

        try {
            // List로 파싱을 시도하면 JSON 배열을 객체로 변환하려고 시도했다는 예외가 발생한다.
            // 따라서 JSON 배열을 List로 직접 파싱하려면 배열의 각 요소를 개별적으로 파싱하고 그 결과를 List에 추가해야 한다.
            SearchHistory[] searchHistories = gson.fromJson(response, SearchHistory[].class);
            histories = new ArrayList<>(Arrays.asList(searchHistories));

        } catch (JsonSyntaxException j) {
            j.printStackTrace();
            Log.d("빈 리스트", "Empty History");

            return new ArrayList<>();
        }

        return histories;
    }


    public Long getHistoryIdByResponse(String response) {
        Long historyId = null;

        Gson gson = new Gson();

        Log.d("검색어 ID 값 조회 요청 응답", response);
        try {
            HistoryResponseDto historyResponseDto = gson.fromJson(response, HistoryResponseDto.class);

            Log.d("매핑할 객체", "" + historyResponseDto);
            historyId = historyResponseDto.getHistoryId();

            Log.d("파싱한 값", "" + historyId);

        } catch (JsonSyntaxException j) {
            j.printStackTrace();
        }

        Log.d("리턴하기 직전 아이디 값", "" + historyId);
        return historyId;
    }
}
