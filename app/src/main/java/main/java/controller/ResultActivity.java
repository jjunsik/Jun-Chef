package main.java.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import main.java.R;

public class ResultActivity extends AppCompatActivity {
    public static final String SEARCH_RESULT_TITLE = "검색 결과";
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            Intent goToSearchActivity = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(goToSearchActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
