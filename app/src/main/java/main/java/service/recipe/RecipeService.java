package main.java.service.recipe;

import static main.java.util.error.constant.ErrorConstant.NUMBER_OF_SEARCHES;
import static main.java.util.error.constant.ErrorConstant.getAPIErrorMessage;
import static main.java.util.error.constant.ErrorConstant.getNetworkErrorMessage;
import static main.java.util.error.constant.ErrorConstant.getSearchErrorMessage;
import static main.java.util.error.constant.ErrorConstant.getSearchWordErrorMessage;

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

                if (searchResult == null) {
                    retryCount++;

                    sleep(1000);
                    continue;
                }

                searchResult.setRecipeName(word);
                addHistory(searchResult);

                return searchResult;

            }

            throw new RuntimeException(getSearchErrorMessage(), new SearchErrorException());
        });
    }

    private static String getResponseByMessage(List<String> message) {
        String response;

        try {
            response = new HttpService().post(new ChatGptRequest(message));
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                throw new RuntimeException(getNetworkErrorMessage(), e);
            }

            throw new RuntimeException(getAPIErrorMessage(), e);
        } catch (JsonSyntaxException j) {
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
