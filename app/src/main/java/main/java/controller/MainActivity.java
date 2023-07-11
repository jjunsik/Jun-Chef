package main.java.controller;

import static main.java.controller.constant.ActivityConstant.BACK_TIME;
import static main.java.controller.constant.ActivityConstant.BACK_TOAST_MESSAGE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import main.java.R;

public class MainActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.startSearch).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finishAffinity();
                    System.exit(0);
                } else {
                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(MainActivity.this, BACK_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, BACK_TIME);
                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}