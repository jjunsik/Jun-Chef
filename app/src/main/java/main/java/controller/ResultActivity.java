package main.java.controller;

import static main.java.model.constant.ResultConstant.COOKING_ORDER;
import static main.java.model.constant.ResultConstant.INGREDIENTS;
import static main.java.model.constant.ResultConstant.RECIPE_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import main.java.R;

public class ResultActivity extends AppCompatActivity {
    TextView recipeNameTextView, ingredientsTextView, cookingOrderTextView;

    OnBackPressedCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar searchActivityToolbar = findViewById(R.id.result_toolbar);
        setSupportActionBar(searchActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(SEARCH_RESULT_TITLE);

        String getRecipeName = getIntent().getStringExtra(RECIPE_NAME);
        setText(getRecipeFormat(getRecipeName), recipeNameTextView, R.id.result_recipe_name);
        setTextByExtra(INGREDIENTS, ingredientsTextView, R.id.result_ingredients);
        setTextByExtra(COOKING_ORDER, cookingOrderTextView, R.id.result_cookingorder);

        // 생성자의 인자로 true를 전달하여 이 콜백이 기본 동작을 가로채도록 설정
        // 이 콜백은 뒤로 가기 버튼 이벤트를 처리하는 역할
        callback = new OnBackPressedCallback(true) {
            // 기기의 뒤로 가기 버튼 클릭에 대한 이벤트 처리하는 메서드
            @Override
            public void handleOnBackPressed() {
                Intent goToSearchActivity = new Intent(ResultActivity.this, SearchActivity.class);
                // 스택에 쌓이지 않게 하기 위한 코드
                //  스택에 이미 존재하는 동일한 액티비티가 있을 경우 해당 액티비티 위의 모든 액티비티를 제거하고, 해당 액티비티가 최상위로 올라가도록 함
                goToSearchActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goToSearchActivity);
                // 액티비티 종료를 위한 코드
                finish();
            }
        };

        // 생성한 callback 객체를 현재 액티비티의 OnBackPressedDispatcher에 등록하여 뒤로 가기 버튼 이벤트를 처리
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setTextByExtra(String resultConstant, TextView textView, int textViewId) {
        String extraString = getIntent().getStringExtra(resultConstant);
        setText(extraString, textView, textViewId);
    }

    private void setText(String extraText, TextView textView, int textViewId) {
        textView = findViewById(textViewId);
        textView.setText(extraText);
    }

    @NonNull
    private static String getRecipeFormat(String getRecipeName) {
        return "\"" + getRecipeName + "\" 레시피";
    }

    @Override
    protected void onDestroy() {
        callback.remove();
        super.onDestroy();
    }
}
