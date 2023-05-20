package main.java.controller;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import main.java.R;
import main.java.model.SearchHistory;
import main.java.repository.HistoryRepository;
import main.java.repository.LocalHistoryRepository;
import main.java.service.history.HistoryService;
import main.java.service.history.HistoryServiceImpl;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView1 = findViewById(R.id.item_test1);

        HistoryRepository historyRepository = new LocalHistoryRepository(MainActivity.this);
        HistoryService historyService = new HistoryServiceImpl(historyRepository);
        historyService.addHistory(new SearchHistory("육회"));

        List<SearchHistory> historyList = historyService.getSearchHistories(5);

        textView1.setText(historyList.get(0).getRecipeName() + "---" + historyList.get(0).getCreateDateTime());
    }
}