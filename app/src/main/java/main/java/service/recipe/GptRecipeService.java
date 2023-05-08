package main.java.service.recipe;

import main.java.model.SearchHistory;
import main.java.model.SearchResult;
import main.java.util.http.HttpService;
import main.java.util.parser.ResultParser;
import main.java.service.history.HistoryService;

public class GptRecipeService extends RecipeService {

    public GptRecipeService(HttpService httpService, ResultParser resultParser, HistoryService historyService) {
        super(httpService, resultParser, historyService);
    }

    @Override
    protected void addHistory(SearchResult searchResult) {
        // searchResult 를 searchHistory 로 변경
        SearchHistory searchHistory = searchResult.toSearchHistory();

        // searchHistory 를 저장소에 추가
        historyService.addHistory(searchHistory);
    }
}