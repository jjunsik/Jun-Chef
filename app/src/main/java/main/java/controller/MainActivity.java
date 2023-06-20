package main.java.controller;

import static main.java.controller.constant.ActivityConstant.BACK_TIME;
import static main.java.controller.constant.ActivityConstant.BACK_TOAST_MESSAGE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import main.java.R;
import main.java.controller.constant.ActivityConstant;

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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK)
            return super.onKeyDown(keyCode, event);

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return true;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, BACK_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() ->
                doubleBackToExitPressedOnce = false, BACK_TIME); // 1초 딜레이

        return true;
    }
}