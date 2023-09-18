package main.java.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import main.java.R;
import main.java.controller.backpressed.MyOnBackPressedCallback;

public class MainActivity extends AppCompatActivity {
    // 뒤로 가기 버튼을 두 번 눌러야 하는지 여부를 추적하는 데 사용
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_login).setOnClickListener(v -> {
            // 서버에 로그인 정보 보내는 코드 작성


            Intent goToSearchActivity = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(goToSearchActivity);
            finish();
        });

        findViewById(R.id.btn_register).setOnClickListener(v -> {
            // 회원 가입 페이지로 이동
            Intent goToRegisterActivity = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(goToRegisterActivity);
            finish();
        });

        MyOnBackPressedCallback mainActivityCallback = new MyOnBackPressedCallback(true, MainActivity.this);

        // OnBackPressedCallback 객체를 현재 액티비티의 OnBackPressedDispatcher에 등록
        getOnBackPressedDispatcher().addCallback(MainActivity.this, mainActivityCallback);
    }
}