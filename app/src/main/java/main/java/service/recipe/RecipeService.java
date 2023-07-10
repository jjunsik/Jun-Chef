package main.java.service.recipe;

import static main.java.util.error.constant.ErrorConstant.getAPIErrorMessage;
import static main.java.util.error.constant.ErrorConstant.getNetworkErrorMessage;
import static main.java.util.error.constant.ErrorConstant.getSearchErrorMessage;

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

                        response = new HttpService().post(new ChatGptRequest(message));

                        searchResult = resultParser.getSearchResultByResponse(response);

                        // 검색이 성공한 경우 결과를 반환
                        if (searchResult != null) {
                            searchResult.setRecipeName(word);
                            addHistory(searchResult);
                            return searchResult;
                        }
                        retryCount++;

                        // 네트워크 연결 및 API 통신 예외 처리
                    } catch (IOException e) {
                        if (e instanceof UnknownHostException) {
                            throw new RuntimeException(getNetworkErrorMessage(), e);
                        }

                        // 네트워크 연결 에러가 아닌 다른 네트워크 문제일 때
                        throw new RuntimeException(getAPIErrorMessage(), e);

                        // response 데이터를 자바 객체(GptResponseDto)로 변환 불가일 때
                        // 파싱 불가(검색 불가)가 아님.
                    } catch (JsonSyntaxException j) {
                        throw new RuntimeException(getAPIErrorMessage(), j);
                    } catch (SearchErrorException s) {
                        throw new RuntimeException(getSearchErrorMessage(), s);
                    }
                }
            }

            // 검색이 3번 재시도 후에도 실패한 경우 예외를 던짐
            throw new RuntimeException(getSearchErrorMessage(), new SearchErrorException());
        });
    }

    protected abstract void addHistory(SearchResult searchResult);
}
