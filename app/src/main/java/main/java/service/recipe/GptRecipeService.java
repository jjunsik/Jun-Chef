package main.java.service.recipe;

import main.java.model.SearchHistory;
import main.java.model.SearchResult;
import main.java.service.util.HttpService;
import main.java.service.util.ResultParser;
import main.java.service.history.HistoryService;

public class GptRecipeService extends RecipeService {

    private static final String GPT_URL = "";
    public GptRecipeService(HttpService httpService, ResultParser resultParser, HistoryService historyService) {
        super(httpService, resultParser, historyService);
    }

    @Override
    protected void addHistory(SearchResult searchResult) {
        // searchResult 를 searchHistory 로 변경
        SearchHistory searchHistory = null;



        // searchHistory 를 저장소에 추가
        historyService.addHistory(searchHistory);
    }

    @Override
    protected String setURL() {
        // url 정의
        return GPT_URL;
    }
}