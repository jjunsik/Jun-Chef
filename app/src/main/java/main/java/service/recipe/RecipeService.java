package main.java.service.recipe;

import static main.java.util.error.constant.ErrorConstant.NUMBER_OF_SEARCHES;
import static main.java.util.error.constant.ErrorConstant.getAPIErrorMessage;
import static main.java.util.error.constant.ErrorConstant.getNetworkErrorMessage;
import static main.java.util.error.constant.ErrorConstant.getSearchErrorMessage;
import static main.java.util.error.constant.ErrorConstant.getSearchWordErrorMessage;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import main.java.model.SearchResult;
import main.java.service.history.HistoryService;
import main.java.service.recipe.request.ChatGptRequest;
import main.java.service.recipe.request.dto.GptRequestMessageDto;
import main.java.util.error.exception.SearchErrorException;
import main.java.util.http.HttpService;
import main.java.util.parser.ResultParser;

public abstract class RecipeService {

    protected final HttpService httpService;
    protected final ResultParser resultParser;
    protected final HistoryService historyService;
    SearchResult searchResult;

    public RecipeService(HttpService httpService, ResultParser resultParser, HistoryService historyService) {
        this.httpService = httpService;
        this.resultParser = resultParser;
        this.historyService = historyService;
    }

    // CompletableFuture는 자체적으로 스레드 풀을 사용하여 비동기 작업을 처리하므로 별도의 스레드 생성 및 관리가 필요하지 않음.
    public CompletableFuture<SearchResult> search(String word) {
        return CompletableFuture.supplyAsync(() -> {
            String response;

            List<String> message = new ArrayList<>();
            GptRequestMessageDto requestMessage = new GptRequestMessageDto(word);

            Gson gson = new Gson();
            message.add(gson.toJson(requestMessage));

            int retryCount = 0;

            while (retryCount < NUMBER_OF_SEARCHES) {
                if (!word.matches("[가-힣 ]*"))
                    throw new RuntimeException(getSearchWordErrorMessage(), new SearchErrorException());

                response = getResponseByMessage(message);

                searchResult = resultParser.getSearchResultByResponse(response);

                // 검색이 성공한 경우 결과를 반환
                if (searchResult == null) {
                    Log.d("TAG", "재검색 해서 실패");
                    retryCount++;

                    // 이 코드가 실행되는 스레드에 대한 제어
                    sleep(1000);
                    continue;
                }

                searchResult.setRecipeName(word);
                addHistory(searchResult);

                Log.d("TAG", "검색 성공");
                return searchResult;

            }

            // 검색 재시도 후에도 실패한 경우 예외를 던짐
            Log.d("TAG", "몇 번의 시도 끝에 검색 오류 전달");
            throw new RuntimeException(getSearchErrorMessage(), new SearchErrorException());
        });
    }

    private static String getResponseByMessage(List<String> message) {
        String response;
        try {
            response = new HttpService().post(new ChatGptRequest(message));

            // 네트워크 연결 및 API 통신 예외 처리
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                Log.d("TAG", "네트워크 연결 오류임.");
                throw new RuntimeException(getNetworkErrorMessage(), e);
            }

            // 네트워크 연결 에러가 아닌 다른 네트워크 문제일 때
            Log.d("TAG", "네트워크 연결이 아닌 네트워크 통신 오류임.");
            throw new RuntimeException(getAPIErrorMessage(), e);

            // response 데이터를 자바 객체(GptResponseDto)로 변환 불가일 때
            // 파싱 불가(검색 불가)가 아님.
        } catch (JsonSyntaxException j) {
            Log.d("TAG", "객체 변환 불가 오류임.\n");
            throw new RuntimeException(getAPIErrorMessage(), j);
        }
        return response;
    }

    private static void sleep (int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void addHistory(SearchResult searchResult);
}
