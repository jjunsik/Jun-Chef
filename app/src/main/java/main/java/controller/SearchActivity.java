package main.java.controller;

import static main.java.controller.constant.ActivityConstant.getMemberId;
import static main.java.controller.constant.ActivityConstant.setMemberId;
import static main.java.model.constant.ResultConstant.COOKING_ORDER;
import static main.java.model.constant.ResultConstant.INGREDIENTS;
import static main.java.model.constant.ResultConstant.RECIPE_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import main.java.R;
import main.java.adapter.HistoryRecyclerViewAdapter;
import main.java.service.junchef.history.JunChefHistoryService;
import main.java.service.junchef.member.MemberService;
import main.java.service.junchef.recipe.JunChefRecipeService;
import main.java.util.LoadingDialog;
import main.java.util.backpressed.MyOnBackPressedCallback;
import main.java.util.error.ErrorFormat;
import main.java.util.error.dialog.ErrorDialog;
import main.java.util.error.junchef.JunChefException;
import main.java.util.http.junchef.history.HistoryHttpService;
import main.java.util.http.junchef.member.MemberHttpService;
import main.java.util.http.junchef.recipe.RecipeHttpService;
import main.java.util.parser.junchef.history.HistoryResponseParser;
import main.java.util.parser.junchef.member.MemberResponseParser;
import main.java.util.parser.junchef.recipe.RecipeResponseParser;

public class SearchActivity extends AppCompatActivity {
    private HistoryRecyclerViewAdapter historyAdapter;
    private final JunChefRecipeService junChefRecipeService = new JunChefRecipeService(new RecipeHttpService(), new RecipeResponseParser());
    private final JunChefHistoryService junChefHistoryService = new JunChefHistoryService(new HistoryHttpService(), new HistoryResponseParser());
    private final MemberService memberService = new MemberService(new MemberHttpService(), new MemberResponseParser());
    private final Long memberId = getMemberId();
    private final Long deleteMemberId = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar searchActivityToolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(searchActivityToolbar);

        Button btnLogout = findViewById(R.id.btn_logout);

        final LoadingDialog loadingDialog = new LoadingDialog(SearchActivity.this);

        SearchView recipeSearch = findViewById(R.id.search_recipe);
        recipeSearch.setSubmitButtonEnabled(true);

        RecyclerView historyRecyclerView = findViewById(R.id.history_items);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        if (historyAdapter != null)
            return;

        MyOnBackPressedCallback searchActivityCallback = new MyOnBackPressedCallback(true, SearchActivity.this);
        getOnBackPressedDispatcher().addCallback(SearchActivity.this, searchActivityCallback);

        Log.d("검색 화면에서 회원 ID값", "onCreate: " + memberId);

        junChefHistoryService.getMemberHistories(memberId)
        .thenAcceptAsync(searchHistories -> {
            runOnUiThread(() -> {
                historyAdapter = new HistoryRecyclerViewAdapter(searchHistories, SearchActivity.this, memberId);
                historyRecyclerView.setAdapter(historyAdapter);
            });

            recipeSearch.setOnQueryTextListener(getOnQueryTextListener(loadingDialog));
        })
        .exceptionally(ex -> {
            // 멤버 최근 검색어 목록을 못 가져올 시 예외 처리
            if (ex != null) {
                Log.d("junchef", "최근 검색어 목록 조회 실패(SearchActivity)" + ex.getClass().getName());

                JunChefException junChefException;

                if (ex.getCause() instanceof JunChefException) {
                    Log.d("junchef", "준쉐프 예외임");

                    junChefException = (JunChefException) ex.getCause();
                    Log.d("junchef", "예외 잘 받음(SearchActivity)" + Objects.requireNonNull(junChefException).getCode() + junChefException.getTitle() + junChefException.getMessage());

                    ErrorFormat errorFormat = new ErrorFormat(junChefException.getTitle(), junChefException.getMessage());
                    Log.d("junchef", "에러 포맷 완성" + errorFormat.getTitle() + errorFormat.getMessage());

                    runOnUiThread(() -> {
                        Log.d("junchef", "에러 다이얼로그 객체 만들기 전");
                        ErrorDialog errorDialog = new ErrorDialog(SearchActivity.this, errorFormat);

                        Log.d("junchef", "에러 다이얼로그 객체 만들고 시작 전" + errorDialog);
                        errorDialog.show();
                    });
                }
            }

            return null;
        });

        // 로그 아웃
        btnLogout.setOnClickListener( v -> memberService.logout(memberId)
        .thenAcceptAsync(logoutMemberId -> {
            Log.d("로그 아웃 버튼 클릭", "logout");

            setMemberId(deleteMemberId);
            Log.d("로그 아웃 후 회원 ID 값", "onCreate: " + getMemberId());

            Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goToMainActivity);

            finish();
        }).exceptionally(ex -> {
            if (ex != null) {
                Log.d("junchef", "로그아웃 실패(SearchActivity)" + ex.getClass().getName());

                JunChefException junChefException;

                if (ex.getCause() instanceof JunChefException) {
                    Log.d("junchef", "준쉐프 예외임");

                    junChefException = (JunChefException) ex.getCause();
                    Log.d("junchef", "예외 잘 받음(SearchActivity)" + Objects.requireNonNull(junChefException).getCode() + junChefException.getTitle() + junChefException.getMessage());

                    ErrorFormat errorFormat = new ErrorFormat(junChefException.getTitle(), junChefException.getMessage());
                    Log.d("junchef", "에러 포맷 완성" + errorFormat.getTitle() + errorFormat.getMessage());

                    runOnUiThread(() -> {
                        Log.d("junchef", "에러 다이얼로그 객체 만들기 전");
                        ErrorDialog errorDialog = new ErrorDialog(SearchActivity.this, errorFormat);

                        Log.d("junchef", "에러 다이얼로그 객체 만들고 시작 전" + errorDialog);
                        errorDialog.show();
                    });
                }
            }

            return null;
        }));
    }

    private SearchView.OnQueryTextListener getOnQueryTextListener(LoadingDialog loadingDialog){
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadingDialog.show();

                junChefRecipeService.search(memberId, query)
                .thenAcceptAsync(result -> {
                    loadingDialog.dismiss();

                    Intent goToResultActivity = new Intent(getApplicationContext(), ResultActivity.class);

                    goToResultActivity.putExtra(RECIPE_NAME, result.getRecipeName());
                    goToResultActivity.putExtra(INGREDIENTS, result.getIngredients());
                    goToResultActivity.putExtra(COOKING_ORDER, result.getCookingOrder());

                    startActivity(goToResultActivity);
                })
                .exceptionally(ex -> {
                    loadingDialog.dismiss();

                    if (ex != null) {
                        Log.d("junchef", "검색 실패(SearchActivity)" + ex.getCause());

                        JunChefException junChefException;

                        if (ex.getCause() instanceof JunChefException) {
                            Log.d("junchef", "준쉐프 예외임");

                            junChefException = (JunChefException) ex.getCause();
                            Log.d("junchef", "예외 잘 받음(SearchActivity)" + Objects.requireNonNull(junChefException).getCode() + junChefException.getTitle() + junChefException.getMessage());

                            ErrorFormat errorFormat = new ErrorFormat(junChefException.getTitle(), junChefException.getMessage());
                            Log.d("junchef", "에러 포맷 완성" + errorFormat.getTitle() + errorFormat.getMessage());

                            runOnUiThread(() -> {
                                Log.d("junchef", "에러 다이얼로그 객체 만들기 전");
                                ErrorDialog errorDialog = new ErrorDialog(SearchActivity.this, errorFormat);

                                Log.d("junchef", "에러 다이얼로그 객체 만들고 시작 전" + errorDialog);
                                errorDialog.show();
                            });
                        }
                    }

                    return null;
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
