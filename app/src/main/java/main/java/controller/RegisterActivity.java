package main.java.controller;

import static main.java.controller.constant.ActivityConstant.REGISTER_TITLE;
import static main.java.controller.constant.ActivityConstant.getMemberId;
import static main.java.controller.constant.ActivityConstant.setMemberId;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import main.java.R;
import main.java.service.junchef.member.MemberService;
import main.java.util.error.ErrorFormat;
import main.java.util.error.dialog.ErrorDialog;
import main.java.util.error.junchef.JunChefException;
import main.java.util.http.junchef.member.MemberHttpService;
import main.java.util.parser.junchef.member.MemberResponseParser;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerEmail, registerPasswd, registerName;
    private final MemberService memberService = new MemberService(new MemberHttpService(this), new MemberResponseParser());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerName = findViewById(R.id.register_name);
        registerEmail = findViewById(R.id.register_email);
        registerPasswd = findViewById(R.id.register_pwd);

        Toolbar registerActivityToolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(registerActivityToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(REGISTER_TITLE);

        Button signUp = findViewById(R.id.btn_signup);

        signUp.setOnClickListener(v -> {
            // 회원 가입 처리 시작
            String enterName = registerName.getText().toString();
            String enterEmail = registerEmail.getText().toString();
            String enterPasswd = registerPasswd.getText().toString();

            // 서버에 회원 가입 정보 보내는 코드 작성
            Log.d("가입 정보", "Register: " + enterName + enterEmail + enterPasswd);

            memberService.joinMember(enterName, enterEmail, enterPasswd)
            .thenAcceptAsync(memberId -> {
                setMemberId(memberId);
                Log.d("회원 가입", "memberId: " +getMemberId());

                Intent goToSearchActivity = new Intent(RegisterActivity.this, SearchActivity.class);
                startActivity(goToSearchActivity);

                finish();
            })
            .exceptionally(ex -> {
                // 회원 가입 실패 시 예외 처리
                if (ex != null) {
                    Log.d("junchef", "회원 가입 실패(RegisterActivity)" + ex.getClass().getName());

                    JunChefException junChefException;

                    if (ex.getCause() instanceof JunChefException) {
                        Log.d("junchef", "준쉐프 예외임");

                        junChefException = (JunChefException) ex.getCause();
                        Log.d("junchef", "예외 잘 받음(RegisterActivity)" + Objects.requireNonNull(junChefException).getCode() + junChefException.getTitle() + junChefException.getMessage());

                        ErrorFormat errorFormat = new ErrorFormat(junChefException.getTitle(), junChefException.getMessage());
                        Log.d("junchef", "에러 포맷 완성" + errorFormat.getTitle() + errorFormat.getMessage());

                        runOnUiThread(() -> {
                            Log.d("junchef", "에러 다이얼로그 객체 만들기 전");
                            ErrorDialog errorDialog = new ErrorDialog(RegisterActivity.this, errorFormat);

                            Log.d("junchef", "에러 다이얼로그 객체 만들고 시작 전" + errorDialog);
                            errorDialog.show();
                        });
                    }
                }

                return null;
            });
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent goToMainActivity = new Intent(RegisterActivity.this, MainActivity.class);
                goToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goToMainActivity);
            }
        };

        getOnBackPressedDispatcher().addCallback(RegisterActivity.this, callback);
    }
}