package main.java.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

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
        signUp = findViewById(R.id.btn_signup);

        signUp.setOnClickListener(v -> {
            // 회원 가입 처리 시작
            String enterEmail = registerEmail.getText().toString();
            String enterPasswd = registerPasswd.getText().toString();

            // 서버에 회원 가입 정보 보내는 코드 작성


            // 정상적으로 가입이 됐다면 검색 화면으로 이동
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
            finish();
        });
    }
}