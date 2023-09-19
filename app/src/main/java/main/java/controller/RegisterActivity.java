package main.java.controller;

import static main.java.controller.constant.ActivityConstant.REGISTER_TITLE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import main.java.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerEmail, registerPasswd;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEmail = findViewById(R.id.register_email);
        registerPasswd = findViewById(R.id.register_pwd);
        Toolbar registerActivityToolbar = findViewById(R.id.register_toolbar);

        setSupportActionBar(registerActivityToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(REGISTER_TITLE);

        signUp = findViewById(R.id.btn_signup);

        signUp.setOnClickListener(v -> {
            // 회원 가입 처리 시작
            String enterEmail = registerEmail.getText().toString();
            String enterPasswd = registerPasswd.getText().toString();

            // 서버에 회원 가입 정보 보내는 코드 작성


            // 정상적으로 가입이 됐다면 검색 화면으로 이동
            Intent goToSearchActivity = new Intent(RegisterActivity.this, SearchActivity.class);
            startActivity(goToSearchActivity);
            finish();
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent goToMainActivity = new Intent(RegisterActivity.this, MainActivity.class);
                goToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goToMainActivity);
                finish();
            }
        };

        getOnBackPressedDispatcher().addCallback(RegisterActivity.this, callback);
    }
}