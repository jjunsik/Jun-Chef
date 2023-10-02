package main.java.service.junchef.recipe;

import android.util.Log;

import java.util.concurrent.CompletableFuture;

import main.java.model.recipe.SearchResult;
import main.java.util.error.junchef.JunChefException;
import main.java.util.error.junchef.JunChefExceptionParser;
import main.java.util.http.junchef.recipe.RecipeHttpService;
import main.java.util.http.junchef.recipe.request.GetRecipeRequestDto;
import main.java.util.parser.ResultParser;

public class JunChefRecipeService {
    private final RecipeHttpService recipeHttpService;
    private final ResultParser resultParser;
    private final JunChefExceptionParser exceptionParser = new JunChefExceptionParser();
    private JunChefException junChefException;
    private SearchResult searchResult;

    public JunChefRecipeService(RecipeHttpService recipeHttpService, ResultParser resultParser) {
        this.recipeHttpService = recipeHttpService;
        this.resultParser = resultParser;
    }

    public CompletableFuture<SearchResult> search(Long memberId, String word) {
        return CompletableFuture.supplyAsync(() -> {
            String response;
            GetRecipeRequestDto getRecipeRequestDto = new GetRecipeRequestDto(memberId, word);

            try {
                response = recipeHttpService.getRecipeAndAddHistory(getRecipeRequestDto);

                junChefException = exceptionParser.getJunChefException(response);

                if (junChefException.getCode() != 0) {
                    Log.d("junchef", "레시피 검색 및 조회 예외 받음" + junChefException.getCode() + junChefException.getTitle() + junChefException.getMessage());
                    throw new JunChefException(junChefException.getCode(), junChefException.getTitle(), junChefException.getMessage());
                }

                searchResult = resultParser.getSearchResultByResponse(response);
                searchResult.setRecipeName(word);

                Log.d("junchef", "검색 되었다." + searchResult.toString());

                return searchResult;

            } catch (JunChefException j) {
                Log.d("junchef", "레시피 검색 및 조회 에러" + j.getCode() + j.getTitle() + j.getMessage());
                throw j;
            }
        });
    }
}
