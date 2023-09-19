package main.java.controller;

import static main.java.model.constant.ResultConstant.COOKING_ORDER;
import static main.java.model.constant.ResultConstant.INGREDIENTS;
import static main.java.model.constant.ResultConstant.RECIPE_NAME;
import static main.java.util.error.constant.ErrorConstant.getErrorFromMessage;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import main.java.R;
import main.java.adapter.HistoryRecyclerViewAdapter;
import main.java.controller.backpressed.MyOnBackPressedCallback;
import main.java.model.SearchHistory;
import main.java.model.SearchResult;
import main.java.repository.HistoryRepository;
import main.java.repository.LocalHistoryRepository;
import main.java.service.history.HistoryService;
import main.java.service.history.HistoryServiceImpl;
import main.java.service.recipe.GptRecipeService;
import main.java.service.recipe.RecipeService;
import main.java.util.LoadingDialog;
import main.java.util.error.ErrorFormat;
import main.java.util.error.dialog.ErrorDialog;
import main.java.util.http.HttpService;
import main.java.util.parser.GptResponseParser;

public class SearchActivity extends AppCompatActivity {
    HistoryRepository historyRepository = new LocalHistoryRepository(this);
    HistoryService historyService = new HistoryServiceImpl(historyRepository);

    HistoryRecyclerViewAdapter historyAdapter;

    RecipeService recipeService
            = new GptRecipeService(new HttpService(), new GptResponseParser(), historyService);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar searchActivityToolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(searchActivityToolbar);

        final LoadingDialog loadingDialog = new LoadingDialog(SearchActivity.this);

        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);

        SearchView recipeSearch = findViewById(R.id.search_recipe);
        recipeSearch.setSubmitButtonEnabled(true);

        RecyclerView historyRecyclerView = findViewById(R.id.history_items);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        if (historyAdapter != null)
            return;

        List<SearchHistory> searchHistories = historyService.getSearchHistories(5);

        // 로그 아웃 버튼 만들고 클릭 시 MainActivity로 이동하도록 코드 추가 ******************************************

        // 뒤로 가기 버튼을 2번 눌러야 앱이 종료되는 로직
        MyOnBackPressedCallback searchActivityCallback = new MyOnBackPressedCallback(true, SearchActivity.this);

        // OnBackPressedCallback 객체를 현재 액티비티의 OnBackPressedDispatcher에 등록
        getOnBackPressedDispatcher().addCallback(SearchActivity.this, searchActivityCallback);

        if (searchHistories == null)
            return;

        historyAdapter = new HistoryRecyclerViewAdapter(searchHistories, SearchActivity.this);
        historyRecyclerView.setAdapter(historyAdapter);

        recipeSearch.setOnQueryTextListener(getOnQueryTextListener(loadingDialog));
    }

    private SearchView.OnQueryTextListener getOnQueryTextListener(LoadingDialog loadingDialog){
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadingDialog.show();

                CompletableFuture<SearchResult> futureResult = recipeService.search(query);

                futureResult.thenAccept(result -> {
                    loadingDialog.dismiss();

                    Intent goToResultActivity = new Intent(getApplicationContext(), ResultActivity.class);

                    goToResultActivity.putExtra(RECIPE_NAME, result.getRecipeName());
                    goToResultActivity.putExtra(INGREDIENTS, result.getIngredients());
                    goToResultActivity.putExtra(COOKING_ORDER, result.getCookingOrder());

                    startActivity(goToResultActivity);
                }).exceptionally(ex -> {
                    loadingDialog.dismiss();

                    String message = Objects.requireNonNull(ex.getMessage());
                    ErrorFormat result = getErrorFromMessage(message);

                    runOnUiThread(() -> {
                        ErrorDialog errorDialog = new ErrorDialog(SearchActivity.this, result);
                        errorDialog.show();
                    });

                    return null;
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        };
    }
}
