package main.java.controller;

import static main.java.model.constant.ResultConstant.COOKING_ORDER;
import static main.java.model.constant.ResultConstant.INGREDIENTS;
import static main.java.model.constant.ResultConstant.RECIPE_NAME;

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
import main.java.model.SearchHistory;
import main.java.model.SearchResult;
import main.java.repository.HistoryRepository;
import main.java.repository.LocalHistoryRepository;
import main.java.service.history.HistoryService;
import main.java.service.history.HistoryServiceImpl;
import main.java.service.recipe.GptRecipeService;
import main.java.service.recipe.RecipeService;
import main.java.util.LoadingDialog;
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

        // 뒤로 가기 버튼 활성화
        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);

        SearchView recipeSearch = findViewById(R.id.search_recipe);

        // SearchView 옆에 검색 버튼 활성화
        recipeSearch.setSubmitButtonEnabled(true);

        RecyclerView historyRecyclerView = findViewById(R.id.history_items);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (historyAdapter != null)
            return;
        List<SearchHistory> searchHistories = historyService.getSearchHistories(5);

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
                // 검색 버튼을 클릭 시 호출
                loadingDialog.startLoadingDialog();

                CompletableFuture<SearchResult> futureResult = recipeService.search(query);

                // thenAccept(): 네트워크 작업이 완료된 후에 결과를 전달받아 메인 스레드에서 해당 결과를 처리하는 작업을 수행
                futureResult.thenAccept(result -> {
                    if (result == null) {
                        // error 처리
                        // 없음!
                        loadingDialog.dismissDialog();
                    }

                    Intent goToResultActivity = new Intent(getApplicationContext(), ResultActivity.class);

                    goToResultActivity.putExtra(RECIPE_NAME, result.getRecipeName());
                    goToResultActivity.putExtra(INGREDIENTS, result.getIngredients());
                    goToResultActivity.putExtra(COOKING_ORDER, result.getCookingOrder());

                    loadingDialog.dismissDialog();

                    startActivity(goToResultActivity);
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 입력한 텍스트가 변경될 때마다 호출
                return true;
            }
        };
    }
}
