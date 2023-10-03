package main.java.controller;

import static main.java.controller.constant.ActivityConstant.getMemberId;
import static main.java.controller.constant.ActivityConstant.setMemberId;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import main.java.R;
import main.java.service.junchef.member.MemberService;
import main.java.util.backpressed.MyOnBackPressedCallback;
import main.java.util.error.ErrorFormat;
import main.java.util.error.dialog.ErrorDialog;
import main.java.util.error.junchef.JunChefException;
import main.java.util.http.junchef.member.MemberHttpService;
import main.java.util.parser.junchef.member.MemberResponseParser;

public class MainActivity extends AppCompatActivity {
    private final MemberService memberService = new MemberService(new MemberHttpService(), new MemberResponseParser());
    private EditText email;
    private EditText passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.member_email);
        passwd = findViewById(R.id.member_pwd);

        findViewById(R.id.btn_login).setOnClickListener(v -> {
            // 서버에 로그인 정보 보내는 코드 작성
            // 사용자 입력 정보
            String userEmail = email.getText().toString();
            String userPasswd = passwd.getText().toString();

            // CompletableFuture를 사용하여 비동기적으로 로그인 수행
            memberService.login(userEmail, userPasswd)
            .thenAcceptAsync(memberId -> {
                setMemberId(memberId);

                Log.d("로그인", "memberId: " +getMemberId());

                // 로그인 성공 후 화면 전환
                Intent goToSearchActivity = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(goToSearchActivity);

                finish();
            })
            .exceptionally(ex -> {
                // 로그인 실패 시 예외 처리
                if (ex != null) {
                    Log.d("junchef", "로그인 실패(MainActivity)" + ex.getClass().getName());

                    JunChefException junChefException;

                    if (ex.getCause() instanceof JunChefException) {
                        Log.d("junchef", "준쉐프 예외임");

                        junChefException = (JunChefException) ex.getCause();
                        Log.d("junchef", "예외 잘 받음(MainActivity)" + Objects.requireNonNull(junChefException).getCode() + junChefException.getTitle() + junChefException.getMessage());

                        ErrorFormat errorFormat = new ErrorFormat(junChefException.getTitle(), junChefException.getMessage());
                        Log.d("junchef", "에러 포맷 완성" + errorFormat.getTitle() + errorFormat.getMessage());

                        runOnUiThread(() -> {
                            Log.d("junchef", "에러 다이얼로그 객체 만들기 전");
                            ErrorDialog errorDialog = new ErrorDialog(MainActivity.this, errorFormat);

                            Log.d("junchef", "에러 다이얼로그 객체 만들고 시작 전" + errorDialog);
                            errorDialog.show();
                        });
                    }
                }

                return null;
            });
        });

        findViewById(R.id.btn_register).setOnClickListener(v -> {
            // 회원 가입 페이지로 이동
            Intent goToRegisterActivity = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(goToRegisterActivity);
        });

        MyOnBackPressedCallback mainActivityCallback = new MyOnBackPressedCallback(true, MainActivity.this);

        // OnBackPressedCallback 객체를 현재 액티비티의 OnBackPressedDispatcher에 등록
        getOnBackPressedDispatcher().addCallback(MainActivity.this, mainActivityCallback);
    }
}