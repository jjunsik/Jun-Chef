package main.java.service.recipe;

import main.java.model.SearchHistory;
import main.java.model.SearchResult;
import main.java.service.history.HistoryService;
import main.java.util.http.HttpService;
import main.java.util.parser.ResultParser;

public class GptRecipeService extends RecipeService {

    public GptRecipeService(HttpService httpService, ResultParser resultParser, HistoryService historyService) {
        super(httpService, resultParser, historyService);
    }

    @Override
    protected void addHistory(SearchResult searchResult) {
        SearchHistory searchHistory = searchResult.toSearchHistory();
        historyService.addHistory(searchHistory);
    }
}