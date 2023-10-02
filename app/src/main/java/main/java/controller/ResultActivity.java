package main.java.controller;

import static main.java.controller.constant.ActivityConstant.SEARCH_RESULT_TITLE;
import static main.java.model.constant.ResultConstant.COOKING_ORDER;
import static main.java.model.constant.ResultConstant.INGREDIENTS;
import static main.java.model.constant.ResultConstant.RECIPE_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import main.java.R;

public class ResultActivity extends AppCompatActivity {
    TextView recipeNameTextView, ingredientsTextView, cookingOrderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar resultActivityToolbar = findViewById(R.id.result_toolbar);
        setSupportActionBar(resultActivityToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(SEARCH_RESULT_TITLE);

        String getRecipeName = getIntent().getStringExtra(RECIPE_NAME);
        setText(getRecipeFormat(getRecipeName), recipeNameTextView, R.id.result_recipe_name);
        setTextByExtra(INGREDIENTS, ingredientsTextView, R.id.result_ingredients);
        setTextByExtra(COOKING_ORDER, cookingOrderTextView, R.id.result_cookingorder);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("result", "handleOnBackPressed");

                Intent goToSearchActivity = new Intent(ResultActivity.this, SearchActivity.class);
                goToSearchActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goToSearchActivity);
            }
        };

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
}
