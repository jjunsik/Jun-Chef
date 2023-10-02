package main.java.service.junchef.history;

import android.util.Log;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import main.java.model.history.SearchHistory;
import main.java.util.error.junchef.JunChefException;
import main.java.util.error.junchef.JunChefExceptionParser;
import main.java.util.http.junchef.history.HistoryHttpService;
import main.java.util.http.junchef.history.request.DeleteHistoryRequestDto;
import main.java.util.http.junchef.history.request.GetHistoryIdRequestDto;
import main.java.util.http.junchef.history.request.GetMemberHistoriesRequestDto;
import main.java.util.parser.junchef.history.HistoryResponseParser;

public class JunChefHistoryService {
    private final HistoryHttpService historyHttpService;
    private final HistoryResponseParser historyResponseParser;
    private final JunChefExceptionParser junChefExceptionParser = new JunChefExceptionParser();
    private JunChefException junChefException;

    public JunChefHistoryService(HistoryHttpService historyHttpService, HistoryResponseParser historyResponseParser) {
        this.historyHttpService = historyHttpService;
        this.historyResponseParser = historyResponseParser;
    }

    public CompletableFuture<List<SearchHistory>> getMemberHistories(Long memberId) {
        return CompletableFuture.supplyAsync(() -> {
            GetMemberHistoriesRequestDto getMemberHistoriesRequestDto = new GetMemberHistoriesRequestDto(memberId);
            String response = historyHttpService.getHistories(getMemberHistoriesRequestDto);

            return historyResponseParser.getSearchHistoriesByResponse(response);
        });
    }
    public CompletableFuture<Long> deleteHistory(Long historyId) {
        return CompletableFuture.supplyAsync(() -> {
            DeleteHistoryRequestDto deleteHistoryRequestDto = new DeleteHistoryRequestDto(historyId);

            Log.d("junchef", "삭제 요청 " + deleteHistoryRequestDto);
            Log.d("junchef", "삭제 요청 검색어 ID 값: " + historyId);

            String response = historyHttpService.deleteHistory(deleteHistoryRequestDto);

            junChefException = junChefExceptionParser.getJunChefException(response);

            Log.d("junchef", "삭제할 검색어 ID 값 파싱 전" + response);

            return historyResponseParser.getHistoryIdByResponse(response);
        });
    }

    public CompletableFuture<Long> getHistoryId(Long memberId, String recipeName) {
        return CompletableFuture.supplyAsync(() -> {
            GetHistoryIdRequestDto getHistoryIdRequestDto = new GetHistoryIdRequestDto(memberId, recipeName);

            try {
                String response = historyHttpService.getHistoryIdByMemberIdAndRecipeName(getHistoryIdRequestDto);

                junChefException = junChefExceptionParser.getJunChefException(response);

                if (junChefException.getCode() != 0) {
                    Log.d("junchef", "최근 검색어 id 조회 예외 받음" + junChefException.getCode() + junChefException.getTitle() + junChefException.getMessage());
                    throw new JunChefException(junChefException.getCode(), junChefException.getTitle(), junChefException.getMessage());
                }

                return historyResponseParser.getHistoryIdByResponse(response);

            } catch (JunChefException j) {
                Log.d("junchef", "검색어 id 값 조회 에러" + j.getCode() + j.getTitle() + j.getMessage());
                throw j;
            }
        });
    }
}
