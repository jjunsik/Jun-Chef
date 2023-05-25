package main.java.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import main.java.R;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar searchActivityToolbar = findViewById(R.id.result_toolbar);
        setSupportActionBar(searchActivityToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("검색 결과");
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
