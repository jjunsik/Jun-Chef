package main.java.controller;

import static main.java.model.constant.ResultConstant.COOKING_ORDER;
import static main.java.model.constant.ResultConstant.INGREDIENTS;
import static main.java.model.constant.ResultConstant.RECIPE_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import main.java.R;
import main.java.adapter.HistoryRecyclerViewAdapter;
import main.java.model.SearchHistory;
import main.java.model.SearchResult;
import main.java.model.constant.ResultConstant;
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

        recipeSearch.setOnQueryTextListener(getOnQueryTextListener(loadingDialog, historyRecyclerView));
    }

    private SearchView.OnQueryTextListener getOnQueryTextListener(LoadingDialog loadingDialog,
                                                                  RecyclerView historyRecyclerView){
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 검색 버튼을 클릭 시 호출
                loadingDialog.startLoadingDialog();
                Toast.makeText(SearchActivity.this, "입력한 검색어: " + query, Toast.LENGTH_SHORT).show();

                SearchResult result = recipeService.search(query);

                if (result == null) {
                    // error 처리
                    // 없음!
                    loadingDialog.dismissDialog();
                    return true;
                }

                if (historyRepository.getSearchHistoryCount() != 0) {
                    historyAdapter = new HistoryRecyclerViewAdapter(historyService.getSearchHistories(5), SearchActivity.this);
                    historyRecyclerView.setAdapter(historyAdapter);
                }

                Intent goToResultActivity = new Intent(getApplicationContext(), ResultActivity.class);
                goToResultActivity.putExtra(RECIPE_NAME, result.getRecipeName());
                goToResultActivity.putExtra(INGREDIENTS, result.getIngredients());
                goToResultActivity.putExtra(COOKING_ORDER, result.getCookingOrder());
                startActivity(goToResultActivity);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 입력한 텍스트가 변경될 때마다 호출
                return true;
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "SearchActivity destroyed");
    }

//    private void updateSearchResults() {
//        // 이전에 검색한 결과를 가져오는 로직
//        // ...
//        if (historyAdapter == null) {
//            List<SearchHistory> searchHistories = historyService.getSearchHistories(5);
//            if (searchHistories != null) { // null 체크
//                historyAdapter = new HistoryRecyclerViewAdapter(searchHistories, SearchActivity.this);
//                historyRecyclerView.setAdapter(historyAdapter);
//            }
//        }
//
//        // 검색 결과를 RecyclerView에 표시
//        if (historyAdapter != null) {
//            List<SearchHistory> searchHistories = historyService.getSearchHistories(5);
//            if (searchHistories != null) {
//                historyAdapter.setSearchHistories(searchHistories);
//                historyAdapter.notifyDataSetChanged();
//            }
//        }
//    }



//    @Override
////    툴바에 존재하는 뒤로 가기 버튼 뿐만 아니라 다른 버튼까지.
////    즉, 툴바의 버튼을 제어하는 메서드
//    public boolean onOptionsItemSelected(MenuItem item){
//        if (item.getItemId() == android.R.id.home) {
//            Intent goToMainActivity = new Intent(SearchActivity.this, MainActivity.class);
//            startActivity(goToMainActivity);
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//    @Override
//    // 툴바 뿐만 아니라 모든 뒤로 가기 버튼에 대한 처리를 하는 메서드
//    public void onBackPressed(){
//        Intent goToMainActivity = new Intent(SearchActivity.this, MainActivity.class);
//        startActivity(goToMainActivity);
//        super.onBackPressed();
//        // 뒤로 가기 버튼에 대한 동작을 처리할 때 액티비티를 종료하고 싶을 때 호출
//        // 하지만, Intent를 사용해 다른 액티비티로 이동하는 코드에서는 일반적으로 사용 X
//        // finish()를 해주지 않으면 첫 화면에서 뒤로 가기를 눌렀을 때, 앱이 종료되는 것이 아니라 다시 검색화면으로 나오므로 finish()를 해주어야 한다.
//    }
}
