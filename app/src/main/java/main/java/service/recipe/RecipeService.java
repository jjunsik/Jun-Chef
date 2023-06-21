package main.java.service.recipe;

import main.java.BackgroundThread;
import main.java.model.SearchResult;
import main.java.service.history.HistoryService;
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

    public SearchResult search(String word) {
        String response;

        try {
            // http 통신을 통해 response 확인
            BackgroundThread gptBack = new BackgroundThread(word);
            Thread gptThread = new Thread(gptBack);

            gptThread.start();

            try {
                gptThread.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            response = gptBack.getResponse();

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
    }

    protected abstract void addHistory(SearchResult searchResult);
}
