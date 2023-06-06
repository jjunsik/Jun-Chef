package main.java.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import main.java.R;
import main.java.adapter.HistoryRecyclerViewAdapter;
import main.java.model.SearchResult;
import main.java.repository.HistoryRepository;
import main.java.repository.LocalHistoryRepository;
import main.java.service.history.HistoryService;
import main.java.service.history.HistoryServiceImpl;
import main.java.service.recipe.GptRecipeService;
import main.java.service.recipe.RecipeService;
import main.java.util.http.HttpService;
import main.java.util.parser.GptResponseParser;

public class SearchActivity extends AppCompatActivity {
    HistoryRepository historyRepository = new LocalHistoryRepository(this);
    HistoryService historyService = new HistoryServiceImpl(historyRepository);

    RecipeService recipeService
            = new GptRecipeService(new HttpService(), new GptResponseParser(), historyService);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar searchActivityToolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(searchActivityToolbar);

        // 뒤로 가기 버튼 활성화
        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);

        SearchView recipeSearch = findViewById(R.id.search_recipe);

        // SearchView 옆에 검색 버튼 활성화
        recipeSearch.setSubmitButtonEnabled(true);

        recipeSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 검색 버튼을 클릭 시 호출
                Toast.makeText(SearchActivity.this, "입력한 검색어: " + query, Toast.LENGTH_SHORT).show();

                SearchResult result = recipeService.search(query);
                if (result == null) {
                    // error 처리
                    // 없음!
                    return true;
                }

                Intent goToResultActivity = new Intent(getApplicationContext(), ResultActivity.class);
                goToResultActivity.putExtra("recipe", result.getRecipeName());
                startActivity(goToResultActivity);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 입력한 텍스트가 변경될 때마다 호출
                return true;
            }
        });

        RecyclerView historyRecyclerView = findViewById(R.id.history_items);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (historyRepository.getSearchHistoryCount() != 0) {
            HistoryRecyclerViewAdapter historyAdapter = new HistoryRecyclerViewAdapter(historyService.getSearchHistories(5), this);
            historyRecyclerView.setAdapter(historyAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // 뒤로 가기 버튼 클릭 시 호출
        if (item.getItemId() == android.R.id.home) {
            Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goToMainActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
