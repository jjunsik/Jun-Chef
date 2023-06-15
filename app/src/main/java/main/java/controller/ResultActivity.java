package main.java.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import main.java.R;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar searchActivityToolbar = findViewById(R.id.result_toolbar);
        setSupportActionBar(searchActivityToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("검색 결과");

        String getRecipeName = getIntent().getStringExtra("recipeName");
        Log.d("TAG", "받아온 레시피 명: " + getRecipeName);
        Toast.makeText(ResultActivity.this, "받아온 레시피 명: " + getRecipeName, Toast.LENGTH_SHORT).show();
    }

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
