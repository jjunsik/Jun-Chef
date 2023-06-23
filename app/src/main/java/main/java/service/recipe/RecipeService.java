package main.java.service.recipe;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import main.java.model.SearchResult;
import main.java.service.history.HistoryService;
import main.java.service.recipe.request.ChatGptRequest;
import main.java.service.recipe.request.dto.GptRequestMessageDto;
import main.java.util.http.HttpService;
import main.java.util.parser.ResultParser;

public abstract class RecipeService {

    protected final HttpService httpService;
    protected final ResultParser resultParser;
    protected final HistoryService historyService;

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


            try {
                response = new HttpService().post(new ChatGptRequest(message));

            } catch (Exception e) {
                // 애러 로직
                e.printStackTrace();
                return null;
            }

            // response 를 파싱하여 searchResult 에 저장
            SearchResult searchResult = resultParser.getSearchResultByResponse(response);

            // 검색 결과가 파싱이 불가능한 형태일 경우
            if (searchResult == null)
                return null;

            searchResult.setRecipeName(word);

            // 검색 결과룰 history 에 추가
            addHistory(searchResult);

            return searchResult;
        });
    }

    protected abstract void addHistory(SearchResult searchResult);
}
